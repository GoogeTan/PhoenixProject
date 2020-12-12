package phoenix.world;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PaneBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import phoenix.init.PhoenixBlocks;
import phoenix.world.structures.CustomEndSpike;

public class StageManager
{
    private static CompoundNBT data = new CompoundNBT();

    public static void read(CompoundNBT nbt)
    {
        if (nbt.contains("stage_nbt"))
        {
            data = nbt.getCompound("stage_nbt");
        }
        else
        {
            CompoundNBT compound = new CompoundNBT();
            compound.putInt("stage", 0);
            compound.putInt("part",  0);
            data = compound;
            nbt.put("stage_nbt", compound);
        }
    }

    public static CompoundNBT write(CompoundNBT compound)
    {
        compound.put("stage_nbt", data);
        return compound;
    }

    public static int getStage()
    {
        if(data == null) data = new CompoundNBT();
        return data.getInt("stage");
    }

    public static Stage getStageEnum()
    {
        if(data == null) data = new CompoundNBT();
        return Stage.values()[data.getInt("stage") % Stage.values().length];
    }

    public static int getPart()
    {
        if(data == null) data = new CompoundNBT();
        return data.getInt("part");
    }

    public static void setStage(int stage)
    {
        data.putInt("stage", stage);
    }

    public static void setStage(int stage, NewEndBiomeProvider provider)
    {
        data.putInt("stage", stage);
        provider.initBiomeLayer();
    }

    public static void setPart(int part)
    {
        data.putInt("part", part);
    }

    public static void addStage()
    {
        setStage(Math.min(getStage() + 1, 3));
    }

    public static void addStage(NewEndBiomeProvider provider)
    {
        setStage(Math.min(getStage() + 1, 3), provider);
    }

    public static void addPart()
    {
        setPart(getPart() + 1);
        if(data.getInt("part") >= 3)
        {
            addStage();
            setPart(0);
        }
    }

    public static void addPart(NewEndBiomeProvider provider)
    {
        setPart(getPart() + 1);
        if(data.getInt("part") >= 3)
        {
            addStage(provider);
            setPart(0);
        }
    }

    public enum Stage
    {
        ASH
            {
                @Override
                public void createTower(CustomEndSpike future, IWorld world, CustomEndSpike.EndSpike spike)
                {
                    if (spike.isGuarded())
                    {
                        BlockPos.Mutable pos = new BlockPos.Mutable();

                        for (int k = -2; k <= 2; ++k)
                        {
                            for (int l = -2; l <= 2; ++l)
                            {
                                for (int i1 = 0; i1 <= 3; ++i1)
                                {
                                    boolean isRight = MathHelper.abs(k) == 2;
                                    boolean ifLeft = MathHelper.abs(l) == 2;
                                    boolean isTop = i1 == 3;
                                    if (isRight || ifLeft || isTop)
                                    {
                                        boolean isNorth = k == -2 || k == 2 || isTop;
                                        boolean flag4 = l == -2 || l == 2 || isTop;
                                        BlockState blockstate = Blocks.IRON_BARS.getDefaultState()
                                                .with(PaneBlock.NORTH, isNorth && l != -2)
                                                .with(PaneBlock.SOUTH, isNorth && l != 2)
                                                .with(PaneBlock.WEST, flag4 && k != -2)
                                                .with(PaneBlock.EAST, flag4 && k != 2);
                                        future.setBlockState(world, pos.setPos(spike.getCenterX() + k, spike.getHeight() + i1, spike.getCenterZ() + l), blockstate);
                                    }
                                }
                            }
                        }
                    }
                }
            }, REDO
            {
                @Override
                public void createTower(CustomEndSpike future, IWorld world, CustomEndSpike.EndSpike spike)
                {
                    BlockPos.Mutable pos = new BlockPos.Mutable();

                    for (int k = -2; k <= 2; ++k)
                    {
                        for (int l = -2; l <= 2; ++l)
                        {
                            for (int i1 = 0; i1 <= 3; ++i1)
                            {
                                boolean isRight = MathHelper.abs(k) == 2;
                                boolean ifLeft = MathHelper.abs(l) == 2;
                                boolean isTop = i1 == 3;
                                if (isRight || ifLeft || isTop)
                                {
                                    BlockState blockstate = PhoenixBlocks.INSTANCE.getARMORED_GLASS().get().getDefaultState();
                                    future.setBlockState(world, pos.setPos(spike.getCenterX() + k, spike.getHeight() + i1, spike.getCenterZ() + l), blockstate);
                                }
                            }
                        }
                    }
                }
            }, REBIRTH
            {
                @Override
                public void createTower(CustomEndSpike future, IWorld world, CustomEndSpike.EndSpike spike)
                {
                    ASH.createTower(future, world, spike);
                }
            },
            AIR
            {
                @Override
                public void createTower(CustomEndSpike future, IWorld world, CustomEndSpike.EndSpike spike)
                {
                    ASH.createTower(future, world, spike);
                }
            };

        public abstract void createTower(CustomEndSpike future, IWorld world, CustomEndSpike.EndSpike spike);
    }
}
