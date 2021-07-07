package phoenix.network

import net.minecraft.client.entity.player.ClientPlayerEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.network.NetworkManager
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
import net.minecraftforge.fml.network.PacketDistributor.TargetPoint
import phoenix.Phoenix
import phoenix.utils.clientPlayer
import java.util.function.Supplier

object NetworkHandler
{
    private val CHANNEL = NetworkRegistry.newSimpleChannel(ResourceLocation(Phoenix.MOD_ID, "network"), { "2.0" }, { true }, { true })

    fun init()
    {
        registerPacket(SyncStagePacket())
        registerPacket(SyncBookPacket(ArrayList()))
        registerPacket(SyncOvenPacket())
        registerPacket(SyncTankPacket())
        registerPacket(OpenCaudaInventoryPacket(0))
    }

    private var id: Short = 0

    private fun registerPacket(packet: Packet) = CHANNEL.registerMessage(id++.toInt(), packet.javaClass, packet::encode, packet::decode, packet::handlePacket)

    abstract class Packet
    {
        abstract fun encode(packet: Packet, buf: PacketBuffer)
        abstract fun decode(buf: PacketBuffer): Packet

        fun handlePacket(packet: Packet, context: Supplier<NetworkEvent.Context>)
        {
            val ctx: NetworkEvent.Context = context.get()

            when (ctx.direction.receptionSide)
            {
                LogicalSide.CLIENT -> this.client(clientPlayer)
                LogicalSide.SERVER -> this.server(ctx.sender)
                null               -> this.server(ctx.sender)
            }
            ctx.packetHandled = true
        }

        @OnlyIn(Dist.CLIENT)
        open fun client(player: ClientPlayerEntity?) {}
        @OnlyIn(Dist.DEDICATED_SERVER)
        open fun server(player: ServerPlayerEntity?) {}

        /**
         * Данный метод позволяет отправить пакет всем игрокам на сервере. Пакет отсылается на КЛИЕНТ!
         * @param entity - сущность, которую нужно отправить отслеживающим
         */
        fun sendToAll() = CHANNEL.send(PacketDistributor.ALL.noArg(), this)
        /**
         * Данный метод позволяет отправить пакет на сервер
         * @param entity - сущность, которую нужно отправить отслеживающим
         */
        fun sendToServer() = CHANNEL.send(PacketDistributor.SERVER.noArg(), this)
        fun sandTo(target : PacketDistributor.PacketTarget) = CHANNEL.send(target, this)
        /**
         * Данный метод позволяет отправить пакет на всем игрокам в измерении
         * @param entity - сущность, которую нужно отправить отслеживающим
         */
        fun sendTo(type : DimensionType) = CHANNEL.send(PacketDistributor.DIMENSION.with { type }, this)
        /**
         * Данный метод позволяет отправить пакет конкретному игроку
         * @param entity - сущность, которую нужно отправить отслеживающим
         */
        fun sendTo(player : ServerPlayerEntity) = CHANNEL.send(PacketDistributor.PLAYER.with { player }, this)

        /**
         * Данный метод позволяет отправить пакет всем отслеживающим сущность. Пакет отсылается на КЛИЕНТ!
         * @param entity - сущность, которую нужно отправить отслеживающим
         */
        fun sendToTracking(entity: Entity) = CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with { entity }, this)

        /**
         * Данный метод позволяет отправить пакет всем отслеживающим сущность и игрока. Пакет отсылается на КЛИЕНТ!
         * @param entity - сущность, которую нужно отправить отслеживающим
         */
        fun sendToTrackingAndSelf(entity: Entity) = CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with { entity }, this)

        /**
         * Данный метод позволяет отправить наш пакет всем отслеживающим чанк. Пакет отсылается на КЛИЕНТ!
         * @param packet - наш пакет
         * @param chunk  - чанк, который нужно отправить отслеживающим
         */
        fun sendToTrackingChunk(chunk: Chunk) = CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with { chunk }, this)
    }
}