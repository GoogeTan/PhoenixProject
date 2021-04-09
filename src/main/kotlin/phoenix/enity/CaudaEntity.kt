package phoenix.enity

import net.minecraft.block.Block
import net.minecraft.block.ChestBlock
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.*
import net.minecraft.entity.monster.IMob
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.container.Container
import net.minecraft.inventory.container.Slot
import net.minecraft.item.*
import net.minecraft.item.Items.*
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.ListNBT
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.util.Direction
import net.minecraft.util.Hand
import net.minecraft.util.SoundEvents
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import net.minecraft.world.IWorld
import net.minecraft.world.World
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.wrapper.InvWrapper
import phoenix.advancements.SitCaudaTrigger
import phoenix.init.CaudaArmorItem
import phoenix.init.PhoenixContainers
import phoenix.network.NetworkHandler
import phoenix.network.OpenCaudaInventoryPacket
import phoenix.utils.clientPlayer
import phoenix.utils.sendMessage
import java.util.*
import javax.annotation.Nonnull

private val EQUIPMENT = EntityDataManager.createKey(CaudaEntity::class.java, DataSerializers.BOOLEAN)
private val SADDLE    = EntityDataManager.createKey(CaudaEntity::class.java, DataSerializers.BOOLEAN)

open class CaudaEntity(type: EntityType<CaudaEntity>, worldIn: World) : FlyingEntity(type, worldIn), IMob
{
    var chests : Inventory = Inventory(22)
    var saddled : Boolean
        get() = dataManager[SADDLE]
        set(value) = dataManager.set(SADDLE, value)
    var equipment : Boolean
        get() = dataManager[EQUIPMENT]
        set(value) = dataManager.set(EQUIPMENT, value)
    private var itemHandler: LazyOptional<*>? = LazyOptional.of { InvWrapper(chests) }

    fun getArmorStack()  = chests.getStackInSlot(20)
    fun getGreatcoatStack() = chests.getStackInSlot(21)

    init
    {
        experienceValue = 15
    }

    override fun registerGoals()
    {
    }

    override fun registerData()
    {
        super.registerData()
        dataManager.register(EQUIPMENT, false)
        dataManager.register(SADDLE, false)
    }

    override fun canSpawn(worldIn: IWorld, spawnReasonIn: SpawnReason): Boolean = position.y in 9..81 && super.canSpawn(worldIn, spawnReasonIn)

