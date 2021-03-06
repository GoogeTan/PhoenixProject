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
import phoenix.api.block.IRedoThink
import phoenix.init.PhxBlocks

object ArmoredGlassBlock : AbstractGlassBlock(
    Properties.create(Material.GLASS).doesNotBlockMovement().hardnessAndResistance(20.0f).harvestLevel(3).harvestTool(
        ToolType.PICKAXE).sound(SoundType.GLASS).notSolid()), IRedoThink
{
    override fun getDrops(state: BlockState, builder: LootContext.Builder): List<ItemStack> = listOf(ItemStack(PhxBlocks.armoredGlass))

    override fun getCollisionShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape = VoxelShapes.fullCube()

    override fun getShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape = VoxelShapes.fullCube()
}