package phoenix.fluids

import net.minecraft.fluid.Fluid
import net.minecraft.fluid.IFluidState
import net.minecraft.state.StateContainer
import net.minecraft.util.ResourceLocation
import net.minecraft.world.IWorldReader
import net.minecraftforge.fluids.FluidAttributes
import net.minecraftforge.fluids.ForgeFlowingFluid
import phoenix.Phoenix
import phoenix.init.PhoenixBlocks
import phoenix.init.PhoenixFluids
import phoenix.init.PhoenixItems

abstract class SetaJuiceFluid(properties: Properties) : ForgeFlowingFluid(properties)
{
    private companion object
    {
        fun makeAttributes(): FluidAttributes.Builder
        {
            val still = ResourceLocation(Phoenix.MOD_ID, "fluid/seta_juice_still")
            val flowing = ResourceLocation(Phoenix.MOD_ID, "fluid/seta_juice_flow")
            return FluidAttributes.builder(still, flowing)
                .density(3000)
                .viscosity(1200)
                .color(0x72727272)
                .luminosity(2)
        }
    }

    override fun getLevelDecreasePerBlock(worldIn: IWorldReader?): Int = 2

    object Flowing : SetaJuiceFluid(Properties(PhoenixFluids::seta_juice_source, PhoenixFluids::seta_juice_flowing, makeAttributes()).block(PhoenixBlocks::SETA_JUICE).bucket(PhoenixItems::SETA_JUICE_BUCKET))
    {
        override fun fillStateContainer(builder: StateContainer.Builder<Fluid?, IFluidState?>)
        {
            super.fillStateContainer(builder)
            builder.add(LEVEL_1_8)
        }

        override fun getLevel(state: IFluidState): Int = state.get(LEVEL_1_8)

        override fun isSource(state: IFluidState?): Boolean = false

        init
        {
            defaultState = getStateContainer().baseState.with(LEVEL_1_8, 7)
        }
    }

    object Source : SetaJuiceFluid(Properties(PhoenixFluids::seta_juice_source, PhoenixFluids::seta_juice_flowing, makeAttributes()).block(PhoenixBlocks::SETA_JUICE).bucket(PhoenixItems::SETA_JUICE_BUCKET))
    {
        override fun getLevel(state: IFluidState?): Int = 8

        override fun isSource(state: IFluidState?): Boolean = true
    }
}