package phoenix.enity.tasks;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
        if(entity.getAttackTarget() != null)
            return false;

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
            if (entity.world.getBlockState(current).getBlock() == Blocks.CHORUS_FLOWER) {
                entity.orbitPosition = current;
                return true;
            }
        }
        return false;
    }

    @Override
    public void startExecuting()
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
            if (entity.world.getBlockState(current).getBlock() == Blocks.CHORUS_FLOWER) {
                entity.orbitPosition = current;
                break;
            }
        }
    }

    @Override
    public void tick()
    {
        super.tick();
        if(BlockPosUtils.distanceTo(entity.orbitPosition, entity.getPosition()) < 1)
        {
            entity.world.setBlockState(entity.orbitPosition, Blocks.AIR.getDefaultState());
        }
    }


}
