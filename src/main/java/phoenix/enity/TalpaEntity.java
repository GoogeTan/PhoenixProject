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
import net.minecraft.util.math.Vec3d;
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
    public boolean isInvulnerableTo(DamageSource source)
    {
        return super.isInvulnerableTo(source) || source == DamageSource.IN_WALL;
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
/*
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBox(Entity entityIn)
    {
        return isEntityInsideOpaqueBlock() ? null : super.getCollisionBox(entityIn);
    }
*/
    @Override
    protected void registerGoals()
    {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 0.25D));
        this.goalSelector.addGoal(2, new BreedGoal(this, 0.5D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 0.5D, Ingredient.fromItems(Items.WHEAT), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 0.5D));
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
