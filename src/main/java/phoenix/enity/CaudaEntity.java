package phoenix.enity;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.controller.BodyController;
import net.minecraft.entity.monster.PhantomEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import phoenix.enity.tasks.OrbitPointGoal;
import phoenix.utils.entity.AbstractFlyingEntity;
import phoenix.utils.entity.ThreeDimensionsLookHelperController;
import phoenix.utils.entity.ThreeDimensionsMoveHelperController;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CaudaEntity extends AbstractFlyingEntity
{
    private static final DataParameter<Integer> SIZE = EntityDataManager.createKey(PhantomEntity.class, DataSerializers.VARINT);

    public CaudaEntity(EntityType<CaudaEntity> type, World worldIn)
    {
        super(type, worldIn);
        this.moveController = new ThreeDimensionsMoveHelperController(this);
        this.lookController = new ThreeDimensionsLookHelperController(this);
    }

    @Override
    protected void registerGoals()
    {
        //this.goalSelector.addGoal  (1, new PickAttackGoal(this));
        //this.goalSelector.addGoal  (2, new SweepAttackGoal(this));
        this.goalSelector.addGoal  (1, new OrbitPointGoal(this));
        //this.goalSelector.addGoal  (2, new OrbitPointGoal(this));
        //this.targetSelector.addGoal(1, new AttackPlayerGoal(this));
    }

    @Override
    protected void registerData()
    {
        super.registerData();
        this.dataManager.register(SIZE, 0);
    }

    public void setCaudaSize(int sizeIn)
    {
        this.dataManager.set(SIZE, MathHelper.clamp(sizeIn, 0, 64));
    }

    public int getCaudaSize()
    {
        return this.dataManager.get(SIZE);
    }

    @Override
    public boolean canSpawn(IWorld worldIn, SpawnReason spawnReasonIn)
    {
        return this.getPosition().getY() < 10 && this.getPosition().getY() > 80 && super.canSpawn(worldIn, spawnReasonIn);
    }

    @Override
    protected float getStandingEyeHeight(@Nonnull Pose poseIn, EntitySize sizeIn)
    {
        return sizeIn.height * 0.35F;
    }

    @Nonnull
    @Override
    protected BodyController createBodyController()
    {
        return new BodyHelperController(this);
    }

    @Override
    public boolean canAttack(@Nonnull EntityType<?> typeIn)
    {
        return true;
    }

    static class BodyHelperController extends BodyController
    {
        MobEntity entity;

        public BodyHelperController(MobEntity mob)
        {
            super(mob);
            entity = mob;
        }

        public void updateRenderAngles()
        {
            entity.rotationYawHead = entity.renderYawOffset;
            entity.renderYawOffset = entity.rotationYaw;
        }
    }

    @Override
    public void tick()
    {
        super.tick();
        if (this.world.isRemote)
        {
            float current = MathHelper.cos((float) (this.getEntityId() * 3 + this.ticksExisted) * 0.13F + (float) Math.PI);
            float next = MathHelper.cos((float) (this.getEntityId() * 3 + this.ticksExisted + 1) * 0.13F + (float) Math.PI);
            if (current > 0.0F && next <= 0.0F)
            {
                this.world.playSound(this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_PHANTOM_FLAP, this.getSoundCategory(), 0.95F + this.rand.nextFloat() * 0.05F, 0.95F + this.rand.nextFloat() * 0.05F, false);
            }
        }
        else
        {
            if(orbitPosition == null || orbitPosition == BlockPos.ZERO)
            {
                orbitPosition = getPosition();
            }
            moveController.setMoveTo(orbitPosition.getX(), orbitPosition.getY(), orbitPosition.getZ(), 10);
        }
    }

    @Nonnull
    @Override
    public EntitySize getSize(@Nonnull Pose poseIn)
    {
        EntitySize entitysize = super.getSize(poseIn);
        return entitysize.scale((entitysize.width + 0.2F * (float) this.getCaudaSize()) / entitysize.width);
    }
}
