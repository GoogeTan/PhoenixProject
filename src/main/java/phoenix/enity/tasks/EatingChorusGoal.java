package phoenix.enity.tasks;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import phoenix.init.PhoenixBlocks;
import phoenix.utils.entity.AbstractFlyingEntity;
import phoenix.utils.entity.ThreeDimensionsMovingGoal;
import phoenix.world.BlockPosUtils;

public class EatingChorusGoal extends ThreeDimensionsMovingGoal
{
    double distance = 10;
    public EatingChorusGoal(AbstractFlyingEntity entityIn)
    {
        super(entityIn);
    }

    @Override
    public boolean shouldExecute()
    {
        for(BlockPos current : BlockPos.getAllInBoxMutable
                (
                        MathHelper.floor(entity.getPosX() - distance),
                        MathHelper.floor(entity.getPosY() - distance),
                        MathHelper.floor(entity.getPosZ() - distance),

                        MathHelper.floor(entity.getPosX() + distance),
                        MathHelper.floor(entity.getPosY() + distance),
                        MathHelper.floor(entity.getPosZ() + distance)
                ))
        {
            if (entity.world.getBlockState(current).getBlock() == Blocks.CHORUS_FLOWER || entity.world.getBlockState(current).getBlock() == PhoenixBlocks.FERTILE_END_STONE.get())
            {
                entity.orbitPosition = current;
                entity.orbitPosition = new BlockPos(entity);
                entity.orbitOffset = (new Vec3d(entity.orbitPosition)).add(current.getX() * MathHelper.cos(10), current.getY(), current.getZ() * MathHelper.sin(10));
                return true;
            }
        }
        return false;
    }

    @Override
    public void tick()
    {
        super.tick();
        if(BlockPosUtils.distanceTo(entity.orbitPosition, entity.getPosition()) <= 1)
        {
            entity.world.setBlockState(entity.orbitPosition, Blocks.AIR.getDefaultState());
        }
    }
}
