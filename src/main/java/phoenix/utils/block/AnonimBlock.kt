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
import java.util.function.Supplier


class AnonimBlock(properties: Properties) : Block(properties), ICustomGroup
{
    var tile: Class<TileEntity>? = null
    var group : ItemGroup = Phoenix.ASH
    constructor(properties: Properties, classIn: Class<TileEntity>) : this(properties)
    {
        tile = classIn
    }

    constructor(properties: Properties, classIn: Class<TileEntity>, itemGroup: ItemGroup) : this(properties)
    {
        tile = classIn
        group = itemGroup
    }

    constructor(properties: Properties, itemGroup: ItemGroup) : this(properties)
    {
        group = itemGroup
    }

    override fun hasTileEntity(state: BlockState): Boolean
    {
        return tile != null
    }

    override fun createTileEntity(state: BlockState, world: IBlockReader): TileEntity?
    {
        return try
        {
            tile!!.newInstance()
        }
        catch (e: Exception)
        {
            Phoenix.LOGGER.error("Can not init tile: $tile")
            null
        }
    }

    override fun getDrops(state: BlockState, builder: LootContext.Builder) = SizedArrayList.of(ItemStack(this))

    override fun getTab() = group

    companion object
    {
        fun create(properties: Properties)                               = Supplier { AnonimBlock(properties) }
        fun create(material: Material)                                   = Supplier { AnonimBlock(Properties.create(material)) }
        fun create(material: Material, entityClass: Class<TileEntity>)   = Supplier { AnonimBlock(Properties.create(material), entityClass) }
        fun create(properties: Properties, classIn: Class<TileEntity>)   = Supplier { AnonimBlock(properties, classIn) }
        fun create(material: Material, entityClass: Class<TileEntity>, itemGroup: ItemGroup)   = Supplier { AnonimBlock(Properties.create(material), entityClass, itemGroup) }
        fun create(properties: Properties, itemGroup: ItemGroup)   = Supplier { AnonimBlock(properties, itemGroup) }
    }
}
