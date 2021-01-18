package phoenix.init

import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import phoenix.Phoenix
import phoenix.fluid.LiquidLapizFlowingFluid
import phoenix.fluid.LiquidLapizSourceFluid

object PhoenixFluids
{
    @JvmStatic val FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Phoenix.MOD_ID)

    @JvmStatic val LAPIZ_FLOWING = FLUIDS.register("lapiz_flowing", ::LiquidLapizFlowingFluid)
    @JvmStatic val LAPIZ_SOURCE  = FLUIDS.register("lapiz_source",  ::LiquidLapizSourceFluid)

    fun register()
    {
        FLUIDS.register(FMLJavaModLoadingContext.get().modEventBus)
    }
}