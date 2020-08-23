package phoenix.enity;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.World;
import phoenix.init.PhoenixEntities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TalpaEntity extends AnimalEntity implements EntityType.IFactory<TalpaEntity>
{
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
    public boolean isChild()
    {
        return isChild;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.1D, Ingredient.fromItems(Items.WHEAT), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
    }

    @Nonnull
    @Override
    public TalpaEntity create(@Nonnull EntityType<TalpaEntity> type, @Nonnull World world)
    {
        TalpaEntity entity = new TalpaEntity(type, world);
        return entity;
    }


    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageable)
    {
        return new TalpaEntity(ageable.world, true);
    }
}
