package phoenix.tile

import net.minecraft.fluid.Fluid
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.NetworkManager
import net.minecraft.network.PacketBuffer
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.tileentity.TileEntityType
import net.minecraft.util.Direction
import net.minecraftforge.fluids.capability.templates.FluidTank
import phoenix.network.NetworkHandler
import phoenix.network.SyncFluidThinkPacket
import phoenix.utils.MPair
import phoenix.utils.SerializeUtils.readTank
import phoenix.utils.SerializeUtils.writeToBuf
import phoenix.utils.SizedArrayList
import phoenix.utils.block.PhoenixTile
import phoenix.utils.getTileAt

abstract class AFluidTile(type: TileEntityType<out AFluidTile>) : PhoenixTile(type), ITickableTileEntity
{
    abstract var inputs: SizedArrayList<FluidTank>
    abstract var outputs: SizedArrayList<FluidTank>
    abstract var needSync: Boolean

    override fun tick()
    {
        val world = world
        if(world != null && !world.isRemote)
        {
            for (i in Direction.values())
            {
                val tile = world.getTileAt<AFluidTile>(pos.offset(i))
                if (tile != null)
                {
                    val map = HashMap<Fluid, MPair<Int, Int>>()
                    for (otherTank in inputs)
                    {
                        if (!map.containsKey(otherTank.fluid.fluid))
                            map[otherTank.fluid.fluid] = MPair(0, 0)
                        map[otherTank.fluid.fluid]!!.first++
                        map[otherTank.fluid.fluid]!!.second += otherTank.fluid.amount
                    }
                    for (otherTank in tile.inputs)
                    {
                        if (!map.containsKey(otherTank.fluid.fluid))
                            map[otherTank.fluid.fluid] = MPair(0, 0)
                        map[otherTank.fluid.fluid]!!.first++
                        map[otherTank.fluid.fluid]!!.second += otherTank.fluid.amount
                    }
                    for (otherTank in inputs)
                    {
                        val pair = map[otherTank.fluid.fluid]!!
                        otherTank.fluid.amount = pair.second / pair.first
                        needSync = true
                    }
                    for (otherTank in tile.inputs)
                    {
                        val pair = map[otherTank.fluid.fluid]!!
                        otherTank.fluid.amount = pair.second / pair.first
                        tile.needSync = true
                    }
                }
            }

            if (needSync)
            {
                NetworkHandler.sendToAll(SyncFluidThinkPacket(inputs, outputs, pos))
                needSync = false
            }
        }
    }

    override fun getUpdateTag(): CompoundNBT = write(super.getUpdateTag())

    override fun getUpdatePacket(): FluidTileUpdatePacket = FluidTileUpdatePacket()

    override fun read(tag: CompoundNBT)
    {
        super.read(tag)

        var i = tag.getInt("input_size")

        inputs.resize(i, FluidTank(1000))
        for (j in 0 until i)
            inputs[i].readFromNBT(tag.getCompound("input$j"))

        i = tag.getInt("output_size")

        outputs.resize(i, FluidTank(1000))
        for (j in 0 until i)
            outputs[i].readFromNBT(tag.getCompound("output$j"))
    }

    override fun write(nbt: CompoundNBT): CompoundNBT
    {
        val res = super.write(nbt)
        res.putInt("input_size", inputs.size)
        for (i in inputs.indices)
            res.put("input$i", inputs[i].writeToNBT(CompoundNBT()))
        res.putInt("output_size", outputs.size)
        for (i in outputs.indices)
            res.put("output$i", outputs[i].writeToNBT(CompoundNBT()))
        return res
    }

    inner open class FluidTileUpdatePacket : SUpdateTileEntityPacket()
    {
        override fun writePacketData(buf: PacketBuffer)
        {
            super.writePacketData(buf)
            buf.writeInt(inputs.size)
            for (i in inputs)
                buf.writeToBuf(i)
            buf.writeInt(outputs.size)
            for (i in outputs)
                buf.writeToBuf(i)
        }

        override fun readPacketData(buf: PacketBuffer)
        {
            super.readPacketData(buf)
            inputs = SizedArrayList()
            outputs = SizedArrayList()
            for (i in 0 until buf.readInt())
                inputs.add(buf.readTank())
            for (i in 0 until buf.readInt())
                outputs.add(buf.readTank())
        }
    }
}

abstract class AFluidTankTile(type: TileEntityType<out AFluidTankTile>, capacity: Int) : AFluidTile(type)
{
    val tank = FluidTank(capacity)

    override var inputs: SizedArrayList<FluidTank> = SizedArrayList.of(tank)
    override var outputs: SizedArrayList<FluidTank> = SizedArrayList.of(tank)
}
