package phoenix.init.events

import net.minecraft.particles.ParticleTypes
import net.minecraft.world.dimension.DimensionType
import net.minecraft.world.server.ServerWorld
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import phoenix.network.NetworkHandler
import phoenix.network.SyncStagePacket
import phoenix.utils.LogManager
import phoenix.utils.capablity.IChapterReader
import phoenix.utils.capablity.PlayerChapterReader
import phoenix.utils.capablity.SaveHandler
import phoenix.world.StageManager
import java.lang.Exception

@Mod.EventBusSubscriber
object PhoenixEvents
{
    @JvmStatic
    @SubscribeEvent
    fun onPlay(event: PlayerEvent.Clone)
    {
        val world = event.player.world as ServerWorld
        val player = event.player

        world.spawnParticle(ParticleTypes.BUBBLE, player.posX, player.posY, player.posZ, 5, 0.0, 1.0, 0.0, 1.0)
        world.spawnParticle(ParticleTypes.BUBBLE, player.posX, player.posY, player.posZ, 5, 0.0, 1.0, 0.0, 1.0)
    }

    @JvmStatic
    @SubscribeEvent
    fun onSave(event: WorldEvent.Save)
    {
        if(!event.world.isRemote)
        {
            LogManager.log(this, "Phoenix is starting saving")
            val nbt = event.world.worldInfo.getDimensionData(DimensionType.THE_END)
            StageManager.write(nbt)
            LogManager.log(this, "${StageManager.getStage()} ${StageManager.getPart()}")
            event.world.worldInfo.setDimensionData(DimensionType.THE_END, nbt)
            LogManager.log(this, "Phoenix has ended saving")
        }
    }

    @JvmStatic
    @SubscribeEvent
    fun onLoad(event: WorldEvent.Load)
    {
        if(!event.world.isRemote)
        {
            LogManager.log(this, "Phoenix is starting loading")
            val nbt = event.world.worldInfo.getDimensionData(DimensionType.THE_END)
            StageManager.read(nbt)
            LogManager.log(this, "${StageManager.getStage()} ${StageManager.getPart()}")
            NetworkHandler.sendToAll(SyncStagePacket(StageManager.getStage(), StageManager.getPart()))
            LogManager.log(this, "Phoenix has ended loading")
        }
    }

    //@JvmStatic
    //@SubscribeEvent
    fun onJoin(event: CapabilityManager)
    {
        try
        {
            event.register(IChapterReader::class.java, SaveHandler(), ::PlayerChapterReader)
        }
        catch (e : Exception)
        {
            LogManager.error(this, e)
        }
    }
}