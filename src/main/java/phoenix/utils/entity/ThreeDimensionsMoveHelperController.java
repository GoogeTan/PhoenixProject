package phoenix.utils.entity;

import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class ThreeDimensionsMoveHelperController extends MovementController
{
    private float speedFactor = 0.1F;
    AbstractFlyingEntity entity;

    public ThreeDimensionsMoveHelperController(AbstractFlyingEntity entityIn)
    {
        super(entityIn);
        entity = entityIn;
    }

    public void tick()
    {
        if (entity.collidedHorizontally)
        {
            entity.rotationYaw += 180.0F;
            this.speedFactor = 0.1F;
        }

        float realX = (float) (entity.orbitOffset.x - entity.getPosX());
        float realY = (float) (entity.orbitOffset.y - entity.getPosY());
        float realZ = (float) (entity.orbitOffset.z - entity.getPosZ());

        double movingHorizontalVLength = MathHelper.sqrt(realX * realX + realZ * realZ);
        double coefficient = 1.0D - (double) MathHelper.abs(realY * 0.7F) / movingHorizontalVLength;
        realX = (float) ((double) realX * coefficient);
        realZ = (float) ((double) realZ * coefficient);

        movingHorizontalVLength = MathHelper.sqrt(realX * realX + realZ * realZ);
        double movingVLength = MathHelper.sqrt(realX * realX + realZ * realZ + realY * realY);

        float rotationYaw = entity.rotationYaw;
        float rotationYawAngle = (float) MathHelper.atan2(realZ, realX);
        float rotationYaw2 = MathHelper.wrapDegrees(entity.rotationYaw + 90.0F);

        float f6 = MathHelper.wrapDegrees(rotationYawAngle * (180F / (float) Math.PI));
        entity.rotationYaw = MathHelper.approachDegrees(rotationYaw2, f6, 4.0F) - 90.0F;
        entity.renderYawOffset = entity.rotationYaw;
        if (MathHelper.degreesDifferenceAbs(rotationYaw, entity.rotationYaw) < 3.0F)
        {
            this.speedFactor = MathHelper.approach(this.speedFactor, 1.8F, 0.005F * (1.8F / this.speedFactor));
        } else
        {
            this.speedFactor = MathHelper.approach(this.speedFactor, 0.2F, 0.025F);
        }

        float f7 = (float) (-(MathHelper.atan2(-realY, movingHorizontalVLength) * (double) (180F / (float) Math.PI)));
        entity.rotationPitch = f7;
        float f8 = entity.rotationYaw + 90.0F;
        double d3 = (double) (this.speedFactor * MathHelper.cos(f8 * ((float) Math.PI / 180F))) * Math.abs((double) realX / movingVLength);
        double d4 = (double) (this.speedFactor * MathHelper.sin(f8 * ((float) Math.PI / 180F))) * Math.abs((double) realZ / movingVLength);
        double d5 = (double) (this.speedFactor * MathHelper.sin(f7 * ((float) Math.PI / 180F))) * Math.abs((double) realY / movingVLength);
        Vec3d vec3d = entity.getMotion();
        entity.setMotion(vec3d.add((new Vec3d(d3, d5, d4)).subtract(vec3d).scale(0.2D)));
    }
}