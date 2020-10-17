package phoenix.enity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.PhantomEntity;
import net.minecraft.world.World;
import phoenix.init.PhoenixEntities;
import phoenix.utils.entity.AbstractFlyingEntity;
import phoenix.utils.entity.OrbitPointGoal;
import phoenix.utils.entity.ThreeDimensionsLookHelperController;
import phoenix.utils.entity.ThreeDimensionsMoveHelperController;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class CaudaEntity extends AbstractFlyingEntity implements EntityType.IFactory<CaudaEntity>
{
    public CaudaEntity(EntityType<CaudaEntity> type, World worldIn)
    {
        super(type, worldIn);
        this.moveController = new ThreeDimensionsMoveHelperController(this);
        this.lookController = new ThreeDimensionsLookHelperController(this);
    }
    public CaudaEntity(World worldIn)
    {
        this(PhoenixEntities.CAUDA.get(), worldIn);
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();
        this.goalSelector.addGoal(0, new OrbitPointGoal(this));
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    @Override
    public CaudaEntity create(EntityType<CaudaEntity> type, World world)
    {
        return new CaudaEntity(type, world);
    }
}
