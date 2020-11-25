package phoenix.blocks.ash

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ContainerBlock
import net.minecraft.block.material.Material
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.container.INamedContainerProvider
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
import net.minecraftforge.common.ForgeHooks
import phoenix.Phoenix
import phoenix.recipes.OvenRecipe
import phoenix.tile.ash.OvenTile
import phoenix.utils.block.BlockWithTile
import javax.annotation.ParametersAreNonnullByDefault

class OvenBlock : BlockWithTile(Properties.create(Material.ROCK).notSolid())
{
    override fun onBlockActivated(state: BlockState?, worldIn: World, pos: BlockPos, playerIn: PlayerEntity, handIn: Hand, hit: BlockRayTraceResult): ActionResultType
    {
        if (!worldIn.isRemote && !playerIn.isSneaking)
        {
            val tile = worldIn.getTileEntity(pos) as OvenTile
            val itemstack: ItemStack = playerIn.getHeldItem(handIn)

            val list = tile.outOtherItems()
            for (i in list)
                playerIn.addItemStackToInventory(i)
            if (OvenRecipe.getRecipes_from_inputs()[itemstack.item] != null)
            {
                if(tile.addItem(itemstack))
                {
                    playerIn.setHeldItem(handIn, ItemStack.EMPTY)
                }
            }
            else if (itemstack.item != Items.LAVA_BUCKET)
            {
                tile.burnTime += ForgeHooks.getBurnTime(itemstack)
                playerIn.getHeldItem(handIn).shrink(1)
            }
            return ActionResultType.SUCCESS
        }

        return ActionResultType.PASS
    }

    override fun createTileEntity(state: BlockState?, world: IBlockReader?): TileEntity = OvenTile()
}