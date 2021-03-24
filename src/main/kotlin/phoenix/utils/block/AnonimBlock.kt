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
import phoenix.utils.LogManager
import phoenix.utils.SizedArrayList
import java.util.function.Supplier

class AnonimBlock(properties: Properties, val tile : () -> TileEntity? = {null}, val itemGroup : ItemGroup = Phoenix.ASH) : Block(properties), ICustomGroup
{
    override fun hasTileEntity(state: BlockState): Boolean = tile.invoke() != null

    override fun createTileEntity(state: BlockState, world: IBlockReader): TileEntity?
    {
        val res = tile.invoke()
        if (res == null)
            LogManager.error(this, "Can not init tile: $tile")
        return res
    }

    override fun getDrops(state: BlockState, builder: LootContext.Builder) = SizedArrayList.of(ItemStack(this))

    override val tab: ItemGroup
        get() = itemGroup

    companion object
    {
        fun create(properties: Properties)                               = Supplier { AnonimBlock(properties) }
        fun create(material: Material)                                   = Supplier { AnonimBlock(Properties.create(material)) }
        fun create(material: Material, entityClass: () -> TileEntity)   = Supplier { AnonimBlock(Properties.create(material), entityClass) }
        fun create(properties: Properties, classIn: () -> TileEntity)   = Supplier { AnonimBlock(properties, classIn) }
        fun create(material: Material, entityClass: () -> TileEntity, itemGroup: ItemGroup)   = AnonimBlock(Properties.create(material), entityClass, itemGroup)
        fun create(properties: Properties, itemGroup: ItemGroup)   = Supplier { AnonimBlock(properties, itemGroup = itemGroup) }
    }
}
