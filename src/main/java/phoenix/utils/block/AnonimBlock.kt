package phoenix.utils.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.IBlockReader
import phoenix.Phoenix
import java.util.function.Supplier


class AnonimBlock(properties: Properties) : Block(properties)
{
    var tile: Class<TileEntity>? = null

    constructor(properties: Properties, classIn: Class<TileEntity>) : this(properties)
    {
        tile = classIn
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

    companion object
    {
        fun create(properties: Properties)                               = Supplier { AnonimBlock(properties) }
        fun create(material: Material)                                   = Supplier { AnonimBlock(Properties.create(material)) }
        fun create(material: Material, entityClass: Class<TileEntity>)   = Supplier { AnonimBlock(Properties.create(material), entityClass) }
        fun create(properties: Properties, classIn: Class<TileEntity>)   = Supplier { AnonimBlock(properties, classIn) }
    }
}
