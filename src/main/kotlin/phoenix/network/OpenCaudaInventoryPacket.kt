package phoenix.network

import net.minecraft.client.entity.player.ClientPlayerEntity
import net.minecraft.entity.Entity
import net.minecraft.network.PacketBuffer
import phoenix.client.gui.CaudaGuiContainer
import phoenix.enity.CaudaEntity
import phoenix.utils.mc

class OpenCaudaInventoryPacket(var entityId : Int) : Packet()
{
    override fun processClient(player: ClientPlayerEntity?)
    {
        if(player == null) return
        val cauda: Entity? = player.world.getEntityByID(entityId)
        if (cauda is CaudaEntity)
        {
            val container = cauda.CaudaContainer(0, player.inventory)
            player.openContainer = container
            mc.displayGuiScreen(CaudaGuiContainer(container, player.inventory, cauda.name))
        }
    }

    object Serializer : Packet.Serializer<OpenCaudaInventoryPacket>()
    {
        override fun encode(packet: OpenCaudaInventoryPacket, buf: PacketBuffer) = buf.writeInt(packet.entityId)
        override fun decode(buf: PacketBuffer): OpenCaudaInventoryPacket = OpenCaudaInventoryPacket(buf.readInt())
    }
}