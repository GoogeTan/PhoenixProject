package phoenix.blocks.redo

import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ActionResultType
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.world.IBlockReader
import net.minecraft.world.World
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.fluids.FluidActionResult
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.fluids.capability.IFluidHandler
import phoenix.tile.FluidTileSidable
import phoenix.tile.redo.JuicerTile
import phoenix.utils.block.BlockWithTile
import phoenix.utils.block.IRedoThink
import phoenix.utils.interactWithFluidHandler

object JuicerBlock : BlockWithTile(
    Properties.create(Material.ROCK).lightValue(5).notSolid().hardnessAndResistance(3.0f)
), IRedoThink
{
    override fun createTileEntity(state: BlockState, world: IBlockReader): TileEntity = JuicerTile()

    override fun onBlockActivated(
        state: BlockState,
        worldIn: World,
        pos: BlockPos,
        playerIn: PlayerEntity,
        hand: Hand,
        hit: BlockRayTraceResult
    ): ActionResultType
    {
        if (super.onBlockActivated(state, worldIn, pos, playerIn, hand, hit) == ActionResultType.SUCCESS)
        {
            return ActionResultType.SUCCESS
        }

        if (hand == Hand.OFF_HAND)
        {
            return ActionResultType.PASS
        }

        val current: ItemStack = playerIn.inventory.getCurrentItem()
        val slot: Int = playerIn.inventory.currentItem

        if (!current.isEmpty)
        {
            val tile = worldIn.getTileEntity(pos)
            if (tile is FluidTileSidable)
            {
                val tank: FluidTileSidable = tile
                val holder: LazyOptional<IFluidHandler> = tank.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                if (holder.isPresent)
                {
                    val forgeResult: FluidActionResult = interactWithFluidHandler(current, holder.orElse(null), playerIn)
                    if (forgeResult.isSuccess)
                    {
                        playerIn.inventory.setInventorySlotContents(slot, forgeResult.result)
                        if (playerIn.container != null)
                        {
                            playerIn.container.detectAndSendChanges()
                        }
                        return ActionResultType.SUCCESS
                    }
                }
                return ActionResultType.PASS
            }
        }

        return ActionResultType.PASS
    }
}