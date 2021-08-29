package phoenix.blocks.ash

import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockReader
import net.minecraft.world.World
import net.minecraft.world.dimension.DimensionType
import net.minecraft.world.server.ServerWorld
import phoenix.api.block.BlockWithTile
import phoenix.other.set
import phoenix.tile.ash.HandMillTile
import phoenix.world.structures.bunker.BunkerPieces
import phoenix.world.structures.bunker.BunkerProperties

class HandMillBlock(properties: Properties) : BlockWithTile(properties)
{
    override fun createTileEntity(state: BlockState, world: IBlockReader): TileEntity = HandMillTile()

    override fun onBlockAdded(
        state: BlockState,
        world: World,
        pos: BlockPos,
        state2: BlockState,
        bool : Boolean
    ) {
        super.onBlockAdded(state, world, pos, state2, bool)
        if (world.dimension.type == DimensionType.THE_END && !world.isRemote)
        {
            world[pos] = Blocks.AIR.defaultState
            BunkerPieces.Enternace.placeRecursive((world as ServerWorld), pos, BunkerProperties(2, 64, false), Direction.DOWN)
        }
    }
}