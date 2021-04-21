package phoenix.init

import net.minecraftforge.registries.ForgeRegistries
import phoenix.Phoenix
import phoenix.fluids.SetaJuiceFluid
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.MOD_BUS

object PhxFluids
{
    val fluids = KDeferredRegister(ForgeRegistries.FLUIDS, Phoenix.MOD_ID)

    val seta_juice_source  by  fluids.register("seta_juice_source")  { SetaJuiceFluid.Source }
    val seta_juice_flowing by  fluids.register("seta_juice_flowing") { SetaJuiceFluid.Flowing }

    fun register() = fluids.register(MOD_BUS)
}