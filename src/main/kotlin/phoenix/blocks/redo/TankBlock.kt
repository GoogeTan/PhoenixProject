package phoenix.blocks.redo

import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BucketItem
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ActionResultType
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.util.text.StringTextComponent
import net.minecraft.world.IBlockReader
import net.minecraft.world.World
import net.minecraftforge.fluids.FluidAttributes
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.FluidUtil
import net.minecraftforge.fluids.capability.IFluidHandler
import phoenix.network.NetworkHandler
import phoenix.network.SyncFluidThinkPacket
import phoenix.tile.redo.TankTile
import phoenix.utils.block.BlockWithTile
import phoenix.utils.block.IRedoThink
import phoenix.utils.getFluidContainedInHand
import phoenix.utils.getTileAt

object TankBlock : BlockWithTile(Properties.create(Material.ROCK).lightValue(5).notSolid().hardnessAndResistance(3.0f)), IRedoThink
{
    override fun onBlockActivated(
        state: BlockState,
        worldIn: World,
        pos: BlockPos,
        player: PlayerEntity,
        handIn: Hand,
        hit: BlockRayTraceResult
    ): ActionResultType
    {
        if(!worldIn.isRemote)
        {
            val tileTank = worldIn.getTileAt<TankTile>(pos)
            if(tileTank != null)
            {
                val old = tileTank.tank.fluid.amount
                FluidUtil.interactWithFluidHandler(player, handIn, worldIn, pos, hit.face)
                if(old != tileTank.tank.fluid.amount)
                    NetworkHandler.sendToAll(SyncFluidThinkPacket(tileTank))
                return ActionResultType.SUCCESS
            }
        }

        return ActionResultType.CONSUME
    }

    override fun createTileEntity(state: BlockState, world: IBlockReader): TileEntity = TankTile()

    override fun getRenderType(state: BlockState): BlockRenderType = BlockRenderType.ENTITYBLOCK_ANIMATED
}
