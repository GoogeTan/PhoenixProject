package phoenix.enity

import net.minecraft.block.Blocks
import net.minecraft.entity.EntityType
import net.minecraft.util.DamageSource
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import phoenix.world.EndDimension

class EnderCrystalEntity(type: EntityType<out EnderCrystalEntity>, world: World) : net.minecraft.entity.item.EnderCrystalEntity(
    type,
    world
)
{
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
            val enddimension = world.dimension as net.minecraft.world.dimension.EndDimension
            val dragonfightmanager = enddimension.getDragonFightManager()
            dragonfightmanager?.onCrystalDestroyed(this, source)
        }
    }
}
