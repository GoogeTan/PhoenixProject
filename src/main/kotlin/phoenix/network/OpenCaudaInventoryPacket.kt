package phoenix.network

import net.minecraft.client.entity.player.ClientPlayerEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.network.PacketBuffer
import phoenix.client.gui.CaudaGuiContainer
import phoenix.enity.CaudaEntity
import phoenix.utils.mc

class OpenCaudaInventoryPacket(var entityId : Int) : NetworkHandler.Packet()
{
    constructor() : this(0)

    override fun encode(packet: NetworkHandler.Packet, buf: PacketBuffer)
    {
        if(packet is OpenCaudaInventoryPacket)
        {
            entityId = packet.entityId
            buf.writeInt(entityId)
        }
    }

    override fun decode(buf: PacketBuffer): NetworkHandler.Packet = OpenCaudaInventoryPacket(buf.readInt())

    override fun client(player: ClientPlayerEntity?)
    {
        if(player == null)
            return
        val cauda: Entity? = player.world.getEntityByID(entityId)
        if (cauda is CaudaEntity)
        {
            val container = cauda.CaudaContainer(0, player.inventory)
            player.openContainer = container
            mc.displayGuiScreen(CaudaGuiContainer(container, player.inventory, cauda.name))
        }
    }

    override fun server(player: ServerPlayerEntity?)
    {
        TODO("Not yet implemented")
    }
}