package phoenix.world

import net.minecraft.block.Blocks
import net.minecraft.block.PaneBlock
import net.minecraft.client.audio.MusicTicker
import net.minecraft.entity.EntityType
import net.minecraft.entity.boss.dragon.EnderDragonEntity
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.IWorld
import net.minecraftforge.common.MinecraftForge
import phoenix.init.PhoenixMusicTracks
import phoenix.init.PhxBlocks.armoredGlass
import phoenix.other.client
import phoenix.other.server
import phoenix.utils.*
import phoenix.world.structures.CustomEndSpike
import kotlin.math.min

object StageManager
{
    private var data: CompoundNBT = CompoundNBT()

    fun read(nbt: CompoundNBT)
    {
        if (nbt.contains("stage_nbt"))
        {
            data = nbt.getCompound("stage_nbt")
        }
        else
        {
            val compound = CompoundNBT()
            compound.putInt("stage", 0)
            compound.putInt("part", 0)
            data = compound
            nbt.put("stage_nbt", compound)
        }
    }

    fun write(compound: CompoundNBT): CompoundNBT
    {
        compound.put("stage_nbt", data)
        return compound
    }

    var stage: Int
        get() = data.getInt("stage")
        set(stage) = data.putInt("stage", stage)
    val stageEnum: Stage
        get() = Stage.values()[data.getInt("stage") % Stage.values().size]
    var part: Int
        get() =  data.getInt("part")
        set(part) = data.putInt("part", part)

    private fun setStage(stage: Int, provider: NewEndBiomeProvider, postEvent : Boolean = true)
    {
        this.stage = stage
        provider.initBiomeLayer()

        if (postEvent)
        {
            client { _, _, world ->
                if (world != null)
                    MinecraftForge.EVENT_BUS.post(ClientChangeStageEvent(world))
            }

            server { server ->
                if (server != null)
                    for (i in server.worlds)
                        MinecraftForge.EVENT_BUS.post(ServerChangeStageEvent(i))
            }
        }
    }

    private fun addStage(provider: NewEndBiomeProvider, postEvent : Boolean = false) = setStage(min(stage + 1, 3), provider, postEvent)

    fun addPart(provider: NewEndBiomeProvider)
    {
        part++
        if (data.getInt("part") >= 3)
        {
            addStage(provider)
            client { _, _, world ->
                if (world != null)
                    MinecraftForge.EVENT_BUS.post(ClientStageUppedEvent(world))
            }

            server { server ->
                if (server != null)
                    for (world in server.worlds)
                        MinecraftForge.EVENT_BUS.post(ServerStageUppedEvent(world))
            }
            part = 0
        }
    }

    enum class Stage
    {
        ASH
        {
            override fun createTower(future: CustomEndSpike, world: IWorld, spike: CustomEndSpike.EndSpike)
            {
                if (spike.isGuarded)
                {
                    val pos = BlockPos.Mutable()
                    for (x in -2..2)
                    {
                        for (z in -2..2)
                        {
                            for (y in 0..3)
                            {
                                val isRight = MathHelper.abs(x) == 2
                                val ifLeft = MathHelper.abs(z) == 2
                                val isTop = y == 3
                                if (isRight || ifLeft || isTop)
                                {
                                    val isNorth = x == -2 || x == 2 || isTop
                                    val flag4 = z == -2 || z == 2 || isTop
                                    val blockstate = Blocks.IRON_BARS.defaultState
                                        .with(PaneBlock.NORTH, isNorth && z != -2)
                                        .with(PaneBlock.SOUTH, isNorth && z != 2)
                                        .with(PaneBlock.WEST, flag4 && x != -2)
                                        .with(PaneBlock.EAST, flag4 && x != 2)
                                    future.setBlockState(
                                        world,
                                        pos.setPos(spike.centerX + x, spike.height + y, spike.centerZ + z),
                                        blockstate
                                    )
                                }
                            }
                        }
                    }
                }
            }

            override val music: MusicTicker.MusicType = MusicTicker.MusicType.END

            override val dragonType: EntityType<out EnderDragonEntity> = EntityType.ENDER_DRAGON
        },
        REDO
        {
            override fun createTower(future: CustomEndSpike, world: IWorld, spike: CustomEndSpike.EndSpike)
            {
                val pos = BlockPos.Mutable()
                for (x in -2..2)
                {
                    for (z in -2..2)
                    {
                        for (y in 0..3)
                        {
                            val isRight = MathHelper.abs(x) == 2
                            val ifLeft = MathHelper.abs(z) == 2
                            val isTop = y == 3
                            if (isRight || ifLeft || isTop)
                            {
                                future.setBlockState(world, pos.setPos(spike.centerX + x, spike.height + y, spike.centerZ + z), armoredGlass.defaultState)
                            }
                        }
                    }
                }
            }
            override val music: MusicTicker.MusicType = PhoenixMusicTracks.REDO_MUSIC
            override val dragonType: EntityType<out EnderDragonEntity> = ASH.dragonType
        },
        REBIRTH
        {
            override fun createTower(future: CustomEndSpike, world: IWorld, spike: CustomEndSpike.EndSpike) = REDO.createTower(future, world, spike)
            override val music: MusicTicker.MusicType = REDO.music
            override val dragonType: EntityType<out EnderDragonEntity> = REDO.dragonType
        },
        AIR
        {
            override fun createTower(future: CustomEndSpike, world: IWorld, spike: CustomEndSpike.EndSpike) = ASH.createTower(future, world, spike)
            override val music: MusicTicker.MusicType = REBIRTH.music
            override val dragonType: EntityType<out EnderDragonEntity> = REBIRTH.dragonType
        };

        abstract fun createTower(future: CustomEndSpike, world: IWorld, spike: CustomEndSpike.EndSpike)

        abstract val music : MusicTicker.MusicType

        abstract val dragonType : EntityType<out EnderDragonEntity>
    }
}
