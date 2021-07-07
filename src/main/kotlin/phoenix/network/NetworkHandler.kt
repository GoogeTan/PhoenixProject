package phoenix.network

import io.netty.buffer.ByteBuf
import net.minecraft.client.entity.player.ClientPlayerEntity
import net.minecraft.entity.Entity
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
import phoenix.utils.clientPlayer
import java.util.function.Supplier
import kotlin.reflect.KClass

object NetworkHandler
{
    private val CHANNEL = NetworkRegistry.newSimpleChannel(ResourceLocation(Phoenix.MOD_ID, "network"), { "2.0" }, { true }, { true })

    fun init()
    {
        var id = 0
        registerPacket(SyncStagePacket::class, SyncStagePacket.Serializer, id++)
        registerPacket(SyncBookPacket::class, SyncBookPacket.Serializer, id++)
        registerPacket(SyncOvenPacket::class, SyncOvenPacket.Serializer, id++)
        registerPacket(SyncTankPacket::class, SyncTankPacket.Serializer, id++)
        registerPacket(OpenCaudaInventoryPacket::class, OpenCaudaInventoryPacket.Serializer, id++)
    }

    private fun<T : Packet> registerPacket(packet: KClass<T>, hangler : Packet.Serializer<T>, id : Int = 0) = CHANNEL.registerMessage(id, packet.java, hangler::encode, hangler::decode, hangler::handlePacket)

    abstract class Packet
    {
        @OnlyIn(Dist.CLIENT)
        open fun client(player: ClientPlayerEntity?) {}
        @OnlyIn(Dist.DEDICATED_SERVER)
        open fun server(player: ServerPlayerEntity?) {}

        /**
         * Данный метод позволяет отправить пакет всем игрокам на сервере
         * @param entity - сущность, которую нужно отправить отслеживающим
         */
        @OnlyIn(Dist.DEDICATED_SERVER)
        fun sendToAll() = CHANNEL.send(PacketDistributor.ALL.noArg(), this)

        /**
         * Данный метод позволяет отправить пакет на сервер
         * @param entity - сущность, которую нужно отправить отслеживающим
         */
        @OnlyIn(Dist.CLIENT)
        fun sendToServer() = CHANNEL.send(PacketDistributor.SERVER.noArg(), this)

        /**
         * Данный метод позволяет отправить пакет на всем игрокам в измерении
         * @param entity - сущность, которую нужно отправить отслеживающим
         */
        @OnlyIn(Dist.DEDICATED_SERVER)
        fun sendTo(type : DimensionType) = CHANNEL.send(PacketDistributor.DIMENSION.with { type }, this)

        /**
         * Данный метод позволяет отправить пакет конкретному игроку
         * @param entity - сущность, которую нужно отправить отслеживающим
         */
        @OnlyIn(Dist.DEDICATED_SERVER)
        fun sendTo(player : ServerPlayerEntity) = CHANNEL.send(PacketDistributor.PLAYER.with { player }, this)

        /**
         * Данный метод позволяет отправить пакет всем отслеживающим сущность
         * @param entity - сущность, которую нужно отправить отслеживающим
         */
        @OnlyIn(Dist.DEDICATED_SERVER)
        fun sendToTracking(entity: Entity) = CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with { entity }, this)

        /**
         * Данный метод позволяет отправить пакет всем отслеживающим сущность и игрока
         * @param entity - сущность, которую нужно отправить отслеживающим
         */
        @OnlyIn(Dist.DEDICATED_SERVER)
        fun sendToTrackingAndSelf(entity: Entity) = CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with { entity }, this)

        /**
         * Данный метод позволяет отправить наш пакет всем отслеживающим чанк
         * @param chunk  - чанк, который нужно отправить отслеживающим
         */
        @OnlyIn(Dist.DEDICATED_SERVER)
        fun sendToTrackingChunk(chunk: Chunk) = CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with { chunk }, this)

        abstract class Serializer<PacketType : Packet>
        {
            abstract fun encode(packet: PacketType, buf: PacketBuffer): ByteBuf
            abstract fun decode(buf: PacketBuffer): PacketType

            fun handlePacket(packet: PacketType, context: Supplier<NetworkEvent.Context>)
            {
                val ctx: NetworkEvent.Context = context.get()
                if (ctx.direction.receptionSide == LogicalSide.CLIENT)
                    packet.client(clientPlayer)
                else
                    packet.server(ctx.sender)
                ctx.packetHandled = true
            }
        }
    }
}