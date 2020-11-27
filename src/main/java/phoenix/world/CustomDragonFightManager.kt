package phoenix.world

import com.google.common.collect.*
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.block.Blocks
import net.minecraft.block.pattern.BlockMatcher
import net.minecraft.block.pattern.BlockPattern
import net.minecraft.block.pattern.BlockPattern.PatternHelper
import net.minecraft.block.pattern.BlockPatternBuilder
import net.minecraft.entity.EntityType
import net.minecraft.entity.boss.dragon.EnderDragonEntity
import net.minecraft.entity.boss.dragon.phase.PhaseType
import net.minecraft.entity.item.EnderCrystalEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.IntNBT
import net.minecraft.nbt.ListNBT
import net.minecraft.nbt.NBTUtil
import net.minecraft.tileentity.EndPortalTileEntity
import net.minecraft.util.*
import net.minecraft.util.Unit
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.BossInfo
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.chunk.ChunkStatus
import net.minecraft.world.gen.Heightmap
import net.minecraft.world.gen.feature.*
import net.minecraft.world.server.ChunkHolder.LocationType
import net.minecraft.world.server.ServerBossInfo
import net.minecraft.world.server.ServerWorld
import net.minecraft.world.server.TicketType
import phoenix.Phoenix
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class CustomDragonFightManager(var world: ServerWorld, compound: CompoundNBT, dim: EndDimension)
{
    private val LOGGER = Phoenix.LOGGER
    private val VALID_PLAYER = EntityPredicates.IS_ALIVE.and(EntityPredicates.withinRange(0.0, 128.0, 0.0, 192.0))
    private val bossInfo = ServerBossInfo(TranslationTextComponent("entity.minecraft.ender_dragon"), BossInfo.Color.PINK, BossInfo.Overlay.PROGRESS).setPlayEndBossMusic(true).setCreateFog(true) as ServerBossInfo
    private val gateways: MutableList<Int> = Lists.newArrayList()
    private var portalPattern: BlockPattern? = null
    private var ticksSinceDragonSeen = 0
    private var aliveCrystals = 0
    private var ticksSinceCrystalsScanned = 0
    private var ticksSinceLastPlayerScan = 0
    private var dragonKilled = false
    private var previouslyKilled = false
    private var dragonUniqueId: UUID? = null
    private var scanForLegacyFight = true
    private var exitPortalLocation: BlockPos? = null
    private var respawnState: CustomDragonSpawnState? = null
    private var respawnStateTicks = 0
    private var crystals: List<EnderCrystalEntity>? = null

    init
    {
        if (compound.contains("DragonKilled", 99))
        {
            if (compound.hasUniqueId("DragonUUID"))
            {
                dragonUniqueId = compound.getUniqueId("DragonUUID")
            }
            dragonKilled = compound.getBoolean("DragonKilled")
            previouslyKilled = compound.getBoolean("PreviouslyKilled")
            scanForLegacyFight = !compound.getBoolean("LegacyScanPerformed") // Forge: fix MC-105080
            if (compound.getBoolean("IsRespawning"))
            {
                respawnState = CustomDragonSpawnState.START
            }
            if (compound.contains("ExitPortalLocation", 10))
            {
                exitPortalLocation = NBTUtil.readBlockPos(compound.getCompound("ExitPortalLocation"))
            }
        }
        else
        {
            dragonKilled = true
            previouslyKilled = true
        }
        if (compound.contains("Gateways", 9))
        {
            val list = compound.getList("Gateways", 3)
            for (i in list.indices)
            {
                gateways.add(list.getInt(i))
            }
        } else
        {
            gateways.addAll(ContiguousSet.create(Range.closedOpen(0, 20), DiscreteDomain.integers()))
            gateways.shuffle(Random(dim.seed))
        }
        portalPattern = BlockPatternBuilder.start().aisle("       ", "       ", "       ", "   #   ", "       ", "       ", "       ").aisle("       ", "       ", "       ", "   #   ", "       ", "       ", "       ").aisle("       ", "       ", "       ", "   #   ", "       ", "       ", "       ").aisle("  ###  ", " #   # ", "#     #", "#  #  #", "#     #", " #   # ", "  ###  ").aisle("       ", "  ###  ", " ##### ", " ##### ", " ##### ", "  ###  ", "       ").where('#', CachedBlockInfo.hasState(BlockMatcher.forBlock(Blocks.BEDROCK))).build()
    }

    fun write(): CompoundNBT
    {
        val res = CompoundNBT()
        if (dragonUniqueId != null)
        {
            res.putUniqueId("DragonUUID", dragonUniqueId!!)
        }
        res.putBoolean("DragonKilled", dragonKilled)
        res.putBoolean("PreviouslyKilled", previouslyKilled)
        res.putBoolean("LegacyScanPerformed", !scanForLegacyFight) // Forge: fix MC-105080
        if (exitPortalLocation != null)
        {
            res.put("ExitPortalLocation", NBTUtil.writeBlockPos(exitPortalLocation!!))
        }
        val listnbt = ListNBT()
        for (i in gateways)
        {
            listnbt.add(IntNBT.valueOf(i))
        }
        res.put("Gateways", listnbt)
        return res
    }

    fun tick() : kotlin.Unit
    {
        bossInfo.isVisible = !dragonKilled
        if (++ticksSinceLastPlayerScan >= 20)
        {
            updatePlayers()
            ticksSinceLastPlayerScan = 0
        }
        if (!bossInfo.players.isEmpty())
        {
            world.chunkProvider.registerTicket(TicketType.DRAGON, ChunkPos(0, 0), 9, Unit.INSTANCE)
            val flag = isFightAreaLoaded()
            if (scanForLegacyFight && flag)
            {
                scanForLegacyFight()
                scanForLegacyFight = false
            }
            if (respawnState != null)
            {
                if (crystals == null && flag)
                {
                    respawnState = null
                    tryRespawnDragon()
                }
                respawnState!!.process(world, this, crystals, respawnStateTicks++, exitPortalLocation)
            }
            if (!dragonKilled)
            {
                if ((dragonUniqueId == null || ++ticksSinceDragonSeen >= 1200) && flag)
                {
                    findOrCreateDragon()
                    ticksSinceDragonSeen = 0
                }
                if (++ticksSinceCrystalsScanned >= 100 && flag)
                {
                    findAliveCrystals()
                    ticksSinceCrystalsScanned = 0
                }
            }
        } else
        {
            world.chunkProvider.releaseTicket(TicketType.DRAGON, ChunkPos(0, 0), 9, Unit.INSTANCE)
        }
    }

    private fun scanForLegacyFight()
    {
        LOGGER.info("Scanning for legacy world dragon fight...")
        val flag = exitPortalExists()
        if (flag)
        {
            LOGGER.info("Found that the dragon has been killed in this world already.")
            previouslyKilled = true
        } else
        {
            LOGGER.info("Found that the dragon has not yet been killed in this world.")
            previouslyKilled = false
            if (findExitPortal() == null)
            {
                generatePortal(false)
            }
        }
        val list = world.dragons
        if (list.isEmpty())
        {
            dragonKilled = true
        } else
        {
            val enderdragonentity = list[0]
            dragonUniqueId = enderdragonentity.uniqueID
            LOGGER.info("Found that there's a dragon still alive ({})", enderdragonentity as Any)
            dragonKilled = false
            if (!flag)
            {
                LOGGER.info("But we didn't have a portal, let's remove it.")
                enderdragonentity.remove()
                dragonUniqueId = null
            }
        }
        if (!previouslyKilled && dragonKilled)
        {
            dragonKilled = false
        }
    }

    private fun findOrCreateDragon()
    {
        val list = world.dragons
        if (list.isEmpty())
        {
            LOGGER.debug("Haven't seen the dragon, respawning it")
            createNewDragon()
        } else
        {
            LOGGER.debug("Haven't seen our dragon, but found another one to use.")
            dragonUniqueId = list[0].uniqueID
        }
    }

    protected fun setRespawnState(state: CustomDragonSpawnState)
    {
        checkNotNull(respawnState) { "Dragon respawn isn't in progress, can't skip ahead in the animation." }
        respawnStateTicks = 0
        if (state === CustomDragonSpawnState.END)
        {
            respawnState = null
            dragonKilled = false
            val dragon = createNewDragon()
            for (player in bossInfo.players)
            {
                CriteriaTriggers.SUMMONED_ENTITY.trigger(player, dragon)
            }
        } else
        {
            respawnState = state
        }
    }

    private fun exitPortalExists(): Boolean
    {
        for (i in -8..8)
        {
            for (j in -8..8)
            {
                val chunk = world.getChunk(i, j)
                for (tile in chunk.tileEntityMap.values)
                {
                    if (tile is EndPortalTileEntity)
                    {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun findExitPortal(): PatternHelper?
    {
        for (i in -8..8)
        {
            for (j in -8..8)
            {
                val chunk = world.getChunk(i, j)
                for (tileentity in chunk.tileEntityMap.values)
                {
                    if (tileentity is EndPortalTileEntity)
                    {
                        val pattern = portalPattern!!.match(world, tileentity.getPos())
                        if (pattern != null)
                        {
                            val blockpos = pattern.translateOffset(3, 3, 3).pos
                            if (exitPortalLocation == null && blockpos.x == 0 && blockpos.z == 0)
                            {
                                exitPortalLocation = blockpos
                            }
                            return pattern
                        }
                    }
                }
            }
        }
        val k = world.getHeight(Heightmap.Type.MOTION_BLOCKING, EndPodiumFeature.END_PODIUM_LOCATION).y
        for (l in k downTo 0)
        {
            val patternHelper = portalPattern!!.match(world, BlockPos(EndPodiumFeature.END_PODIUM_LOCATION.x, l, EndPodiumFeature.END_PODIUM_LOCATION.z))
            if (patternHelper != null)
            {
                if (exitPortalLocation == null)
                {
                    exitPortalLocation = patternHelper.translateOffset(3, 3, 3).pos
                }
                return patternHelper
            }
        }
        return null
    }

    private fun isFightAreaLoaded(): Boolean
    {
        for (i in -8..8)
        {
            for (j in 8..8)
            {
                val ichunk = world.getChunk(i, j, ChunkStatus.FULL, false) as? Chunk
                        ?: return false
                val locationType = ichunk.locationType
                if (!locationType.isAtLeast(LocationType.TICKING))
                {
                    return false
                }
            }
        }
        return true
    }

    private fun updatePlayers()
    {
        val set: MutableSet<ServerPlayerEntity> = Sets.newHashSet()
        for (player in world.getPlayers(VALID_PLAYER))
        {
            bossInfo.addPlayer(player)
            set.add(player)
        }
        val set1: MutableSet<ServerPlayerEntity> = Sets.newHashSet(bossInfo.players)
        set1.removeAll(set)
        for (player in set1)
        {
            bossInfo.removePlayer(player)
        }
    }

    private fun findAliveCrystals()
    {
        ticksSinceCrystalsScanned = 0
        aliveCrystals = 0
        for (spike in EndSpikeFeature.generateSpikes(world))
        {
            aliveCrystals += world.getEntitiesWithinAABB(EnderCrystalEntity::class.java, spike.topBoundingBox).size
        }
        LOGGER.debug("Found {} end crystals still alive", aliveCrystals)
    }

    fun processDragonDeath(dragon: EnderDragonEntity)
    {
        if (dragon.uniqueID == dragonUniqueId)
        {
            bossInfo.percent = 0.0f
            bossInfo.isVisible = false
            generatePortal(true)
            spawnNewGateway()
            if (!previouslyKilled)
            {
                world.setBlockState(world.getHeight(Heightmap.Type.MOTION_BLOCKING, EndPodiumFeature.END_PODIUM_LOCATION), Blocks.DRAGON_EGG.defaultState)
            }
            previouslyKilled = true
            dragonKilled = true
        }
    }

    private fun spawnNewGateway()
    {
        if (gateways.isNotEmpty())
        {
            val i = gateways.removeAt(gateways.size - 1)
            val j = MathHelper.floor(96.0 * cos(2.0 * (-Math.PI + 0.15707963267948966 * i.toDouble())))
            val k = MathHelper.floor(96.0 * sin(2.0 * (-Math.PI + 0.15707963267948966 * i.toDouble())))
            generateGateway(BlockPos(j, 75, k))
        }
    }

    private fun generateGateway(pos: BlockPos)
    {
        world.playEvent(3000, pos, 0)
        Feature.END_GATEWAY.withConfiguration(EndGatewayConfig.func_214698_a()).place(world, world.chunkProvider.chunkGenerator, Random(), pos)
    }

    private fun generatePortal(active: Boolean)
    {
        val endpodiumfeature = EndPodiumFeature(active)
        if (exitPortalLocation == null)
        {
            exitPortalLocation = world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.END_PODIUM_LOCATION).down()
            while (world.getBlockState(exitPortalLocation!!).block === Blocks.BEDROCK && exitPortalLocation!!.y > world.seaLevel)
            {
                exitPortalLocation = exitPortalLocation!!.down()
            }
        }
        endpodiumfeature.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).place(world, world.chunkProvider.chunkGenerator, Random(), exitPortalLocation!!)
    }

    private fun createNewDragon(): EnderDragonEntity
    {
        world.getChunkAt(BlockPos(0, 128, 0))
        val dragon = EntityType.ENDER_DRAGON.create(world)
        dragon!!.phaseManager.setPhase(PhaseType.HOLDING_PATTERN)
        dragon.setLocationAndAngles(0.0, 128.0, 0.0, world.rand.nextFloat() * 360.0f, 0.0f)
        world.addEntity(dragon)
        dragonUniqueId = dragon.uniqueID
        return dragon
    }

    fun dragonUpdate(dragonIn: EnderDragonEntity)
    {
        if (dragonIn.uniqueID == dragonUniqueId)
        {
            bossInfo.percent = dragonIn.health / dragonIn.maxHealth
            ticksSinceDragonSeen = 0
            if (dragonIn.hasCustomName())
            {
                bossInfo.name = dragonIn.displayName
            }
        }
    }

    fun getNumAliveCrystals(): Int
    {
        return aliveCrystals
    }

    fun onCrystalDestroyed(crystal: EnderCrystalEntity, dmgSrc: DamageSource?)
    {
        if (respawnState != null && crystals!!.contains(crystal))
        {
            LOGGER.debug("Aborting respawn sequence")
            respawnState = null
            respawnStateTicks = 0
            resetSpikeCrystals()
            generatePortal(true)
        } else
        {
            findAliveCrystals()
            val entity = world.getEntityByUuid(dragonUniqueId!!)
            if (entity is EnderDragonEntity)
            {
                entity.onCrystalDestroyed(crystal, BlockPos(crystal), dmgSrc)
            }
        }
    }

    fun hasPreviouslyKilledDragon(): Boolean
    {
        return previouslyKilled
    }

    fun tryRespawnDragon()
    {
        if (dragonKilled && respawnState == null)
        {
            var blockpos = exitPortalLocation
            if (blockpos == null)
            {
                LOGGER.debug("Tried to respawn, but need to find the portal first.")
                val patternHelper = findExitPortal()
                if (patternHelper == null)
                {
                    LOGGER.debug("Couldn't find a portal, so we made one.")
                    generatePortal(true)
                } else
                {
                    LOGGER.debug("Found the exit portal & temporarily using it.")
                }
                blockpos = exitPortalLocation
            }
            val list1: MutableList<EnderCrystalEntity> = Lists.newArrayList()
            val blockpos1 = blockpos!!.up(1)
            for (direction in Direction.Plane.HORIZONTAL)
            {
                val list = world.getEntitiesWithinAABB(EnderCrystalEntity::class.java, AxisAlignedBB(blockpos1.offset(direction, 2)))
                if (list.isEmpty())
                {
                    return
                }
                list1.addAll(list)
            }
            LOGGER.debug("Found all crystals, respawning dragon.")
            respawnDragon(list1)
        }
    }

    private fun respawnDragon(crystalsIn: List<EnderCrystalEntity>)
    {
        if (dragonKilled && respawnState == null)
        {
            var portal = findExitPortal()
            while (portal != null)
            {
                for (i in 0 until portalPattern!!.palmLength)
                {
                    for (j in 0 until portalPattern!!.thumbLength)
                    {
                        for (k in 0 until portalPattern!!.fingerLength)
                        {
                            val ctx = portal.translateOffset(i, j, k)
                            if (ctx.blockState.block === Blocks.BEDROCK || ctx.blockState.block === Blocks.END_PORTAL)
                            {
                                world.setBlockState(ctx.pos, Blocks.END_STONE.defaultState)
                            }
                        }
                    }
                }
                portal = findExitPortal()
            }
            respawnState = CustomDragonSpawnState.START
            respawnStateTicks = 0
            generatePortal(false)
            crystals = crystalsIn
        }
    }

    fun resetSpikeCrystals()
    {
        for (spike in EndSpikeFeature.generateSpikes(world))
        {
            for (entity in world.getEntitiesWithinAABB(EnderCrystalEntity::class.java, spike.topBoundingBox))
            {
                entity.isInvulnerable = false
                entity.beamTarget = null
            }
        }
    }

    fun addPlayer(player: ServerPlayerEntity) = bossInfo.addPlayer(player)
    fun removePlayer(player: ServerPlayerEntity) = bossInfo.removePlayer(player)
}
