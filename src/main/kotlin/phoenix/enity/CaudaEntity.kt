package phoenix.enity

import net.minecraft.block.Block
import net.minecraft.block.ChestBlock
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.*
import net.minecraft.entity.ai.controller.BodyController
import net.minecraft.entity.ai.controller.LookController
import net.minecraft.entity.ai.controller.MovementController
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.monster.IMob
import net.minecraft.entity.passive.CatEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.IInventoryChangedListener
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.container.Container
import net.minecraft.inventory.container.INamedContainerProvider
import net.minecraft.inventory.container.Slot
import net.minecraft.item.*
import net.minecraft.item.Items.*
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.ListNBT
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.server.management.PreYggdrasilConverter
import net.minecraft.util.Direction
import net.minecraft.util.EntityPredicates
import net.minecraft.util.Hand
import net.minecraft.util.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.IWorld
import net.minecraft.world.World
import net.minecraft.world.gen.Heightmap
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.wrapper.InvWrapper
import phoenix.init.PhoenixContainers
import phoenix.network.NetworkHandler
import phoenix.network.OpenCaudaInventoryPacket
import java.util.*
import javax.annotation.Nonnull
import kotlin.experimental.and
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

private val SIZE      = EntityDataManager.createKey(CaudaEntity::class.java, DataSerializers.VARINT)
private val EQUIPMENT = EntityDataManager.createKey(CaudaEntity::class.java, DataSerializers.VARINT)
private val SADDLE    = EntityDataManager.createKey(CaudaEntity::class.java, DataSerializers.BOOLEAN)
private val OWNER_UNIQUE_ID = EntityDataManager.createKey(CaudaEntity::class.java, DataSerializers.OPTIONAL_UNIQUE_ID)

class CaudaEntity(type: EntityType<CaudaEntity>, worldIn: World) : FlyingEntity(type, worldIn), IMob, IInventoryChangedListener
{
    private var orbitOffset = Vec3d.ZERO
    private var orbitPosition = BlockPos.ZERO
    private var attackPhase = AttackPhase.CIRCLE
    var chests : Inventory
    var saddled : Boolean
        get() = dataManager[SADDLE]
        set(value) = dataManager.set(SADDLE, value)
    var equipment : Int
        get() = dataManager[EQUIPMENT]
        set(value) = dataManager.set(EQUIPMENT, value)
    var ownerUUID : UUID?
        get() = dataManager.get(OWNER_UNIQUE_ID).orElse(null)
        set(uniqueId) = dataManager.set(OWNER_UNIQUE_ID, Optional.ofNullable(uniqueId))
    private var itemHandler: LazyOptional<*>? = null

    init
    {
        experienceValue = 15
        moveController = MoveHelperController(this)
        lookController = LookHelperController(this)
        this.chests = Inventory(22)
        this.chests.addListener(this)
        this.itemHandler = LazyOptional.of { InvWrapper(chests) }
    }

    override fun registerGoals()
    {
        goalSelector.addGoal(1, this.PickAttackGoal())
        goalSelector.addGoal(2, this.SweepAttackGoal())
        goalSelector.addGoal(3, this.OrbitPointGoal())
    }

    override fun registerData()
    {
        super.registerData()
        dataManager.register(SIZE, 0)
        dataManager.register(EQUIPMENT, 1)
        dataManager.register(OWNER_UNIQUE_ID, Optional.empty())
        dataManager.register(SADDLE, false)
    }

    override fun canSpawn(worldIn: IWorld, spawnReasonIn: SpawnReason): Boolean = position.y in 9..81 && super.canSpawn(worldIn, spawnReasonIn)

    override fun getStandingEyeHeight(@Nonnull poseIn: Pose, sizeIn: EntitySize): Float = sizeIn.height * 0.35f