    override fun getStandingEyeHeight(@Nonnull poseIn: Pose, sizeIn: EntitySize): Float = sizeIn.height * 0.35f

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
        getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).baseValue = 1.2
        getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).baseValue = 40.0
        getAttribute(SharedMonsterAttributes.ARMOR).baseValue = 4.0
    }

    override fun travel(positionIn: Vec3d)
    {
        if (this.isAlive)
        {
            val entity = if (passengers.isEmpty()) null else passengers[0]
            if (entity is LivingEntity && this.isBeingRidden && canBeSteered())
            {
                rotationYaw = entity.rotationYaw
                prevRotationYaw = rotationYaw
                rotationPitch = entity.rotationPitch * 0.5f
                setRotation(rotationYaw, rotationPitch)
                renderYawOffset = rotationYaw
                rotationYawHead = rotationYaw
                stepHeight = 1.0f
                jumpMovementFactor = this.aiMoveSpeed * 0.1f

                if (canPassengerSteer())
                {
                    this.aiMoveSpeed = getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).value.toFloat() * 0.225f * ((getArmorStack().item as? CaudaArmorItem)?.material?.speedModifier?:1.0f)
                    super.travel(Vec3d(0.0,  rotationYawHead / 90.0, 1.0))
                    newPosRotationIncrements = 0
                }
                else
                {
                    motion = Vec3d.ZERO
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
                stepHeight = 0.5f
                jumpMovementFactor = 0.02f
                super.travel(positionIn)
            }
        }
    }

    override fun setCustomName(name: ITextComponent?)
    {
        super.setCustomName(StringTextComponent(name?.unformattedComponentText?.replace("Hentai", "Anime girl")?.replace("GoogleTan", "Zahara")?:""))
    }

    override fun dropInventory()
    {
        super.dropInventory()
        if(equipment)
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
        return if (player is ServerPlayerEntity)
        {
            val item = player.getHeldItem(hand)
            if (player != this.controllingPassenger)
            {
                if (Block.getBlockFromItem(item.item) is ChestBlock && !equipment)
                {
                    equipment = true
                    item.shrink(1)
                    player.setHeldItem(hand, item)
                } else if (!saddled && item.item == Items.SADDLE)
                {
                    saddled = true
                    item.shrink(1)
                    player.setHeldItem(hand, item)
                } else if (player.isSneaking)
                {
                    player.openContainer = CaudaContainer(player.currentWindowId, player.inventory)
                    player.openContainer.addListener(player)
                    NetworkHandler.sendTo(OpenCaudaInventoryPacket(this.entityId), player)
                } else if (saddled)
                {
                    SitCaudaTrigger.test(player)
                    mountTo(player)
                }
            }
            true
        } else
            false
    }

    override fun canBeSteered(): Boolean = this.controllingPassenger is LivingEntity

    override fun getControllingPassenger(): Entity? = if (passengers.isEmpty()) null else passengers[0]

    override fun readAdditional(compound: CompoundNBT)
    {
        super.readAdditional(compound)
        equipment = compound.getBoolean("equipment")
        saddled = compound.getBoolean("saddled")

        val listnbt = compound.getList("Items", 10)

        for (i in listnbt.indices)
        {
            val tag = listnbt.getCompound(i)
            chests.setInventorySlotContents(tag.getByte("Slot").toInt(), ItemStack.read(tag))
        }
    }

    override fun writeAdditional(compound: CompoundNBT)
    {
        super.writeAdditional(compound)
        compound.putBoolean("equipment", equipment)
        compound.putBoolean("saddled", saddled)
        val listnbt = ListNBT()
        for (i in 0 until this.chests.sizeInventory)
        {
            val stack: ItemStack = this.chests.getStackInSlot(i)
            if (!stack.isEmpty)
            {
                val tag = CompoundNBT()
                tag.putByte("Slot", i.toByte())
                stack.write(tag)
                listnbt.add(tag)
            }
        }
        compound.put("Items", listnbt)

    }

    override fun <T> getCapability(capability: Capability<T>, facing: Direction?): LazyOptional<T>
    {
        return if (this.isAlive && capability === CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && itemHandler != null) itemHandler!!.cast() else super.getCapability(capability, facing)
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

    inner class CaudaContainer(id: Int, val inventory: PlayerInventory) : Container(PhoenixContainers.CAUDA, id)
    {
        init
        {
            this@CaudaEntity.chests.openInventory(inventory.player)

            var i = 0

            for (y in 0..3)
                for (x in 0..4)
                    addSlot(CaudaEntitySlot(this@CaudaEntity.chests, i++, 18 + 80 + x * 18, 8 + y * 18))

            addSlot(object : Slot(this@CaudaEntity.chests, i++, 172, 92)
            {
                override fun isItemValid(stack: ItemStack) = stack.item is CaudaArmorItem
            })
            addSlot(object : Slot(this@CaudaEntity.chests, i++, 172, 113)
            {
                override fun isItemValid(stack: ItemStack) = stack.item is BannerItem
            })
            inventory.player.sendMessage(i.toString())
            for (x in 0..2)
                for (y in 0..8)
                    addSlot(Slot(inventory, y + x * 9 + 9, 8 + y * 18, 102 + x * 18 + -18))

            for (x in 0..8)
                addSlot(Slot(inventory, x, 8 + x * 18, 142))
        }

        fun getCauda() = this@CaudaEntity

        inner class CaudaEntitySlot(inventoryIn: IInventory, index: Int, x: Int, y: Int) : Slot(inventoryIn, index, x, y)
        {
            override fun isEnabled() = this@CaudaEntity.equipment
        }

        override fun canInteractWith(playerIn: PlayerEntity) = this@CaudaEntity.getDistanceSq(playerIn) < 8

        override fun onContainerClosed(playerIn: PlayerEntity)
        {
            super.onContainerClosed(playerIn)
            this@CaudaEntity.chests.closeInventory(playerIn)
        }

        /*
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
        */
    }
}
