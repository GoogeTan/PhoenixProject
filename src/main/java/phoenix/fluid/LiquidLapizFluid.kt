package phoenix.fluid

import net.minecraft.util.ResourceLocation
import net.minecraftforge.fluids.FluidAttributes
import net.minecraftforge.fluids.ForgeFlowingFluid
import phoenix.init.PhoenixFluids


class LiquidLapizFlowingFluid : ForgeFlowingFluid.Flowing(Properties(PhoenixFluids.LAPIZ_SOURCE, PhoenixFluids.LAPIZ_FLOWING,
    FluidAttributes.builder(ResourceLocation(""), ResourceLocation(""))
        .translationKey("fluid.phoenix.water")))

class LiquidLapizSourceFluid : ForgeFlowingFluid.Source(Properties(PhoenixFluids.LAPIZ_SOURCE, PhoenixFluids.LAPIZ_FLOWING,
    FluidAttributes.builder(ResourceLocation(""), ResourceLocation(""))))