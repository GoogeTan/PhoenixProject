package phoenix.enity.tasks;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import phoenix.init.PhoenixBlocks;
import phoenix.utils.entity.AbstractFlyingEntity;
import phoenix.utils.entity.AttackPhases;
import phoenix.utils.entity.ThreeDimensionsMovingGoal;

public class OrbitPoint2Goal extends ThreeDimensionsMovingGoal
{
    double distance = 10;
    private float speed = -1;
    private float posX;
    private float posY;
    private float posZ;
    AbstractFlyingEntity entity;
    public OrbitPoint2Goal(AbstractFlyingEntity entityIn)
    {
        super(entityIn);
        entity = entityIn;
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean shouldExecute()
    {
        return entity.getAttackTarget() == null || entity.attackPhase == AttackPhases.CIRCLE;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        if(speed == -1)
            speed = entity.getRNG().nextFloat() * 2.0F * (float) Math.PI;

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
                posX = current.getX();
                posY = current.getY();
                posZ = current.getZ();
                updateOffset();
            }
        }
    }

    private void updateOffset()
    {
        if (BlockPos.ZERO.equals(entity.orbitPosition))
        {
            entity.orbitPosition = new BlockPos(entity);
        }

        this.speed += this.posZ * 15.0F * ((float) Math.PI / 180F);
        entity.orbitOffset = (new Vec3d(entity.orbitPosition)).add(this.posX * MathHelper.cos(this.speed), -4.0F + this.posY, this.posX * MathHelper.sin(this.speed));
    }
}
