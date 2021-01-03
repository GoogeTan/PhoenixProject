package phoenix.blocks.redo

import net.minecraft.block.AbstractGlassBlock
import net.minecraft.block.BlockState
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.util.math.shapes.VoxelShapes
import net.minecraft.world.IBlockReader
import net.minecraft.world.storage.loot.LootContext
import net.minecraftforge.common.ToolType
import phoenix.Phoenix
import phoenix.init.PhoenixBlocks
import phoenix.utils.block.ICustomGroup

object ArmoredGlassBlock : AbstractGlassBlock(
    Properties.create(Material.GLASS).doesNotBlockMovement().hardnessAndResistance(20.0f).harvestLevel(3).harvestTool(
        ToolType.PICKAXE).sound(SoundType.GLASS)), ICustomGroup
{
    override fun getTab() = Phoenix.REDO
    override fun getShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape = VoxelShapes.fullCube()
    override fun getCollisionShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape =  VoxelShapes.fullCube();

    override fun getDrops(state: BlockState, builder: LootContext.Builder): MutableList<ItemStack>
    {
        val res = ArrayList<ItemStack>()
        res.add(ItemStack(PhoenixBlocks.ARMORED_GLASS.get()));
        return res
    }
}