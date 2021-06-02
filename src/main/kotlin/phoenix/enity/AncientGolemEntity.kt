package phoenix.enity

import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.monster.MonsterEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.SmallFireballEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.util.DamageSource
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import phoenix.init.PhxEntities
import java.util.*

class AncientGolemEntity(type: EntityType<out AncientGolemEntity> = PhxEntities.ancientGolemEntity, worldIn: World) : MonsterEntity(type, worldIn)
{
    var breathState = 0.0f
    var closing = true
    var closed : Boolean
        get() = dataManager[CLOSED]
        private inline set(value)
        {
            dataManager.set(CLOSED, value)
            closedTimer = (closedTimer * 0.7 + 20 * 10).toInt()
        }

    var closedTimer = 0
    var shownHand = false

    override fun livingTick()
    {
        super.livingTick()

        if (closing || closed && breathState in 0.0f..0.25f)
            breathState -= 0.008f
        else if (!closed)
            breathState += 0.008f

        if (breathState < 0.0 || breathState > 0.25f)
            closing = !closing

        if (!world.isRemote)
        {
            if (closedTimer > 0)
                closedTimer--
            if (closedTimer <= 0)
                closed = false
        }

        shownHand = attackTarget != null && BlockPos(this).distanceSq(BlockPos(attackTarget)) < 5.0
    }

    override fun onAddedToWorld()
    {
        super.onAddedToWorld()
        val stack = ItemStack(Items.DIAMOND_SWORD)
        stack.addEnchantment(Enchantments.SHARPNESS, 3)
        stack.addEnchantment(Enchantments.FIRE_ASPECT, 1)
        stack.addEnchantment(Enchantments.KNOCKBACK, 1)
        setHeldItem(Hand.MAIN_HAND, stack)
    }

    override fun registerData()
    {
        super.registerData()
        dataManager.register(CLOSED, false)
    }

    override fun registerGoals()
    {
        super.registerGoals()
        targetSelector.addGoal(1, HurtByTargetGoal(this).setCallsForHelp())
        targetSelector.addGoal(3, NearestAttackableTargetGoal(this, PlayerEntity::class.java, true))
        targetSelector.addGoal(2, AttackGoal(0.2, true))
        goalSelector.addGoal(4, FireballAttackGoal())
        goalSelector.addGoal(5, MoveTowardsRestrictionGoal(this, 0.2))
        goalSelector.addGoal(7, WaterAvoidingRandomWalkingGoal(this, 0.2, 0.0f))
        goalSelector.addGoal(8, LookAtGoal(this, PlayerEntity::class.java, 8.0f))
        goalSelector.addGoal(8, LookRandomlyGoal(this))
    }

    override fun attackEntityFrom(source: DamageSource, amount: Float): Boolean
    {
        closed = true
        return super.attackEntityFrom(source, amount)
    }

    companion object
    {
        protected val CLOSED = EntityDataManager.createKey(AncientGolemEntity::class.java, DataSerializers.BOOLEAN)
    }

    override fun registerAttributes()
    {
        super.registerAttributes()
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).baseValue = 38.0
        getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).baseValue = 1.2
        getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).baseValue = 30.0
        getAttribute(SharedMonsterAttributes.ARMOR).baseValue = 4.0
        getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).baseValue = 6.0
    }

    private inner class FireballAttackGoal : Goal()
    {
        private var attackStep = 0
        private var attackTime = 0
        private var field_223527_d = 0

        override fun shouldExecute(): Boolean
        {
            val target = this@AncientGolemEntity.attackTarget
            return target != null && target.isAlive && this@AncientGolemEntity.canAttack(target) && this@AncientGolemEntity.position.distanceSq(target.position) > 18
        }

        override fun startExecuting()
        {
            attackStep = 0
        }

        override fun resetTask()
        {
            this@AncientGolemEntity.closed = true
            field_223527_d = 0
        }

        override fun tick()
        {
            --attackTime
            val target : LivingEntity? = this@AncientGolemEntity.attackTarget
            if (target != null)
            {
                val canSeeTarget = this@AncientGolemEntity.entitySenses.canSee(target)
                if (canSeeTarget)
                {
                    field_223527_d = 0
                } else
                {
                    ++field_223527_d
                }
                val distanceToTarget = this@AncientGolemEntity.getDistanceSq(target)
                if (distanceToTarget < 4.0)
                {
                    if (!canSeeTarget)
                        return
                    if (attackTime <= 0)
                    {
                        attackTime = 20
                        this@AncientGolemEntity.attackEntityAsMob(target)
                    }
                    this@AncientGolemEntity.moveHelper.setMoveTo(target.posX, target.posY, target.posZ, 1.0)
                } else if (distanceToTarget < followDistance * followDistance && canSeeTarget)
                {
                    val x = target.posX - this@AncientGolemEntity.posX
                    val y = target.getPosYHeight(0.5) - this@AncientGolemEntity.getPosYHeight(0.5)
                    val z = target.posZ - this@AncientGolemEntity.posZ
                    if (attackTime <= 0)
                    {
                        ++attackStep
                        when
                        {
                            attackStep == 1 ->
                            {
                                attackTime = 60
                                this@AncientGolemEntity.closed = true
                            }
                            attackStep <= 4 ->
                            {
                                attackTime = 6
                            }
                            else            ->
                            {
                                attackTime = 100
                                attackStep = 0
                                this@AncientGolemEntity.closed = true
                            }
                        }
                        if (attackStep > 1)
                        {
                            val halfOfDistance = MathHelper.sqrt(MathHelper.sqrt(distanceToTarget)) * 0.5f
                            this@AncientGolemEntity.world.playEvent(null, 1018, BlockPos(this@AncientGolemEntity), 0)

                            val fireBall = SmallFireballEntity(this@AncientGolemEntity.world, this@AncientGolemEntity, x + this@AncientGolemEntity.rng.nextGaussian() * halfOfDistance.toDouble(), y, z + this@AncientGolemEntity.rng.nextGaussian() * halfOfDistance.toDouble())
                            fireBall.setPosition(fireBall.posX, this@AncientGolemEntity.getPosYHeight(0.5) + 0.5, fireBall.posZ)
                            this@AncientGolemEntity.world.addEntity(fireBall)

                        }
                    }
                    this@AncientGolemEntity.lookController.setLookPositionWithEntity(target, 10.0f, 10.0f)
                } else if (field_223527_d < 5)
                {
                    this@AncientGolemEntity.moveHelper.setMoveTo(target.posX, target.posY, target.posZ, 1.0)
                }
                super.tick()
            }
        }

        private val followDistance: Double
            get() = this@AncientGolemEntity.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).value

        init
        {
            this.mutexFlags = EnumSet.of(Flag.MOVE, Flag.LOOK)
        }
    }

    inner class AttackGoal(speedIn: Double, longMemoryIn: Boolean) : MeleeAttackGoal(this@AncientGolemEntity, speedIn, longMemoryIn)
    {
        private var raiseArmTicks = 0

        override fun startExecuting()
        {
            super.startExecuting()
            raiseArmTicks = 0
        }

        override fun resetTask()
        {
            super.resetTask()
            this@AncientGolemEntity.setAggroed(false)
        }

        override fun tick()
        {
            super.tick()
            ++raiseArmTicks
            this@AncientGolemEntity.setAggroed(raiseArmTicks >= 5 && attackTick < 10)
        }
    }
}