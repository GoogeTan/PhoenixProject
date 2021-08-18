
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
import phoenix.MOD_ID
import phoenix.Phoenix
import phoenix.other.clientPlayer
import java.util.function.Supplier
import kotlin.reflect.KClass

private val CHANNEL = NetworkRegistry.newSimpleChannel(ResourceLocation(MOD_ID, "network"), { "2.0" }, { true }, { true })

fun initPacketSystem()
{
    registerPacket(SyncStagePacket.Serializer)
    registerPacket(SyncBookPacket.Serializer)
    registerPacket(SyncOvenPacket.Serializer)
    registerPacket(SyncTankPacket.Serializer)
    registerPacket(OpenCaudaInventoryPacket.Serializer)
    registerPacket(OpenDiaryPacket.Serializer)
}

private var id = 0

@Suppress("INACCESSIBLE_TYPE")
private inline fun<reified T : Packet> registerPacket(handler : Packet.Serializer<T>)
{
    CHANNEL.registerMessage(id, T::class.java, handler::encode, handler::decode)
    { packet: T, context: Supplier<NetworkEvent.Context> ->
        val ctx: NetworkEvent.Context = context.get()
        if (ctx.direction.receptionSide == LogicalSide.CLIENT)
            packet.processClient(clientPlayer)
        else
            packet.processServer(ctx.sender)
        ctx.packetHandled = true
    }
    ++id
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
