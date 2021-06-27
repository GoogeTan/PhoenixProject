package phoenix.utils

import net.minecraft.client.world.ClientWorld
import net.minecraft.world.World
import net.minecraft.world.server.ServerWorld
import net.minecraftforge.eventbus.api.Event
import phoenix.world.StageManager

sealed class ChangeStageEvent<T : World>(val world: T) : Event()
{
    val stage : Int
        get() = StageManager.stage
    val part : Int
        get() = StageManager.part
}

open class ClientChangeStageEvent(world: ClientWorld) : ChangeStageEvent<ClientWorld>(world)
open class ServerChangeStageEvent(world: ServerWorld) : ChangeStageEvent<ServerWorld>(world)

class ClientStageUppedEvent(world: ClientWorld) : ClientChangeStageEvent(world)
class ServerStageUppedEvent(world: ServerWorld) : ServerChangeStageEvent(world)