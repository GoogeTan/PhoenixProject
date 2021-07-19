package phoenix.enity

import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.monster.MonsterEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.SmallFireballEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.network.datasync.DataParameter
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.util.DamageSource
import net.minecraft.util.Hand
import net.minecraft.util.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import phoenix.enity.projectile.IceBallEntity
import phoenix.enity.projectile.KnifeEntity
import phoenix.init.PhxEntities
import phoenix.utils.add
import phoenix.utils.getEnchantmentLevel
import java.util.*

class AncientGolemEntity(type: EntityType<out AncientGolemEntity> = PhxEntities.ancientGolemEntity, worldIn: World) : MonsterEntity(type, worldIn)
{
    var breathState = 0.0f
    var closing = true
    private var closed : Boolean
        inline get() = dataManager[CLOSED]
        inline set(value)
        {
            dataManager[CLOSED] = value
            if (value)
            {
                closedTimer = (closedTimer * 0.7 + 20 * 10).toInt()
                getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).baseValue = 0.2
                getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).baseValue = 15.0
                breathState = 0F
            }
            else
            {
                closedTimer = 0
                getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).baseValue = 1.2
                getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).baseValue = 30.0
            }
        }

    var closedTimer = 0
    var shownHand = false

    override fun livingTick()
    {
        super.livingTick()

        if (closing)
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

        shownHand = attackTarget != null && BlockPos(this).distanceSq(BlockPos(attackTarget)) < 5.0 && !closed
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
        if (source.trueSource != null)
            closed = true
        return super.attackEntityFrom(source, amount)
    }

    companion object
    {
        protected val CLOSED                       : DataParameter<Boolean> = EntityDataManager.createKey(AncientGolemEntity::class.java, DataSerializers.BOOLEAN)
        protected val COVERED_ARMOR_BONUS_ID       : UUID                   = UUID.fromString("910aaa79-7db8-47ec-93ad-c03da665d22f")
        protected val COVERED_ARMOR_BONUS_MODIFIER : AttributeModifier      = AttributeModifier(COVERED_ARMOR_BONUS_ID, "Covered armor bonus", 20.0, AttributeModifier.Operation.ADDITION).setSaved(false)
    }

    fun updateArmorModifier(int: Int)
    {
        if (!world.isRemote)
        {
            getAttribute(SharedMonsterAttributes.ARMOR).removeModifier(COVERED_ARMOR_BONUS_MODIFIER)
            if (int == 0)
            {
                getAttribute(SharedMonsterAttributes.ARMOR).applyModifier(COVERED_ARMOR_BONUS_MODIFIER)
                playSound(SoundEvents.ENTITY_SHULKER_CLOSE, 1.0f, 1.0f)
            } else
            {
                playSound(SoundEvents.ENTITY_SHULKER_OPEN, 1.0f, 1.0f)
            }
        }
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

    override fun dropInventory()
    {
        entityDropItem(getHeldItem(Hand.OFF_HAND))
        super.dropInventory()
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
                    if (attackTime <= 0)
                    {
                        ++attackStep
                        when
                        {
                            attackStep == 1 ->
                            {
                                attackTime = 60
                            }
                            attackStep <= 4 ->
                            {
                                attackTime = 6
                            }
                            else            ->
                            {
                                attackTime = 100
                                attackStep = 0
                            }
                        }

                        if (attackStep > 1)
                        {
                            IceBallEntity(world, this@AncientGolemEntity).apply {
                                shoot(this@AncientGolemEntity, this@AncientGolemEntity.rotationPitch, this@AncientGolemEntity.rotationYaw, 0.0f, 2f, 0.3f)
                                world.addEntity(this)
                            }
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
