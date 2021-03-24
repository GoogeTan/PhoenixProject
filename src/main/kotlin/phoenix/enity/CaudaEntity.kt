package phoenix.enity

import net.minecraft.entity.*
import net.minecraft.entity.ai.controller.BodyController
import net.minecraft.entity.monster.PhantomEntity
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.util.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.IWorld
import net.minecraft.world.World
import phoenix.enity.tasks.OrbitPointGoal
import phoenix.utils.entity.AbstractFlyingEntity
import phoenix.utils.entity.ThreeDimensionsLookHelperController
import phoenix.utils.entity.ThreeDimensionsMoveHelperController
import javax.annotation.Nonnull

class CaudaEntity(type: EntityType<CaudaEntity>, worldIn: World) :
    AbstractFlyingEntity(type, worldIn)
{
    override fun registerGoals()
    {
        //this.goalSelector.addGoal  (1, new PickAttackGoal(this));
        //this.goalSelector.addGoal  (2, new SweepAttackGoal(this));
        //goalSelector.addGoal(1, OrbitPointGoal(this))
        //this.goalSelector.addGoal  (2, new OrbitPointGoal(this));
        //this.targetSelector.addGoal(1, new AttackPlayerGoal(this));
    }

    override fun registerData()
    {
        super.registerData()
        dataManager.register(SIZE, 0)
    }

    var caudaSize: Int
        get()  = dataManager.get(SIZE)
        set(sizeIn) = dataManager.set(SIZE, MathHelper.clamp(sizeIn, 0, 64))

    override fun canSpawn(worldIn: IWorld, spawnReasonIn: SpawnReason): Boolean = position.y in 81 downTo 9 && super.canSpawn(worldIn, spawnReasonIn)

    override fun getStandingEyeHeight(@Nonnull poseIn: Pose, sizeIn: EntitySize): Float = sizeIn.height * 0.35f

    @Nonnull
    override fun createBodyController(): BodyController = BodyHelperController(this)

    override fun canAttack(@Nonnull typeIn: EntityType<*>) = true

    internal class BodyHelperController(var entity: MobEntity) : BodyController(entity)
    {
        override fun updateRenderAngles()
        {
            entity.rotationYawHead = entity.renderYawOffset
            entity.renderYawOffset = entity.rotationYaw
        }
    }
    /*
    override fun tick()
    {
        super.tick()
        if (world.isRemote)
        {
            val current = MathHelper.cos((entityId * 3 + ticksExisted).toFloat() * 0.13f + Math.PI.toFloat())
            val next = MathHelper.cos((entityId * 3 + ticksExisted + 1).toFloat() * 0.13f + Math.PI.toFloat())
            if (current > 0.0f && next <= 0.0f)
            {
                world.playSound(posX, posY, posZ, SoundEvents.ENTITY_PHANTOM_FLAP, this.soundCategory, 0.95f + rand.nextFloat() * 0.05f, 0.95f + rand.nextFloat() * 0.05f, false)
            }
        } else
        {
            if (orbitPosition == null || orbitPosition === BlockPos.ZERO)
            {
                orbitPosition = position
            }
            moveController.setMoveTo(orbitPosition.x.toDouble(), orbitPosition.y.toDouble(), orbitPosition.z.toDouble(), 10.0)
        }
    }

    init
    {
        moveController = ThreeDimensionsMoveHelperController(this)
        lookController = ThreeDimensionsLookHelperController(this)
    }
    */

    @Nonnull
    override fun getSize(@Nonnull poseIn: Pose): EntitySize = super.getSize(poseIn).scale((super.getSize(poseIn).width + 0.2f * caudaSize.toFloat()) / super.getSize(poseIn).width)

    companion object
    {
        private val SIZE = EntityDataManager.createKey(PhantomEntity::class.java, DataSerializers.VARINT)
    }
}
