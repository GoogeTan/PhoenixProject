package phoenix.enity.tasks

import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import phoenix.init.PhoenixBlocks
import phoenix.utils.BlockPosUtils
import phoenix.utils.entity.AbstractFlyingEntity
import phoenix.utils.entity.ThreeDimensionsMovingGoal

class EatingChorusGoal(entityIn: AbstractFlyingEntity?) : ThreeDimensionsMovingGoal(entityIn)
{
    var distance = 10.0
    override fun shouldExecute(): Boolean
    {
        for (current in BlockPos.getAllInBoxMutable(
                MathHelper.floor(entity.posX - distance),
                MathHelper.floor(entity.posY - distance),
                MathHelper.floor(entity.posZ - distance),
                MathHelper.floor(entity.posX + distance),
                MathHelper.floor(entity.posY + distance),
                MathHelper.floor(entity.posZ + distance)
        ))
        {
            if (entity.world.getBlockState(current).block === Blocks.CHORUS_FLOWER || entity.world.getBlockState(current).block === PhoenixBlocks.FERTILE_END_STONE.get())
            {
                entity.orbitPosition = current
                entity.orbitPosition = BlockPos(entity)
                entity.orbitOffset = Vec3d(entity.orbitPosition).add(current.x * MathHelper.cos(10f).toDouble(), current.y.toDouble(), current.z * MathHelper.sin(10f).toDouble())
                return true
            }
        }
        return false
    }

    override fun tick()
    {
        super.tick()
        if (BlockPosUtils.distanceTo(entity.orbitPosition, entity.position) <= 1)
        {
            entity.world.setBlockState(entity.orbitPosition, Blocks.AIR.defaultState)
        }
    }
}
