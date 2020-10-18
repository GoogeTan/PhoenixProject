package phoenix.enity;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import phoenix.init.PhoenixEntities;

public class TalpaEntity extends AnimalEntity
{
    private BlockPos boundOrigin;
    public TalpaEntity(EntityType<TalpaEntity> type, World worldIn)
    {
        super(type, worldIn);
    }

    public static TalpaEntity create(World worldIn)
    {
        return new TalpaEntity(PhoenixEntities.TALPA.get(), worldIn);
    }

    @Override
    public void tick()
    {
        this.setNoGravity(isEntityInsideOpaqueBlock());
        super.tick();
    }
    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(14.0D);
    }
    @Override
    public boolean isInvulnerableTo(   DamageSource source)
    {
        return super.isInvulnerableTo(source) || source == DamageSource.IN_WALL;
    }

    @Override
    public int getMaxFallHeight()
    {
        return 7;
    }

    public BlockPos getBoundOrigin() {
        return this.boundOrigin;
    }

    @Override
    public EntitySize getSize(   Pose poseIn)
    {
        return new EntitySize(0.6F, 0.6F, false);
    }

      
    @Override
    public AxisAlignedBB getCollisionBox(Entity entityIn)
    {
        return isEntityInsideOpaqueBlock() ? null : super.getCollisionBox(entityIn);
    }

    @Override
    public boolean canSpawn(IWorld worldIn, SpawnReason spawnReasonIn)
    {
        return this.getPosition().getY() > 10 && this.getPosition().getY() < 50 && super.canSpawn(worldIn, spawnReasonIn);
    }

    @Override
    protected void registerGoals()
    {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 0.25D));
        this.goalSelector.addGoal(2, new BreedGoal(this, 0.5D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 0.5D, Ingredient.fromItems(Items.WHEAT), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 0.5D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(5, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setCallsForHelp());
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }
      
    @Override
    public AgeableEntity createChild(AgeableEntity ageable)
    {
        return TalpaEntity.create(ageable.world);
    }
}