    override fun onInitialSpawn(worldIn: IWorld, difficultyIn: DifficultyInstance, reason: SpawnReason, spawnDataIn: ILivingEntityData?, dataTag: CompoundNBT?): ILivingEntityData?
    {
        orbitPosition = BlockPos(this).up(5)
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag)
    }

    @Nonnull
    override fun createBodyController(): BodyController = BodyHelperController(this)

    override fun canAttack(@Nonnull typeIn: EntityType<*>) = true

    override fun <T> getCapability(capability: Capability<T>, facing: Direction?): LazyOptional<T>
    {
        return if (this.isAlive && capability === CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && itemHandler != null) itemHandler!!.cast() else super.getCapability(capability, facing)
    }
    override fun remove(keepData: Boolean)
    {
        super.remove(keepData)
        if (!keepData && itemHandler != null)
        {
            itemHandler!!.invalidate()
            itemHandler = null
        }
    }

    override fun tick()
    {
        super.tick()
        if (world.isRemote)
        {
            val current = MathHelper.cos((entityId * 3 + ticksExisted).toFloat() * 0.13f + Math.PI.toFloat())
            val next    = MathHelper.cos((entityId * 3 + ticksExisted + 1).toFloat() * 0.13f + Math.PI.toFloat())
            if (current > 0.0f && next <= 0.0f)
            {
                world.playSound(posX, posY, posZ, SoundEvents.ENTITY_PHANTOM_FLAP, this.soundCategory, 0.95f + rand.nextFloat() * 0.05f, 0.95f + rand.nextFloat() * 0.05f, false)
            }
        }
    }

    override fun onInventoryChanged(invBasic: IInventory)
    {
    }

    override fun setCustomName(name: ITextComponent?)
    {
        super.setCustomName(StringTextComponent(name?.unformattedComponentText?.replace("Hentai", "Anime girl")?.replace("GoogleTan", "Zahara Z. M.")?:""))
    }

    override fun dropInventory()
    {
        super.dropInventory()
        val equipment = dataManager[EQUIPMENT]
        for (i in 0 until equipment)
            this.entityDropItem(ItemStack(Items.CHEST))
        if(dataManager[SADDLE])
            this.entityDropItem(ItemStack(Items.SADDLE))

        for (i in 0 until this.chests.sizeInventory)
        {
            val stack: ItemStack = this.chests.getStackInSlot(i)
            if (!stack.isEmpty && !EnchantmentHelper.hasVanishingCurse(stack))
                this.entityDropItem(stack)
        }
    }

    override fun processInteract(player: PlayerEntity, hand: Hand): Boolean
    {
        val item = player.getHeldItem(hand)
        return when
        {
            item.item is SpawnEggItem    -> super.processInteract(player, hand)
            this.isChild                 -> super.processInteract(player, hand)
            player is ServerPlayerEntity ->
            {
                if (player != this.controllingPassenger)
                {
                    if (Block.getBlockFromItem(item.item) is ChestBlock && equipment < 4)
                    {
                        equipment++
                        item.shrink(1)
                        player.setHeldItem(hand, item)
                    } else player.startRiding(this)
                } else
                {
                    if (!saddled && item.item == Items.SADDLE)
                    {
                        saddled = true
                        item.shrink(1)
                        player.setHeldItem(hand, item)
                    } else
                    {
                        NetworkHandler.sendTo(OpenCaudaInventoryPacket(this.entityId), player)
                    }
                }

                true
            }
            else                         -> false
        }
    }

    override fun travel(positionIn: Vec3d)
    {
        if (this.isAlive)
        {
            if (this.isBeingRidden && canBeSteered())
            {
                val controller = this.controllingPassenger as LivingEntity?
                if(controller != null)
                {
                    rotationYaw = controller.rotationYaw
                    prevRotationYaw = rotationYaw
                    rotationPitch = controller.rotationPitch * 0.5f
                    setRotation(rotationYaw, rotationPitch)
                    renderYawOffset = rotationYaw
                    rotationYawHead = renderYawOffset
                    val moveStrafing = controller.moveStrafing * 0.5f
                    val moveForward = controller.moveForward
                    jumpMovementFactor = this.aiMoveSpeed * 0.1f
                    if (canPassengerSteer())
                    {
                        this.aiMoveSpeed = getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).value.toFloat()
                        super.travel(Vec3d(moveStrafing.toDouble(), positionIn.y, moveForward.toDouble()))
                    } else if (controller is PlayerEntity)
                    {
                        motion = Vec3d.ZERO
                    }
                    prevLimbSwingAmount = limbSwingAmount
                    val deltaX = posX - prevPosX
                    val deltaZ = posZ - prevPosZ
                    var speed = MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ) * 4.0f
                    if (speed > 1.0f)
                    {
                        speed = 1.0f
                    }
                    limbSwingAmount += (speed - limbSwingAmount) * 0.4f
                    limbSwing += limbSwingAmount
                }
            } else
            {
                jumpMovementFactor = 0.02f
                super.travel(positionIn)
            }
        }
    }

    override fun canBeLeashedTo(player: PlayerEntity?): Boolean = !this.leashed

    override fun getControllingPassenger(): Entity? = if (passengers.isEmpty()) null else passengers[0]

    override fun readAdditional(compound: CompoundNBT)
    {
        super.readAdditional(compound)
        val s: String = if (compound.contains("OwnerUUID", 8))
        {
            compound.getString("OwnerUUID")
        } else
        {
            val s1 = compound.getString("Owner")
            PreYggdrasilConverter.convertMobOwnerIfNeeded(this.server!!, s1)
        }
        if (s.isNotEmpty())
        {
            ownerUUID = UUID.fromString(s)
        }
        equipment = compound.getInt("equipment")
        saddled = compound.getBoolean("saddled")

        val listnbt = compound.getList("Items", 10)

        for (i in listnbt.indices)
        {
            val compoundnbt = listnbt.getCompound(i)
            val j = compoundnbt.getByte("Slot") and 255.toByte()
            if (j >= 0 && j < chests.sizeInventory)
            {
                chests.setInventorySlotContents(j.toInt(), ItemStack.read(compoundnbt))
            }
        }
    }

    override fun writeAdditional(compound: CompoundNBT)
    {
        super.writeAdditional(compound)
        compound.putInt("equipment", equipment)
        compound.putBoolean("saddled", saddled)
        if (ownerUUID != null)
        {
            compound.putString("OwnerUUID", ownerUUID.toString())
        }
        val listnbt = ListNBT()
        for (i in 0 until this.chests.sizeInventory)
        {
            val stack: ItemStack = this.chests.getStackInSlot(i)
            if (!stack.isEmpty)
            {
                val compoundnbt = CompoundNBT()
                compoundnbt.putByte("Slot", i.toByte())
                stack.write(compoundnbt)
                listnbt.add(compoundnbt)
            }
        }
        compound.put("Items", listnbt)

    }

    internal enum class AttackPhase
    {
        CIRCLE, SWOOP
    }

    inner class BodyHelperController(mob: MobEntity) : BodyController(mob)
    {
        override fun updateRenderAngles()
        {
            this@CaudaEntity.rotationYawHead = this@CaudaEntity.renderYawOffset
            this@CaudaEntity.renderYawOffset = this@CaudaEntity.rotationYaw
        }
    }

    class LookHelperController(entityIn: MobEntity) : LookController(entityIn) { override fun tick() {} }

    abstract inner class MoveGoal : Goal()
    {
        protected fun isNear() = this@CaudaEntity.orbitOffset.squareDistanceTo(this@CaudaEntity.posX, this@CaudaEntity.posY, this@CaudaEntity.posZ) < 4.0

        init {  this.mutexFlags = EnumSet.of(Flag.MOVE)  }
    }

    inner class MoveHelperController(entityIn: MobEntity) : MovementController(entityIn)
    {
        private var speedFactor = 0.1f
        override fun tick()
        {
            if (this@CaudaEntity.collidedHorizontally)
            {
                this@CaudaEntity.rotationYaw += 180.0f
                speedFactor = 0.1f
            }
            var deltaX = (this@CaudaEntity.orbitOffset.x - this@CaudaEntity.posX).toFloat()
            val deltaY = (this@CaudaEntity.orbitOffset.y - this@CaudaEntity.posY).toFloat()
            var deltaZ = (this@CaudaEntity.orbitOffset.z - this@CaudaEntity.posZ).toFloat()
            var lengthHorizontal = MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ).toDouble()
            val yaw = 1.0 - MathHelper.abs(deltaY * 0.7f).toDouble() / lengthHorizontal

            deltaX = (deltaX.toDouble() * yaw).toFloat()
            deltaZ = (deltaZ.toDouble() * yaw).toFloat()

            lengthHorizontal = MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ).toDouble()

            val length = MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ + deltaY * deltaY).toDouble()

            val rotationYaw = this@CaudaEntity.rotationYaw

            val look = MathHelper.atan2(deltaZ.toDouble(), deltaX.toDouble()).toFloat()
            val normalizedYaw = MathHelper.wrapDegrees(this@CaudaEntity.rotationYaw + 90.0f)
            val normalizedPitch = MathHelper.wrapDegrees(look * (180f / Math.PI.toFloat()))

            this@CaudaEntity.rotationYaw = MathHelper.approachDegrees(normalizedYaw, normalizedPitch, 4.0f) - 90.0f
            this@CaudaEntity.renderYawOffset = this@CaudaEntity.rotationYaw

            speedFactor = if (MathHelper.degreesDifferenceAbs(rotationYaw, this@CaudaEntity.rotationYaw) < 3.0f)
                 MathHelper.approach(speedFactor, 1.8f, 0.005f * (1.8f / speedFactor))
             else
                 MathHelper.approach(speedFactor, 0.2f, 0.025f)

            val f7 = (-(MathHelper.atan2((-deltaY).toDouble(), lengthHorizontal) * (180f / Math.PI.toFloat()).toDouble())).toFloat()
            this@CaudaEntity.rotationPitch = f7
            val f8: Float = this@CaudaEntity.rotationYaw + 90.0f
            val d3 = (speedFactor * MathHelper.cos(f8 * (Math.PI.toFloat() / 180f))).toDouble() * abs(deltaX.toDouble() / length)
            val d4 = (speedFactor * MathHelper.sin(f8 * (Math.PI.toFloat() / 180f))).toDouble() * abs(deltaZ.toDouble() / length)
            val d5 = (speedFactor * MathHelper.sin(f7 * (Math.PI.toFloat() / 180f))).toDouble() * abs(deltaY.toDouble() / length)
            val vec3d: Vec3d = this@CaudaEntity.motion
            this@CaudaEntity.motion = vec3d.add(Vec3d(d3, d5, d4).subtract(vec3d).scale(0.2))
        }
    }

    inner class OrbitPointGoal : MoveGoal()
    {
        private var field_203150_c = 0f
        private var field_203151_d = 0f
        private var field_203152_e = 0f
        private var field_203153_f = 0f

        override fun shouldExecute(): Boolean
        {
            return this@CaudaEntity.attackTarget == null || this@CaudaEntity.attackPhase == AttackPhase.CIRCLE
        }

        override fun startExecuting()
        {
            field_203151_d = 5.0f + this@CaudaEntity.rand.nextFloat() * 10.0f
            field_203152_e = -4.0f + this@CaudaEntity.rand.nextFloat() * 9.0f
            field_203153_f = if (this@CaudaEntity.rand.nextBoolean()) 1.0f else -1.0f
            validateOrbitPosition()
        }

        override fun tick()
        {
            if (this@CaudaEntity.rand.nextInt(350) == 0)
            {
                field_203152_e = -4.0f + this@CaudaEntity.rand.nextFloat() * 9.0f
            }
            if (this@CaudaEntity.rand.nextInt(250) == 0)
            {
                ++field_203151_d
                if (field_203151_d > 15.0f)
                {
                    field_203151_d = 5.0f
                    field_203153_f = -field_203153_f
                }
            }
            if (this@CaudaEntity.rand.nextInt(450) == 0)
            {
                field_203150_c = this@CaudaEntity.rand.nextFloat() * 2.0f * Math.PI.toFloat()
                validateOrbitPosition()
            }
            if (isNear())
            {
                validateOrbitPosition()
            }
            if (this@CaudaEntity.orbitOffset.y < this@CaudaEntity.posY && !this@CaudaEntity.world.isAirBlock(BlockPos(this@CaudaEntity).down(1)))
            {
                field_203152_e = max(1.0f, field_203152_e)
                validateOrbitPosition()
            }
            if (this@CaudaEntity.orbitOffset.y > this@CaudaEntity.posY && !this@CaudaEntity.world.isAirBlock(BlockPos(this@CaudaEntity).up(1)))
            {
                field_203152_e = min(-1.0f, field_203152_e)
                validateOrbitPosition()
            }
        }

        private fun validateOrbitPosition()
        {
            if (BlockPos.ZERO == this@CaudaEntity.orbitPosition)
            {
                this@CaudaEntity.orbitPosition = BlockPos(this@CaudaEntity)
            }
            field_203150_c += field_203153_f * 15.0f * (Math.PI.toFloat() / 180f)
            this@CaudaEntity.orbitOffset = Vec3d(this@CaudaEntity.orbitPosition)
            .add(
                (field_203151_d * MathHelper.cos(field_203150_c)).toDouble(),
                (-4.0f + field_203152_e).toDouble(),
                (field_203151_d * MathHelper.sin(field_203150_c)).toDouble()
            )
        }
    }

    inner class PickAttackGoal : Goal()
    {
        private var tickDelay = 0

        override fun shouldExecute(): Boolean
        {
            val entity: LivingEntity? = this@CaudaEntity.attackTarget
            return if (entity != null) this@CaudaEntity.canAttack(entity, EntityPredicate.DEFAULT) else false
        }


        override fun startExecuting()
        {
            tickDelay = 10
            this@CaudaEntity.attackPhase = AttackPhase.CIRCLE
            validateOrbitPosition()
        }

        override fun resetTask()
        {
            this@CaudaEntity.orbitPosition =
                this@CaudaEntity.world.getHeight(Heightmap.Type.MOTION_BLOCKING, this@CaudaEntity.orbitPosition)
                    .up(10 + this@CaudaEntity.rand.nextInt(20))
        }

        override fun tick()
        {
            if (this@CaudaEntity.attackPhase == AttackPhase.CIRCLE)
            {
                --tickDelay
                if (tickDelay <= 0)
                {
                    this@CaudaEntity.attackPhase = AttackPhase.SWOOP
                    validateOrbitPosition()
                    tickDelay = (8 + this@CaudaEntity.rand.nextInt(4)) * 20
                    this@CaudaEntity.playSound(
                        SoundEvents.ENTITY_PHANTOM_SWOOP,
                        10.0f,
                        0.95f + this@CaudaEntity.rand.nextFloat() * 0.1f
                    )
                }
            }
        }

        private fun validateOrbitPosition()
        {
            this@CaudaEntity.orbitPosition = BlockPos(this@CaudaEntity.attackTarget!!).up(20 + this@CaudaEntity.rand.nextInt(20))
            if (this@CaudaEntity.orbitPosition.y < this@CaudaEntity.world.seaLevel)
            {
                this@CaudaEntity.orbitPosition = BlockPos(this@CaudaEntity.orbitPosition.x, this@CaudaEntity.world.seaLevel + 1, this@CaudaEntity.orbitPosition.z)
            }
        }
    }

    inner class SweepAttackGoal : MoveGoal()
    {
        override fun shouldExecute() = this@CaudaEntity.attackTarget != null && this@CaudaEntity.attackPhase == AttackPhase.SWOOP

        override fun shouldContinueExecuting(): Boolean
        {
            val livingentity: LivingEntity? = this@CaudaEntity.attackTarget
            return if (livingentity == null)
            {
                false
            } else if (!livingentity.isAlive)
            {
                false
            } else if (livingentity !is PlayerEntity || !livingentity.isSpectator && !livingentity.isCreative)
            {
                if (!shouldExecute())
                {
                    false
                } else
                {
                    if (this@CaudaEntity.ticksExisted % 20 == 0)
                    {
                        val list: List<CatEntity> = this@CaudaEntity.world.getEntitiesWithinAABB(
                            CatEntity::class.java,
                            this@CaudaEntity.boundingBox.grow(16.0),
                            EntityPredicates.IS_ALIVE
                        )
                        if (list.isNotEmpty())
                        {
                            for (catentity in list)
                            {
                                catentity.func_213420_ej()
                            }
                            return false
                        }
                    }
                    true
                }
            } else
            {
                false
            }
        }

        override fun startExecuting() {  }

        override fun resetTask()
        {
            this@CaudaEntity.attackTarget = null
            this@CaudaEntity.attackPhase = AttackPhase.CIRCLE
        }

        override fun tick()
        {
            val livingentity: LivingEntity = this@CaudaEntity.attackTarget!!
            this@CaudaEntity.orbitOffset =
                Vec3d(livingentity.posX, livingentity.getPosYHeight(0.5), livingentity.posZ)
            if (this@CaudaEntity.boundingBox.grow(0.2f.toDouble()).intersects(livingentity.boundingBox))
            {
                this@CaudaEntity.attackEntityAsMob(livingentity)
                this@CaudaEntity.attackPhase = AttackPhase.CIRCLE
                this@CaudaEntity.world.playEvent(1039, BlockPos(this@CaudaEntity), 0)
            } else if (this@CaudaEntity.collidedHorizontally || this@CaudaEntity.hurtTime > 0)
            {
                this@CaudaEntity.attackPhase = AttackPhase.CIRCLE
            }
        }
    }


    inner class CaudaContainer(id: Int, val inventory: PlayerInventory) : Container(PhoenixContainers.CAUDA, id),
        INamedContainerProvider
    {
        init
        {
            this@CaudaEntity.chests.openInventory(inventory.player)

            var i = 0

            for (y in 0..3)
                for (x in 0 until 5)
                    addSlot(CaudaEntitySlot(this@CaudaEntity.chests, i++, 18 + 80 + x * 18, 8 + y * 18))

            addSlot(object : Slot(this@CaudaEntity.chests, i++, 172, 92)
            {
                override fun isItemValid(stack: ItemStack) = stack.item is ArmorItem
            })
            addSlot(object : Slot(this@CaudaEntity.chests, i++, 172, 113)
            {
                override fun isItemValid(stack: ItemStack) = stack.item is BannerItem
            })

            for (x in 0..2)
                for (y in 0..8)
                    addSlot(Slot(inventory, y + x * 9 + 9, 8 + y * 18, 102 + x * 18 + -18))

            for (x in 0..8)
                addSlot(Slot(inventory, x, 8 + x * 18, 142))
        }

        fun getCauda() = this@CaudaEntity

        inner class CaudaEntitySlot(inventoryIn: IInventory, index: Int, x: Int, y: Int) : Slot(inventoryIn, index, x, y)
        {
            override fun isEnabled() = this@CaudaEntity.chests.sizeInventory > this.slotIndex
        }

        override fun canInteractWith(playerIn: PlayerEntity) = this@CaudaEntity.getDistanceSq(playerIn) < 8

        override fun createMenu(id: Int, inventory: PlayerInventory, player: PlayerEntity) = CaudaContainer(id, inventory)

        override fun getDisplayName(): ITextComponent = this@CaudaEntity.name?:StringTextComponent("Cauda")

        override fun transferStackInSlot(playerIn: PlayerEntity?, index: Int): ItemStack?
        {
            var itemstack = ItemStack.EMPTY
            val slot = inventorySlots[index]
            if (slot != null && slot.hasStack)
            {
                val itemstack1 = slot.stack
                itemstack = itemstack1.copy()
                val i: Int = this@CaudaEntity.chests.sizeInventory
                if (index < i)
                {
                    if (!mergeItemStack(itemstack1, i, inventorySlots.size, true))
                    {
                        return ItemStack.EMPTY
                    }
                } else if (getSlot(1).isItemValid(itemstack1) && !getSlot(1).hasStack)
                {
                    if (!mergeItemStack(itemstack1, 1, 2, false))
                    {
                        return ItemStack.EMPTY
                    }
                } else if (getSlot(0).isItemValid(itemstack1))
                {
                    if (!mergeItemStack(itemstack1, 0, 1, false))
                    {
                        return ItemStack.EMPTY
                    }
                } else if (i <= 2 || !mergeItemStack(itemstack1, 2, i, false))
                {
                    val j = i + 27
                    val k = j + 9
                    if (index in j until k)
                    {
                        if (!mergeItemStack(itemstack1, i, j, false))
                        {
                            return ItemStack.EMPTY
                        }
                    } else if (index in i until j)
                    {
                        if (!mergeItemStack(itemstack1, j, k, false))
                        {
                            return ItemStack.EMPTY
                        }
                    } else if (!mergeItemStack(itemstack1, j, j, false))
                    {
                        return ItemStack.EMPTY
                    }
                    return ItemStack.EMPTY
                }
                if (itemstack1.isEmpty)
                {
                    slot.putStack(ItemStack.EMPTY)
                } else
                {
                    slot.onSlotChanged()
                }
            }
            return itemstack
        }
    }
}
