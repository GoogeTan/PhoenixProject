package phoenix.blocks.redo

import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.block.material.MaterialColor
import net.minecraft.client.renderer.color.IBlockColor
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.item.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockReader
import net.minecraft.world.ILightReader
import net.minecraft.world.IWorld
import net.minecraft.world.World
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.event.world.BlockEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import phoenix.tile.redo.CeramicTile
import phoenix.utils.block.BlockWithTile
import phoenix.utils.block.IColoredBlock
import phoenix.utils.block.IRedoThink
import phoenix.utils.changeIfEqual
import phoenix.utils.clientWorld
import phoenix.utils.getTileAt

@Mod.EventBusSubscriber
object CeramicBlock : BlockWithTile(Properties.create(Material.CLAY)), IRedoThink, IColoredBlock
{
    override fun createTileEntity(state: BlockState, world: IBlockReader): TileEntity = CeramicTile ()

    @SubscribeEvent
    fun onDrops(event: BlockEvent.BreakEvent)
    {
        val res = ItemStack(CeramicBlock)
        val tile = event.world.getTileAt<CeramicTile>(event.pos)
        val pos = event.pos
        val world : World = event.player.world
        if (tile != null)
        {
            res.orCreateTag.put("tile", tile.write(CompoundNBT()))
        }
        world.addEntity(ItemEntity(world, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), res))
    }

    override fun onPlayerDestroy(worldIn: IWorld, pos: BlockPos, state: BlockState)
    {
        super.onPlayerDestroy(worldIn, pos, state)
    }

    override fun onBlockPlacedBy(
        worldIn: World,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        stack: ItemStack
    )
    {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack)
        val tile = worldIn.getTileAt<CeramicTile>(pos)
        tile?.deserializeNBT(stack.orCreateTag.getCompound("tile"))
    }

    val red = MaterialColor.RED_TERRACOTTA.colorValue

    /*
    override fun getBlockColor() = IBlockColor { state : BlockState, light : ILightReader?, pos : BlockPos?, tint : Int ->  if (pos != null) clientWorld?.getTileAt<CeramicTile>(pos)?.colour ?: red else red }

    override fun getItemColor(): IItemColor = IItemColor { state : ItemStack, tint : Int -> state.orCreateTag.getCompound("tile").getInt("colour").changeIfEqual(0, red) }
    */

    override fun getBlockColor() = IBlockColor { _: BlockState, _: ILightReader?, _: BlockPos?, _: Int -> Material.WATER.color.colorValue }
    override fun getItemColor() = IItemColor { _: ItemStack, _: Int -> Material.WATER.color.colorValue }

    override fun getItem(worldIn: IBlockReader, pos: BlockPos, state: BlockState): ItemStack
    {
        val res = ItemStack(CeramicBlock)
        val tile = worldIn.getTileEntity(pos)
        if (tile != null)
        {
            res.orCreateTag.put("tile", tile.write(CompoundNBT()))
        }
        return res
    }
}