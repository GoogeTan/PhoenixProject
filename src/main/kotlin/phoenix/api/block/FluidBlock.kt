package phoenix.api.block

import net.minecraft.block.FlowingFluidBlock
import net.minecraft.block.material.Material
import net.minecraft.fluid.FlowingFluid

class FluidBlock(fluidSource : () -> FlowingFluid) : FlowingFluidBlock(fluidSource, Properties.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0f).noDrops().notSolid()), INonTab
{

}