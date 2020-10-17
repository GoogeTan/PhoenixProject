package phoenix.utils.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class AbstractFlyingEntity  extends FlyingEntity
{
    protected Vec3d orbitOffset = Vec3d.ZERO;
    protected BlockPos orbitPosition = BlockPos.ZERO;
    public AttackPhases attackPhase = AttackPhases.CIRCLE;
    protected AbstractFlyingEntity(EntityType<? extends FlyingEntity> type, World worldIn) {
        super(type, worldIn);
    }
}
