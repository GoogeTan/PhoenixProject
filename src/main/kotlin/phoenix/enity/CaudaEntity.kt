package phoenix.enity

import net.minecraft.block.Block
import net.minecraft.block.ChestBlock
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.*
import net.minecraft.entity.ai.controller.BodyController
import net.minecraft.entity.ai.controller.LookController
import net.minecraft.entity.ai.controller.MovementController
import net.minecraft.entity.monster.IMob
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.inventory.IInventory
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
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.wrapper.InvWrapper
import phoenix.init.CaudaArmorItem
import phoenix.init.PhoenixContainers
import phoenix.network.NetworkHandler
import phoenix.network.OpenCaudaInventoryPacket
import java.util.*
import javax.annotation.Nonnull
import kotlin.experimental.and
import kotlin.math.abs

private val SIZE      = EntityDataManager.createKey(CaudaEntity::class.java, DataSerializers.VARINT)
private val EQUIPMENT = EntityDataManager.createKey(CaudaEntity::class.java, DataSerializers.VARINT)
private val SADDLE    = EntityDataManager.createKey(CaudaEntity::class.java, DataSerializers.BOOLEAN)

open class CaudaEntity(type: EntityType<CaudaEntity>, worldIn: World) : FlyingEntity(type, worldIn), IMob
{
    var chests : Inventory
    var saddled : Boolean
        get() = dataManager[SADDLE]
        set(value) = dataManager.set(SADDLE, value)
    var equipment : Int
        get() = dataManager[EQUIPMENT]
        set(value) = dataManager.set(EQUIPMENT, value)
    private var itemHandler: LazyOptional<*>? = null

    fun getArmorStack()  = chests.getStackInSlot(20)
    fun getGreatcoatStack() = chests.getStackInSlot(21)

    init
    {
        experienceValue = 15
        this.chests = Inventory(22)
        this.itemHandler = LazyOptional.of { InvWrapper(chests) }
    }

    override fun registerGoals()
    {
    }

    override fun registerData()
    {
        super.registerData()
        dataManager.register(SIZE, 0)
        dataManager.register(EQUIPMENT, 1)
        dataManager.register(SADDLE, false)
    }

    override fun canSpawn(worldIn: IWorld, spawnReasonIn: SpawnReason): Boolean = position.y in 9..81 && super.canSpawn(worldIn, spawnReasonIn)

    override fun getStandingEyeHeight(@Nonnull poseIn: Pose, sizeIn: EntitySize): Float = sizeIn.height * 0.35f

    override fun onInitialSpawn(worldIn: IWorld, difficultyIn: DifficultyInstance, reason: SpawnReason, spawnDataIn: ILivingEntityData?, dataTag: CompoundNBT?): ILivingEntityData?
    {
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag)
    }

    override fun canAttack(@Nonnull typeIn: EntityType<*>) = true

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

    override fun registerAttributes()
    {
        super.registerAttributes()
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).baseValue = 28.0
        getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).baseValue = 0.6f.toDouble()
        getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).baseValue = 40.0
        getAttribute(SharedMonsterAttributes.ARMOR).baseValue = 4.0
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
        return when (player)
        {
            is ServerPlayerEntity ->
            {
                if (player != this.controllingPassenger)
                {
                    if (Block.getBlockFromItem(item.item) is ChestBlock && equipment < 4)
                    {
                        equipment++
                        item.shrink(1)
                        player.setHeldItem(hand, item)
                    } else mountTo(player)
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
            else              -> false
        }
    }

    override fun canBeSteered(): Boolean
    {
        return this.controllingPassenger is LivingEntity
    }

    override fun updatePassenger(passenger: Entity)
    {
        super.updatePassenger(passenger)
        if (passenger is MobEntity)
        {
            renderYawOffset = passenger.renderYawOffset
        }
    }

    override fun getMoveHelper(): MovementController?
    {
        return if (this.isPassenger && ridingEntity is MobEntity) (ridingEntity as MobEntity).moveHelper else moveController
    }


    override fun travel(positionIn: Vec3d)
    {
        /*
        if (this.isAlive)
        {
            if (this.isBeingRidden && canBeSteered() && saddled)
            {
                val passanger = this.controllingPassenger!! as LivingEntity
                rotationYaw = passanger.rotationYaw
                prevRotationYaw = rotationYaw
                rotationPitch = passanger.rotationPitch * 0.5f
                setRotation(rotationYaw, rotationPitch)
                renderYawOffset = rotationYaw
                rotationYawHead = renderYawOffset
                val strafing = passanger.moveStrafing * 0.5
                val forward = passanger.moveForward.toDouble()
                jumpMovementFactor = this.aiMoveSpeed * 0.1f
                if (canPassengerSteer())
                {
                    this.aiMoveSpeed = getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).value.toFloat()
                    super.travel(Vec3d(strafing, positionIn.y, forward))
                }
                prevLimbSwingAmount = limbSwingAmount
                val deltaX = posX - prevPosX
                val deltaZ = posZ - prevPosZ
                var dist = MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ) * 4.0f
                if (dist > 1.0f)
                {
                    dist = 1.0f
                }
                limbSwingAmount += (dist - limbSwingAmount) * 0.4f
                limbSwing += limbSwingAmount
            } else
            {
                jumpMovementFactor = 0.02f
                super.travel(positionIn)
            }
        }

         */
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

    override fun <T> getCapability(capability: Capability<T>, facing: Direction?): LazyOptional<T>
    {
        return if (this.isAlive && capability === CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && itemHandler != null) itemHandler!!.cast() else super.getCapability(
            capability,
            facing
        )
    }

    override fun canPassengerSteer(): Boolean
    {
        return canBeSteered() && super.canPassengerSteer()
    }

    protected open fun mountTo(player: PlayerEntity)
    {
        if (!world.isRemote)
        {
            player.rotationYaw = rotationYaw
            player.rotationPitch = rotationPitch
            player.startRiding(this)
        }
    }


    inner class CaudaContainer(id: Int, val inventory: PlayerInventory) : Container(PhoenixContainers.CAUDA, id), INamedContainerProvider
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
                override fun isItemValid(stack: ItemStack) = stack.item is CaudaArmorItem
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
