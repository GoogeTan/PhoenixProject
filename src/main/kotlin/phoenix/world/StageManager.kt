package phoenix.world

import net.minecraft.block.Blocks
import net.minecraft.block.PaneBlock
import net.minecraft.client.audio.MusicTicker
import net.minecraft.entity.EntityType
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.IWorld
import phoenix.enity.boss.AbstractEnderDragonEntity
import phoenix.enity.boss.phase.PhaseType
import phoenix.init.PhxBlocks.armoredGlass
import phoenix.init.PhxEntities
import phoenix.utils.PhoenixMusicTracks
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

    fun setStage(stage: Int, provider: NewEndBiomeProvider)
    {
        data.putInt("stage", stage)
        provider.initBiomeLayer()
    }

    fun addStage()
    {
        stage = min(stage + 1, 3)
    }

    fun addStage(provider: NewEndBiomeProvider) = setStage(min(stage + 1, 3), provider)

    fun addPart()
    {
        part++
        if (data.getInt("part") >= 3)
        {
            addStage()
            part = 0
        }
    }

    fun addPart(provider: NewEndBiomeProvider)
    {
        part++
        if (data.getInt("part") >= 3)
        {
            addStage(provider)
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
                    for (k in -2..2)
                    {
                        for (l in -2..2)
                        {
                            for (i1 in 0..3)
                            {
                                val isRight = MathHelper.abs(k) == 2
                                val ifLeft = MathHelper.abs(l) == 2
                                val isTop = i1 == 3
                                if (isRight || ifLeft || isTop)
                                {
                                    val isNorth = k == -2 || k == 2 || isTop
                                    val flag4 = l == -2 || l == 2 || isTop
                                    val blockstate = Blocks.IRON_BARS.defaultState
                                        .with(PaneBlock.NORTH, isNorth && l != -2)
                                        .with(PaneBlock.SOUTH, isNorth && l != 2)
                                        .with(PaneBlock.WEST, flag4 && k != -2)
                                        .with(PaneBlock.EAST, flag4 && k != 2)
                                    future.setBlockState(
                                        world,
                                        pos.setPos(spike.centerX + k, spike.height + i1, spike.centerZ + l),
                                        blockstate
                                    )
                                }
                            }
                        }
                    }
                }


            }

            override val music: MusicTicker.MusicType = MusicTicker.MusicType.END

            override val holdingPhase: PhaseType = PhaseType.ASH_HOLDING_PATTERN
            override val hoverPhase: PhaseType = PhaseType.ASH_HOVER

            override val dragonType: EntityType<out AbstractEnderDragonEntity> = PhxEntities.DRAGON_ASH_STAGE
        },
        REDO
        {
            override fun createTower(future: CustomEndSpike, world: IWorld, spike: CustomEndSpike.EndSpike)
            {
                val pos = BlockPos.Mutable()
                for (k in -2..2)
                {
                    for (l in -2..2)
                    {
                        for (i1 in 0..3)
                        {
                            val isRight = MathHelper.abs(k) == 2
                            val ifLeft = MathHelper.abs(l) == 2
                            val isTop = i1 == 3
                            if (isRight || ifLeft || isTop)
                            {
                                val blockstate = armoredGlass.defaultState
                                future.setBlockState(
                                    world,
                                    pos.setPos(spike.centerX + k, spike.height + i1, spike.centerZ + l),
                                    blockstate
                                )
                            }
                        }
                    }
                }
            }
            override val music: MusicTicker.MusicType = PhoenixMusicTracks.REDO_MUSIC

            override val holdingPhase: PhaseType = PhaseType.REDO_HOLDING_PATTERN
            override val hoverPhase: PhaseType = PhaseType.REDO_HOVER

            override val dragonType: EntityType<out AbstractEnderDragonEntity> = PhxEntities.DRAGON_REDO_STAGE
        },
        REBIRTH
        {
            override fun createTower(future: CustomEndSpike, world: IWorld, spike: CustomEndSpike.EndSpike) = REDO.createTower(future, world, spike)

            override val music: MusicTicker.MusicType = REDO.music

            override val holdingPhase: PhaseType = REDO.holdingPhase
            override val hoverPhase: PhaseType = REDO.hoverPhase

            override val dragonType: EntityType<out AbstractEnderDragonEntity> = REDO.dragonType
        },
        AIR
        {
            override fun createTower(future: CustomEndSpike, world: IWorld, spike: CustomEndSpike.EndSpike) = ASH.createTower(future, world, spike)

            override val music: MusicTicker.MusicType = REBIRTH.music

            override val holdingPhase: PhaseType = REBIRTH.holdingPhase
            override val hoverPhase: PhaseType = REBIRTH.hoverPhase

            override val dragonType: EntityType<out AbstractEnderDragonEntity> = REBIRTH.dragonType
        };

        abstract fun createTower(future: CustomEndSpike, world: IWorld, spike: CustomEndSpike.EndSpike)

        abstract val music : MusicTicker.MusicType

        abstract val holdingPhase : PhaseType
        abstract val hoverPhase : PhaseType

        abstract val dragonType : EntityType<out AbstractEnderDragonEntity>
    }
}
