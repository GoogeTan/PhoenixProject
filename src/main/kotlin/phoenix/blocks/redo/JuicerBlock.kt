package phoenix.blocks.redo

import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BucketItem
import net.minecraft.item.Items
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ActionResultType
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.world.IBlockReader
import net.minecraft.world.World
import net.minecraftforge.fluids.FluidAttributes
import net.minecraftforge.fluids.capability.IFluidHandler
import phoenix.tile.redo.JuicerTile
import phoenix.utils.block.BlockWithTile
import phoenix.utils.block.IRedoThink
import phoenix.utils.getFluidContained
import phoenix.utils.getTileAt

object JuicerBlock : BlockWithTile(Properties.create(Material.ROCK).lightValue(5).notSolid().hardnessAndResistance(3.0f)), IRedoThink
{
    override fun createTileEntity(state: BlockState, world: IBlockReader): TileEntity = JuicerTile()

    override fun onBlockActivated(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        raytrace: BlockRayTraceResult
    ): ActionResultType
    {
        if(!world.isRemote)
        {
            val stack = player.getHeldItem(hand)
            val tile = world.getTileAt<JuicerTile>(pos) ?: return super.onBlockActivated(state, world, pos, player, hand, raytrace)
            when
            {
                !stack.getFluidContained().isEmpty && tile.tank.isEmpty && tile.tank.capacity - tile.tank.fluidAmount >= FluidAttributes.BUCKET_VOLUME ->
                {
                    tile.tank.fill(stack.getFluidContained(), IFluidHandler.FluidAction.EXECUTE)
                }
                stack.item === Items.BUCKET && tile.tank.fluid.amount >= FluidAttributes.BUCKET_VOLUME ->
                {

                }
            }
        }

        return super.onBlockActivated(state, world, pos, player, hand, raytrace)
    }
}