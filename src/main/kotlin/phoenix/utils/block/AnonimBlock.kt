package phoenix.utils.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.IBlockReader
import net.minecraft.world.storage.loot.LootContext
import phoenix.Phoenix
import phoenix.utils.SizedArrayList

class AnonimBlock(properties: Properties, val tile : () -> TileEntity? = {null}, val itemGroup : ItemGroup = Phoenix.ASH) : Block(properties), ICustomGroup
{
    override fun hasTileEntity(state: BlockState): Boolean = tile.invoke() != null

    override fun createTileEntity(state: BlockState, world: IBlockReader): TileEntity? = tile.invoke()

    override fun getDrops(state: BlockState, builder: LootContext.Builder) = SizedArrayList.of(ItemStack(this))

    override val tab: ItemGroup = itemGroup

    companion object
    {
        fun create(properties: Properties)                              = AnonimBlock(properties)
        fun create(material: Material)                                  = create(Properties.create(material))
        fun create(material: Material, tile: () -> TileEntity)   = AnonimBlock(Properties.create(material), tile)
        fun create(properties: Properties, tile: () -> TileEntity)   = AnonimBlock(properties, tile)
        fun create(material: Material, tile: () -> TileEntity, itemGroup: ItemGroup)  = AnonimBlock(Properties.create(material), tile, itemGroup)
        fun create(properties: Properties, itemGroup: ItemGroup)   =  AnonimBlock(properties, itemGroup = itemGroup)
    }
}
