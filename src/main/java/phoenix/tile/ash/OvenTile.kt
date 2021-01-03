package phoenix.tile.ash

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.NetworkManager
import net.minecraft.network.PacketBuffer
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.server.ServerWorld
import phoenix.blocks.ash.OvenBlock
import phoenix.containers.ash.OvenContainer
import phoenix.init.PhoenixItems
import phoenix.init.PhoenixTiles
import phoenix.network.NetworkHandler
import phoenix.network.SyncOvenPacket
import phoenix.recipes.OvenRecipe.recipes_from_inputs
import phoenix.utils.BlockPosUtils
import phoenix.utils.block.PhoenixTile
import java.lang.Integer.max
import java.lang.Integer.min

class OvenTile : PhoenixTile<OvenTile>(PhoenixTiles.OVEN.get()), ITickableTileEntity, IInventory
{
    var timers = IntArray(4)
    var burnTime = 0
    private val maxBurnTime = 20 * 60
    var inventory = OvenContainer()

    init
    {
        timers[0] = -1
        timers[1] = -1
        timers[2] = -1
        timers[3] = -1
    }

    fun outOtherItems() : List<ItemStack>
    {
        val res = ArrayList<ItemStack>()
        var has = false
        for (i in 0..3)
        {
            if(!recipes_from_inputs.contains(inventory[i].item) || inventory[i].item == PhoenixItems.COOKED_SETA)
            {
                res.add(inventory[i].copy())
                inventory[i] = ItemStack.EMPTY
                timers[i] = 0
                has = true
            }
        }
        if(has)
            NetworkHandler.sendToAll(SyncOvenPacket(this))
        return res
    }

    fun getOtherItems() : List<ItemStack>
    {
        val res = ArrayList<ItemStack>()
        for (i in 0..3)
        {
            if(!recipes_from_inputs.contains(inventory[i].item))
            {
                res.add(inventory[i]);
            }
        }
        return res
    }

    fun addItem(stack : ItemStack) : Boolean
    {
        for (i in 0..3)
            if(inventory[i].item == Items.AIR)
            {
                inventory[i] = ItemStack(stack.item)
                timers[i] = 0
                return true
            }
        return false
    }

    override fun tick()
    {
        if(!world!!.isRemote && world != null)
        {
            if(burnTime > 0)
            {
                burnTime--
                burnTime = min(maxBurnTime, burnTime)
                burnTime = max(0, burnTime)
                if(world!!.getBlockState(pos)[OvenBlock.STATE] == 2)
                {
                    var has = false
                    for (i in 0..3)
                    {
                        val current = inventory[i]
                        if (recipes_from_inputs.contains(current.item))
                        {
                            val recipe = recipes_from_inputs[current.item]!!
                            val cookTime: Int = recipe.cookTime
                            timers[i]++
                            if (timers[i] >= cookTime)
                            {
                                inventory[i] = recipe.result
                                has = true
                                timers[i] = 0
                            }
                        }
                    }
                    if (has)
                        NetworkHandler.sendToDim(SyncOvenPacket(this), world!!.dimension.type)
                }
                if (burnTime == 0)
                    world!!.setBlockState(pos, world!!.getBlockState(pos).with(OvenBlock.STATE, 0))
            }
        }
    }

    override fun getUpdatePacket(): SUpdateTileEntityPacket = UpdatePacket(timers, burnTime, inventory, pos)

    override fun onDataPacket(net: NetworkManager, pkt: SUpdateTileEntityPacket)
    {
        timers = (pkt as UpdatePacket).timers
        burnTime = pkt.burnTime
    }

    override fun write(compound: CompoundNBT): CompoundNBT?
    {
        compound.putIntArray("timers", timers)
        compound.putInt("burn_time", burnTime)

        val nbt = CompoundNBT()
        for (i in 0..3)
        {
            val stack = CompoundNBT()
            inventory[i].write(stack)
            nbt.put("slot$i", stack)
        }

        compound.put("container", nbt)

        return super.write(compound)
    }


    override fun read(compound: CompoundNBT)
    {
        timers = compound.getIntArray("timers")
        burnTime = compound.getInt("burn_time")
        val nbt = compound.getCompound("container")
        for (i in 0..3)
        {
            val current = nbt.getCompound("slot$i")
            inventory[i] = ItemStack.read(current)
        }
        super.read(compound)
    }


    class UpdatePacket(var timers: IntArray, var burnTime : Int, var container: OvenContainer, pos: BlockPos) : SUpdateTileEntityPacket(
        pos,
        32,
        CompoundNBT()
    )
    {
        override fun writePacketData(buf: PacketBuffer)
        {
            super.writePacketData(buf)
            buf.writeVarIntArray(timers)
            buf.writeVarInt(burnTime)
            buf.writeItemStack(container[0])
            buf.writeItemStack(container[1])
            buf.writeItemStack(container[2])
            buf.writeItemStack(container[3])
        }

        override fun readPacketData(buf: PacketBuffer)
        {
            super.readPacketData(buf)
            timers = buf.readVarIntArray()
            burnTime = buf.readInt()
            container[0] = buf.readItemStack()
            container[1] = buf.readItemStack()
            container[2] = buf.readItemStack()
            container[3] = buf.readItemStack()
        }
    }

    override fun clear() = inventory.inventory.clear()
    override fun getSizeInventory() = inventory.size
    override fun isEmpty() = inventory.inventory.isEmpty()
    override fun getStackInSlot(index: Int): ItemStack = inventory[index]
    override fun isUsableByPlayer(player: PlayerEntity) = BlockPosUtils.distanceTo(player.position, pos) < 20

    override fun decrStackSize(index: Int, count: Int): ItemStack
    {
        inventory[index].shrink(count);
        return inventory[index]
    }

    override fun removeStackFromSlot(index: Int) : ItemStack
    {
        val tmp = inventory[index];
        inventory[index] = ItemStack.EMPTY
        return tmp
    }

    override fun setInventorySlotContents(index : Int, stack : ItemStack)
    {
        inventory[index] = stack
    }

    operator fun get(i : Int) = getStackInSlot(i)
    operator fun set(i : Int, stack : ItemStack) = setInventorySlotContents(i, stack)
}

