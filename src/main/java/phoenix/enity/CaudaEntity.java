package phoenix.enity;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.World;
import phoenix.init.PhoenixEntities;

import javax.annotation.Nullable;

public class CaudaEntity extends AnimalEntity implements EntityType.IFactory<CaudaEntity>
{
    public CaudaEntity(EntityType<CaudaEntity> type, World worldIn)
    {
        super(type, worldIn);
    }
    public CaudaEntity(World worldIn)
    {
        super(PhoenixEntities.CAUDA.get(), worldIn);
    }

    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageable)
    {
        return new CaudaEntity(ageable.getEntityWorld());
    }

    @Override
    public CaudaEntity create(EntityType<CaudaEntity> p_create_1_, World p_create_2_)
    {
        return null;
    }
}
