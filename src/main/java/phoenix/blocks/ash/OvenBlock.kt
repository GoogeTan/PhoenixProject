package phoenix.blocks.ash

import net.minecraft.block.BlockState
import net.minecraft.block.ContainerBlock
import net.minecraft.block.material.Material
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.container.INamedContainerProvider
import net.minecraft.state.BooleanProperty
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ActionResultType
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.world.IBlockReader
import net.minecraft.world.World
import phoenix.tile.ash.OvenTile
import javax.annotation.ParametersAreNonnullByDefault

class OvenBlock : ContainerBlock(Properties.create(Material.ROCK))
{
    val buckets = arrayOfNulls<BooleanProperty>(4)
    /*
    init
    {
        for (i in 0..buckets.size)
            buckets[i] = BooleanProperty.create("hasbucket$i")
        val state = stateContainer.baseState
        for (property in buckets) state.with(property, false)
        defaultState = state
    }
    */
    override fun onBlockActivated(state: BlockState?, worldIn: World, pos: BlockPos?, playerIn: PlayerEntity, handIn: Hand?, hit: BlockRayTraceResult?): ActionResultType?
    {
        if (!worldIn.isRemote)
        {
            val tile = worldIn.getTileEntity(pos)
            if (tile is OvenTile)
            {
                playerIn.openContainer(tile as INamedContainerProvider?)
            }
        }
        return ActionResultType.SUCCESS
    }

    /*
    override fun fillStateContainer(builder: StateContainer.Builder<Block?, BlockState?>)
    {
        for (bucket in buckets) builder.add(bucket)
        super.fillStateContainer(builder)
    }
    */
    override fun createTileEntity(state: BlockState?, world: IBlockReader?): TileEntity?
    {
        return OvenTile()
    }

    @ParametersAreNonnullByDefault
    override fun createNewTileEntity(worldIn: IBlockReader?): TileEntity?
    {
        return OvenTile()
    }
}