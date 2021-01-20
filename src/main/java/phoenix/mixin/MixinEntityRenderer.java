package phoenix.mixin;

import net.minecraft.nbt.CompoundNBT;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


public class MixinEntityRenderer
{

    public void onWriteEntityToNBT(CompoundNBT nbt, CallbackInfo ci)
    {
    }
}
