package phoenix.enity;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import phoenix.init.PhoenixEntities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TalpaEntity extends AnimalEntity implements EntityType.IFactory<TalpaEntity>
{
    private float randomMotionX;
    private float randomMotionY;
    private float randomMotionZ;
    private boolean isChild;

    public TalpaEntity(EntityType<TalpaEntity> type, World worldIn)
    {
        super(type, worldIn);
        isChild = false;
    }

    public TalpaEntity(World worldIn)
    {
        this(PhoenixEntities.TALPA.get(), worldIn);
        isChild = false;
    }

    public TalpaEntity(World worldIn, boolean isChildIn)
    {
        this(PhoenixEntities.TALPA.get(), worldIn);
        this.isChild = isChildIn;
    }


    @Override
    public void livingTick()
    {
        if (!this.world.isRemote)
        {
            BlockPos pos = getPosition().offset(Direction.values()[rand.nextInt(Direction.values().length)]);
            setPosition(pos.getX(), pos.getY(), pos.getZ());
        }
        if (!this.world.isRemote)
        {
            this.updateLeashedState();
            if (this.ticksExisted % 5 == 0)
            {
                this.updateMovementGoalFlags();
            }
        }
    }

    public boolean isInvulnerableTo(DamageSource source)
    {
        return super.isInvulnerableTo(source) || source == DamageSource.IN_WALL;
    }

    public void setMovementVector(float randomMotionVecXIn, float randomMotionVecYIn, float randomMotionVecZIn)
    {
        this.randomMotionX = randomMotionVecXIn;
        this.randomMotionY = randomMotionVecYIn;
        this.randomMotionZ = randomMotionVecZIn;
    }

    public boolean hasMovementVector()
    {
        return this.randomMotionX != 0.0F || this.randomMotionY != 0.0F || this.randomMotionZ != 0.0F;
    }

    @Override
    public int getMaxFallHeight()
    {
        return 7;
    }


    @Override
    public EntitySize getSize(Pose poseIn)
    {
        return new EntitySize(0.6F, 0.6F, false);
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBox(Entity entityIn)
    {
        return isEntityInsideOpaqueBlock() ? null : super.getCollisionBox(entityIn);
    }

    @Override
    protected void registerGoals()
    {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 0.25D));
        this.goalSelector.addGoal(2, new BreedGoal(this, 0.5D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 0.5D, Ingredient.fromItems(Items.WHEAT), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 0.5D));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 0.5D));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
    }

    @Nonnull
    @Override
    public TalpaEntity create(@Nonnull EntityType<TalpaEntity> type, @Nonnull World world)
    {
        return new TalpaEntity(type, world);
    }

    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageable)
    {
        return new TalpaEntity(ageable.world, true);
    }
}
