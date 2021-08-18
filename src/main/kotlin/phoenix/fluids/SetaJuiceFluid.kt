package phoenix.fluids

import net.minecraft.fluid.Fluid
import net.minecraft.fluid.IFluidState
import net.minecraft.state.StateContainer
import net.minecraft.util.ResourceLocation
import net.minecraft.world.IWorldReader
import net.minecraftforge.fluids.FluidAttributes
import net.minecraftforge.fluids.ForgeFlowingFluid
import phoenix.MOD_ID
import phoenix.Phoenix
import phoenix.init.PhxBlocks
import phoenix.init.PhxFluids
import phoenix.init.PhxItems

sealed class SetaJuiceFluid(properties: Properties) : ForgeFlowingFluid(properties)
{
    private companion object
    {
        fun makeAttributes(): FluidAttributes.Builder
        {
            val still = ResourceLocation(MOD_ID, "fluid/seta_juice_still")
            val flowing = ResourceLocation(MOD_ID, "fluid/seta_juice_flow")
            return FluidAttributes.builder(still, flowing)
                .density(3000)
                .viscosity(1200)
                .color(0x72727272)
                .luminosity(2)
        }
    }

    override fun getLevelDecreasePerBlock(worldIn: IWorldReader?): Int = 2

    object Flowing : SetaJuiceFluid(Properties(PhxFluids::seta_juice_source, PhxFluids::seta_juice_flowing, makeAttributes()).block(PhxBlocks::setaJuice).bucket(PhxItems::SETA_JUICE_BUCKET))
    {
        override fun fillStateContainer(builder: StateContainer.Builder<Fluid?, IFluidState?>)
        {
            super.fillStateContainer(builder)
            builder.add(LEVEL_1_8)
        }

        override fun getLevel(state: IFluidState): Int = state.get(LEVEL_1_8)
        override fun isSource(state: IFluidState?): Boolean = false

        init { defaultState = getStateContainer().baseState.with(LEVEL_1_8, 7) }
    }

    object Source : SetaJuiceFluid(Properties(PhxFluids::seta_juice_source, PhxFluids::seta_juice_flowing, makeAttributes()).block(PhxBlocks::setaJuice).bucket(PhxItems::SETA_JUICE_BUCKET))
    {
        override fun getLevel(state: IFluidState?): Int = 8
        override fun isSource(state: IFluidState?): Boolean = true
    }
}