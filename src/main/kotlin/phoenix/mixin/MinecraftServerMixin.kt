package phoenix.mixin

import com.mojang.authlib.GameProfileRepository
import com.mojang.authlib.minecraft.MinecraftSessionService
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
import net.minecraft.command.Commands
import net.minecraft.server.MinecraftServer
import net.minecraft.server.management.PlayerProfileCache
import net.minecraft.world.chunk.listener.IChunkStatusListenerFactory
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.io.File
import java.net.Proxy

var serverInstance : MinecraftServer? = null

@Mixin(MinecraftServer::class)
class MinecraftServerMixin
{
    private companion object
    {
        @JvmStatic
        @Inject(method = ["<init>"], at = [At("TAIL")])
        fun init(
            anvilFileIn: File?,
            serverProxyIn: Proxy?,
            commandManagerIn: Commands?,
            authentication: YggdrasilAuthenticationService?,
            sessionServiceIn: MinecraftSessionService?,
            profileRepository: GameProfileRepository?,
            profileCache: PlayerProfileCache?,
            chunkStatusListenerFactoryIn: IChunkStatusListenerFactory?,
            folderNameIn: String?,
            callbackInfo: CallbackInfo?
        )
        {
            serverInstance = this as MinecraftServer
        }
    }
}