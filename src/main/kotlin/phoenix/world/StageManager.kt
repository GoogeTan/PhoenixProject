package phoenix.world

import net.minecraft.block.Blocks
import net.minecraft.block.PaneBlock
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.IWorld
import phoenix.init.PhoenixBlocks.ARMORED_GLASS
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
        } else
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

    fun addStage(provider: NewEndBiomeProvider)
    {
        setStage(min(stage + 1, 3), provider)
    }

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
                                val blockstate = ARMORED_GLASS.defaultState
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
        },
        REBIRTH
        {
            override fun createTower(future: CustomEndSpike, world: IWorld, spike: CustomEndSpike.EndSpike)
            {
                ASH.createTower(future, world, spike)
            }
        },
        AIR
        {
            override fun createTower(future: CustomEndSpike, world: IWorld, spike: CustomEndSpike.EndSpike)
            {
                ASH.createTower(future, world, spike)
            }
        };

        abstract fun createTower(future: CustomEndSpike, world: IWorld, spike: CustomEndSpike.EndSpike)
    }
}
