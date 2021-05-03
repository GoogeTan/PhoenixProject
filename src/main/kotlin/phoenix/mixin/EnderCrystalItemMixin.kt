package phoenix.mixin

import net.minecraft.block.Blocks
import net.minecraft.item.EnderCrystalItem
import net.minecraft.item.ItemUseContext
import net.minecraft.util.ActionResultType
import net.minecraft.util.math.AxisAlignedBB
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import phoenix.enity.EnderCrystalEntity
import phoenix.world.EndDimension

@Mixin(EnderCrystalItem::class)
class EnderCrystalItemMixin
{
    @Inject(at = [At("HEAD")], method=["onItemUse"], cancellable = true)
    fun onItemUse(context: ItemUseContext, ci : CallbackInfoReturnable<ActionResultType>)
    {
        val world = context.world
        val blockpos = context.pos
        val blockstate = world.getBlockState(blockpos)
        ci.returnValue =  if (blockstate.block !== Blocks.OBSIDIAN && blockstate.block !== Blocks.BEDROCK)
        {
            ActionResultType.FAIL
        } else
        {
            val blockpos1 = blockpos.up()
            if (!world.isAirBlock(blockpos1))
            {
                ActionResultType.FAIL
            } else
            {
                val d0 = blockpos1.x.toDouble()
                val d1 = blockpos1.y.toDouble()
                val d2 = blockpos1.z.toDouble()
                val list = world.getEntitiesWithinAABBExcludingEntity(null, AxisAlignedBB(d0, d1, d2, d0 + 1.0, d1 + 2.0, d2 + 1.0))
                if (list.isNotEmpty())
                {
                    ActionResultType.FAIL
                } else
                {
                    if (!world.isRemote)
                    {
                        val crystal = EnderCrystalEntity(world, d0 + 0.5, d1, d2 + 0.5)
                        crystal.setShowBottom(false)
                        world.addEntity(crystal)
                        (world.dimension as? EndDimension)?.dragonFightManager?.tryRespawnDragon()
                    }
                    context.item.shrink(1)
                    ActionResultType.SUCCESS
                }
            }
        }
        ci.cancel()
    }
}