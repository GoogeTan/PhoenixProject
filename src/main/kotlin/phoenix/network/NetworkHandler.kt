
package phoenix.network

import io.netty.buffer.ByteBuf
import net.minecraft.client.entity.player.ClientPlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.network.PacketBuffer
import net.minecraft.util.ResourceLocation
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.dimension.DimensionType
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.fml.LogicalSide
import net.minecraftforge.fml.network.NetworkEvent
import net.minecraftforge.fml.network.NetworkRegistry
import net.minecraftforge.fml.network.PacketDistributor
import phoenix.Phoenix
import phoenix.other.clientPlayer
import java.util.function.Supplier
import kotlin.reflect.KClass

private val CHANNEL = NetworkRegistry.newSimpleChannel(ResourceLocation(Phoenix.MOD_ID, "network"), { "2.0" }, { true }, { true })

fun initPacketSystem()
{
    registerPacket(SyncStagePacket::class, SyncStagePacket.Serializer, 0)
    registerPacket(SyncBookPacket::class, SyncBookPacket.Serializer, 1)
    registerPacket(SyncOvenPacket::class, SyncOvenPacket.Serializer, 2)
    registerPacket(SyncTankPacket::class, SyncTankPacket.Serializer, 3)
    registerPacket(OpenCaudaInventoryPacket::class, OpenCaudaInventoryPacket.Serializer, 4)
}

@Suppress("INACCESSIBLE_TYPE")
private fun<T : Packet> registerPacket(packetClass : KClass<T>, handler : Packet.Serializer<T>, id : Int)
{
    CHANNEL.registerMessage<T>(id, packetClass.java, handler::encode, handler::decode)
    { packet: T, context: Supplier<NetworkEvent.Context> ->
        val ctx: NetworkEvent.Context = context.get()
        if (ctx.direction.receptionSide == LogicalSide.CLIENT)
            packet.processClient(clientPlayer)
        else
            packet.processServer(ctx.sender)
        ctx.packetHandled = true
    }
}

abstract class Packet
{
    @OnlyIn(Dist.CLIENT)
    open fun processClient(player: ClientPlayerEntity?) {}

    @OnlyIn(Dist.DEDICATED_SERVER)
    open fun processServer(player: ServerPlayerEntity?) {}

    abstract class Serializer<PacketType : Packet>
    {
        abstract fun encode(packet: PacketType, buf: PacketBuffer): ByteBuf
        abstract fun decode(buf: PacketBuffer): PacketType
    }
}

fun Packet.sendToAllPlayers() = CHANNEL.send(PacketDistributor.ALL.noArg(), this)

fun Packet.sendToServer() = CHANNEL.send(PacketDistributor.SERVER.noArg(), this)

fun Packet.sendToDimension(type : DimensionType) = CHANNEL.send(PacketDistributor.DIMENSION.with { type }, this)

fun Packet.sendToPlayer(player : ServerPlayerEntity) = CHANNEL.send(PacketDistributor.PLAYER.with { player }, this)

/**
 * метод позволяет отправить наш пакет всем отслеживающим чанк
 */
fun Packet.sendToTrackingChunk(chunk: Chunk) = CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with { chunk }, this)
