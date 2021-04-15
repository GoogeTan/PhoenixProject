package phoenix.world

import com.google.common.collect.*
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.block.Blocks
import net.minecraft.block.pattern.BlockMatcher
import net.minecraft.block.pattern.BlockPattern
import net.minecraft.block.pattern.BlockPattern.PatternHelper
import net.minecraft.block.pattern.BlockPatternBuilder
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
import org.apache.logging.log4j.LogManager
import phoenix.enity.EnderCrystalEntity
import phoenix.enity.boss.AbstractEnderDragonEntity
import java.util.*

open class CustomDragonFightManager(
    var world: ServerWorld,
    compound: CompoundNBT,
    dim: EndDimension
)
{
    var bossInfo = ServerBossInfo(
        TranslationTextComponent("entity.minecraft.ender_dragon"),
        BossInfo.Color.PINK,
        BossInfo.Overlay.PROGRESS
    ).setPlayEndBossMusic(true).setCreateFog(true) as ServerBossInfo
    var gateways: MutableList<Int> = Lists.newArrayList()
    var portalPattern: BlockPattern
    var ticksSinceDragonSeen = 0
    var numAliveCrystals = 0
    var ticksSinceCrystalsScanned = 0
    var ticksSinceLastPlayerScan = 0
    var dragonKilled = false
    var previouslyKilled = false
    var dragonUniqueId: UUID? = null
    var scanForLegacyFight = true
    var exitPortalLocation: BlockPos? = null
    var respawnState: CustomDragonSpawnState? = null
        set(state)
        {
            checkNotNull(respawnState) { "Dragon respawn isn't in progress, can't skip ahead in the animation." }
            respawnStateTicks = 0
            if (state === CustomDragonSpawnState.END)
            {
                field = null
                dragonKilled = false
                val dragon = createNewDragon()
                for (serverplayerentity in bossInfo.players)
                {
                    CriteriaTriggers.SUMMONED_ENTITY.trigger(serverplayerentity, dragon)
                }
            } else
            {
                field = state
            }
        }

    var respawnStateTicks = 0
    var crystals: List<EnderCrystalEntity>? = null
    fun write(): CompoundNBT
    {
        val compoundnbt = CompoundNBT()
        if (dragonUniqueId != null)
        {
            compoundnbt.putUniqueId("DragonUUID", dragonUniqueId)
        }
        compoundnbt.putBoolean("DragonKilled", dragonKilled)
        compoundnbt.putBoolean("PreviouslyKilled", previouslyKilled)
        compoundnbt.putBoolean("LegacyScanPerformed", !scanForLegacyFight) // Forge: fix MC-105080
        if (exitPortalLocation != null)
        {
            compoundnbt.put("ExitPortalLocation", NBTUtil.writeBlockPos(exitPortalLocation))
        }
        val listnbt = ListNBT()
        for (i in gateways)
        {
            listnbt.add(IntNBT.valueOf(i))
        }
        compoundnbt.put("Gateways", listnbt)
        return compoundnbt
    }

    open fun tick()
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
            val flag = isFightAreaLoaded
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
                respawnState!!.process(world, this, crystals!!, respawnStateTicks++, exitPortalLocation!!)
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

    fun scanForLegacyFight()
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
            LOGGER.info(
                "Found that there's a dragon still alive ({})",
                enderdragonentity
            )
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

    fun findOrCreateDragon()
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

    fun exitPortalExists(): Boolean
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
                        return true
                    }
                }
            }
        }
        return false
    }

    fun findExitPortal(): PatternHelper?
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
                        val `blockpattern$patternhelper` = portalPattern.match(world, tileentity.getPos())
                        if (`blockpattern$patternhelper` != null)
                        {
                            val blockpos = `blockpattern$patternhelper`.translateOffset(3, 3, 3).pos
                            if (exitPortalLocation == null && blockpos.x == 0 && blockpos.z == 0)
                            {
                                exitPortalLocation = blockpos
                            }
                            return `blockpattern$patternhelper`
                        }
                    }
                }
            }
        }
        val k = world.getHeight(Heightmap.Type.MOTION_BLOCKING, EndPodiumFeature.END_PODIUM_LOCATION).y
        for (l in k downTo 0)
        {
            val `blockpattern$patternhelper1` = portalPattern.match(
                world,
                BlockPos(EndPodiumFeature.END_PODIUM_LOCATION.x, l, EndPodiumFeature.END_PODIUM_LOCATION.z)
            )
            if (`blockpattern$patternhelper1` != null)
            {
                if (exitPortalLocation == null)
                {
                    exitPortalLocation = `blockpattern$patternhelper1`.translateOffset(3, 3, 3).pos
                }
                return `blockpattern$patternhelper1`
            }
        }
        return null
    }

    val isFightAreaLoaded: Boolean
        get()
        {
            for (i in -8..8)
            {
                for (j in 8..8)
                {
                    val ichunk =
                        world.getChunk(i, j, ChunkStatus.FULL, false) as? Chunk ?: return false
                    val `chunkholder$locationtype` = ichunk.locationType
                    if (!`chunkholder$locationtype`.isAtLeast(LocationType.TICKING))
                    {
                        return false
                    }
                }
            }
            return true
        }

    fun updatePlayers()
    {
        val set: MutableSet<ServerPlayerEntity> = Sets.newHashSet()
        for (serverplayerentity in world.getPlayers(VALID_PLAYER))
        {
            bossInfo.addPlayer(serverplayerentity)
            set.add(serverplayerentity)
        }
        val set1: MutableSet<ServerPlayerEntity> = Sets.newHashSet(bossInfo.players)
        set1.removeAll(set)
        for (serverplayerentity1 in set1)
        {
            bossInfo.removePlayer(serverplayerentity1)
        }
    }

    fun findAliveCrystals()
    {
        ticksSinceCrystalsScanned = 0
        numAliveCrystals = 0
        for (`endspikefeature$endspike` in EndSpikeFeature.generateSpikes(world))
        {
            numAliveCrystals += world.getEntitiesWithinAABB(
                EnderCrystalEntity::class.java,
                `endspikefeature$endspike`.topBoundingBox
            ).size
        }
        LOGGER.debug("Found {} end crystals still alive", numAliveCrystals)
    }

    fun processDragonDeath(dragon: AbstractEnderDragonEntity)
    {
        if (dragon.uniqueID == dragonUniqueId)
        {
            bossInfo.percent = 0.0f
            bossInfo.isVisible = false
            generatePortal(true)
            spawnNewGateway()
            if (!previouslyKilled)
            {
                world.setBlockState(
                    world.getHeight(
                        Heightmap.Type.MOTION_BLOCKING,
                        EndPodiumFeature.END_PODIUM_LOCATION
                    ), Blocks.DRAGON_EGG.defaultState
                )
            }
            previouslyKilled = true
            dragonKilled = true
        }
    }

    fun spawnNewGateway()
    {
        if (!gateways.isEmpty())
        {
            val i = gateways.removeAt(gateways.size - 1)
            val j = MathHelper.floor(96.0 * Math.cos(2.0 * (-Math.PI + 0.15707963267948966 * i.toDouble())))
            val k = MathHelper.floor(96.0 * Math.sin(2.0 * (-Math.PI + 0.15707963267948966 * i.toDouble())))
            generateGateway(BlockPos(j, 75, k))
        }
    }

    fun generateGateway(pos: BlockPos?)
    {
        world.playEvent(3000, pos, 0)
        Feature.END_GATEWAY.withConfiguration(EndGatewayConfig.func_214698_a()).place(
            world, world.chunkProvider.chunkGenerator, Random(), pos
        )
    }

    fun generatePortal(active: Boolean)
    {
        val endpodiumfeature = EndPodiumFeature(active)
        if (exitPortalLocation == null)
        {
            exitPortalLocation =
                world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.END_PODIUM_LOCATION).down()
            while (world.getBlockState(exitPortalLocation).block === Blocks.BEDROCK && exitPortalLocation!!.y > world.seaLevel)
            {
                exitPortalLocation = exitPortalLocation!!.down()
            }
        }
        endpodiumfeature.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).place(
            world, world.chunkProvider.chunkGenerator, Random(),
            exitPortalLocation
        )
    }

    open fun createNewDragon(): AbstractEnderDragonEntity?
    {
        world.getChunkAt(BlockPos(0, 128, 0))
        val enderdragonentity = StageManager.stageEnum.dragonType.create(world)
        enderdragonentity!!.phaseManager.setPhase(StageManager.stageEnum.holdingPhase)
        enderdragonentity.setLocationAndAngles(0.0, 128.0, 0.0, world.rand.nextFloat() * 360.0f, 0.0f)
        world.addEntity(enderdragonentity)
        dragonUniqueId = enderdragonentity.uniqueID
        return enderdragonentity
    }

    fun dragonUpdate(dragonIn: AbstractEnderDragonEntity)
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

    fun onCrystalDestroyed(crystal: EnderCrystalEntity, dmgSrc: DamageSource)
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
            val entity = world.getEntityByUuid(dragonUniqueId)
            if (entity is AbstractEnderDragonEntity)
            {
                entity.onCrystalDestroyed(crystal, BlockPos(crystal), dmgSrc)
            }
        }
    }

    fun hasPreviouslyKilledDragon(): Boolean = previouslyKilled

    fun tryRespawnDragon()
    {
        if (dragonKilled && respawnState == null)
        {
            var blockpos = exitPortalLocation
            if (blockpos == null)
            {
                LOGGER.debug("Tried to respawn, but need to find the portal first.")
                val `blockpattern$patternhelper` = findExitPortal()
                if (`blockpattern$patternhelper` == null)
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
                if (list.isEmpty()) return
                list1.addAll(list)
            }
            LOGGER.debug("Found all crystals, respawning dragon.")
            respawnDragon(list1)
        }
    }

    fun respawnDragon(crystalsIn: List<EnderCrystalEntity>?)
    {
        if (dragonKilled && respawnState == null)
        {
            var `blockpattern$patternhelper` = findExitPortal()
            while (`blockpattern$patternhelper` != null)
            {
                for (i in 0 until portalPattern.palmLength)
                {
                    for (j in 0 until portalPattern.thumbLength)
                    {
                        for (k in 0 until portalPattern.fingerLength)
                        {
                            val cachedblockinfo = `blockpattern$patternhelper`.translateOffset(i, j, k)
                            if (cachedblockinfo.blockState.block === Blocks.BEDROCK || cachedblockinfo.blockState.block === Blocks.END_PORTAL)
                            {
                                world.setBlockState(cachedblockinfo.pos, Blocks.END_STONE.defaultState)
                            }
                        }
                    }
                }
                `blockpattern$patternhelper` = findExitPortal()
            }
            respawnState = CustomDragonSpawnState.START
            respawnStateTicks = 0
            generatePortal(false)
            crystals = crystalsIn
        }
    }

    fun resetSpikeCrystals()
    {
        for (`endspikefeature$endspike` in EndSpikeFeature.generateSpikes(world))
        {
            for (endercrystalentity in world.getEntitiesWithinAABB(
                EnderCrystalEntity::class.java, `endspikefeature$endspike`.topBoundingBox
            ))
            {
                endercrystalentity.isInvulnerable = false
                endercrystalentity.beamTarget = null
            }
        }
    }

    fun addPlayer(player: ServerPlayerEntity) = bossInfo.addPlayer(player)

    fun removePlayer(player: ServerPlayerEntity) = bossInfo.removePlayer(player)

    companion object
    {
        var LOGGER = LogManager.getLogger()
        var VALID_PLAYER = EntityPredicates.IS_ALIVE.and(EntityPredicates.withinRange(0.0, 128.0, 0.0, 192.0))
    }

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
        } else
        {
            dragonKilled = true
            previouslyKilled = true
        }
        if (compound.contains("Gateways", 9))
        {
            val listnbt = compound.getList("Gateways", 3)
            for (i in listnbt.indices)
            {
                gateways.add(listnbt.getInt(i))
            }
        } else
        {
            gateways.addAll(ContiguousSet.create(Range.closedOpen(0, 20), DiscreteDomain.integers()))
            gateways.shuffle(Random(dim.seed))
        }
        portalPattern = BlockPatternBuilder.start()
            .aisle("       ", "       ", "       ", "   #   ", "       ", "       ", "       ")
            .aisle("       ", "       ", "       ", "   #   ", "       ", "       ", "       ")
            .aisle("       ", "       ", "       ", "   #   ", "       ", "       ", "       ")
            .aisle("  ###  ", " #   # ", "#     #", "#  #  #", "#     #", " #   # ", "  ###  ")
            .aisle("       ", "  ###  ", " ##### ", " ##### ", " ##### ", "  ###  ", "       ").where(
                '#', CachedBlockInfo.hasState(
                    BlockMatcher.forBlock(
                        Blocks.BEDROCK
                    )
                )
            ).build()
    }
}
