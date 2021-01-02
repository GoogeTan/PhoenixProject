package phoenix.enity

import net.minecraft.entity.*
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.item.ItemEntity
import net.minecraft.entity.monster.AbstractRaiderEntity
import net.minecraft.entity.passive.AnimalEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Items
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.DamageSource
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld
import net.minecraft.world.World
import phoenix.init.PhoenixEntities


class TalpaEntity(type: EntityType<TalpaEntity>, worldIn: World) : AnimalEntity(type, worldIn)
{
    val boundOrigin: BlockPos? = null

    override fun tick()
    {
        setNoGravity(isEntityInsideOpaqueBlock)
        super.tick()
    }

    override fun registerAttributes()
    {
        super.registerAttributes()
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).baseValue = 14.0
    }

    override fun isInvulnerableTo(source: DamageSource) = super.isInvulnerableTo(source) || source === DamageSource.IN_WALL
    override fun getMaxFallHeight() = 7
    override fun getSize(poseIn: Pose) = EntitySize(0.6f, 0.6f, false)
    override fun getCollisionBox(entityIn: Entity): AxisAlignedBB? = if (isEntityInsideOpaqueBlock) null else super.getCollisionBox(entityIn)
    override fun createChild(ageable: AgeableEntity): AgeableEntity = TalpaEntity(PhoenixEntities.TALPA.get(), ageable.world)
    override fun canSpawn(worldIn: IWorld, spawnReasonIn: SpawnReason) = this.position.y in 11..49 && super.canSpawn(worldIn, spawnReasonIn)

    override fun registerGoals()
    {
        goalSelector.addGoal(0, SwimGoal(this))
        goalSelector.addGoal(1, PanicGoal(this, 0.25))
        goalSelector.addGoal(2, BreedGoal(this, 0.5))
        goalSelector.addGoal(3, TemptGoal(this, 0.5, Ingredient.fromItems(Items.WHEAT), false))
        goalSelector.addGoal(4, FollowParentGoal(this, 0.5))
        goalSelector.addGoal(9, LookAtGoal(this, PlayerEntity::class.java, 3.0f, 1.0f))
        goalSelector.addGoal(10, LookAtGoal(this, MobEntity::class.java, 8.0f))
        targetSelector.addGoal(5, HurtByTargetGoal(this, AbstractRaiderEntity::class.java).setCallsForHelp())
        targetSelector.addGoal(6, NearestAttackableTargetGoal(this, PlayerEntity::class.java, true))
    }


}
