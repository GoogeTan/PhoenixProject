package phoenix.enity

import net.minecraft.block.Blocks
import net.minecraft.entity.EntityType
import net.minecraft.util.DamageSource
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import phoenix.utils.LogManager
import phoenix.world.EndDimension

class EnderCrystalEntity : net.minecraft.entity.item.EnderCrystalEntity
{
    constructor(type: EntityType<out EnderCrystalEntity>, world: World) : super(type, world)
    {
        LogManager.error(this, position.toString())
    }

    constructor(world: World, x: Double, y: Double, z: Double) : super(world, x, y, z)

    override fun tick()
    {
        ++innerRotation
        if (!world.isRemote)
        {
            val blockpos = BlockPos(this)
            if (world.dimension is EndDimension && world.getBlockState(blockpos).isAir)
            {
                world.setBlockState(blockpos, Blocks.FIRE.defaultState)
            }
        }
    }

    override fun onCrystalDestroyed(source: DamageSource)
    {
        if (world.dimension is EndDimension)
        {
            (world.dimension as EndDimension).dragonFightManager?.onCrystalDestroyed(this, source)
        }
    }
}
