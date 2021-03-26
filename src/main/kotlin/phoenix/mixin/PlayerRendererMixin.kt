package phoenix.mixin

import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.entity.player.AbstractClientPlayerEntity
import net.minecraft.client.renderer.entity.PlayerRenderer
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import phoenix.utils.IPhoenixPlayer

@Mixin(PlayerRenderer::class)
class PlayerRendererMixin
{
    @Inject(at = [At("HEAD")], method=["applyRotations"], cancellable = true)
    fun applyRotations(entityLiving : AbstractClientPlayerEntity, matrixStackIn : MatrixStack, ageInTicks : Float, rotationYaw : Float, partialTicks : Float, ci : CallbackInfoReturnable<Void>)
    {
        if(entityLiving is IPhoenixPlayer)
        {
            if(entityLiving.isOnCauda())
            {
                ci.cancel()
            }
        }
    }
}