package phoenix.blocks

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.material.Material
import net.minecraft.client.resources.I18n
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.ActionResultType
import net.minecraft.util.Hand
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.util.text.StringTextComponent
import net.minecraft.world.World
import net.minecraft.world.dimension.DimensionType
import phoenix.init.PhxSounds
import phoenix.init.PhxTriggers
import phoenix.world.EndDimension
import phoenix.world.StageManager.addPart
import phoenix.world.StageManager.part
import phoenix.world.StageManager.stage
import javax.annotation.Nonnull

object UpdaterBlock : Block(Properties.create(Material.ROCK).lightValue(5).hardnessAndResistance(-1f))
{
    @Nonnull
    override fun onBlockActivated(state: BlockState, worldIn: World, pos: BlockPos, player: PlayerEntity, handIn: Hand, hit: BlockRayTraceResult): ActionResultType
    {
        player.ridingEntity
        if (!worldIn.isRemote && worldIn.getDimension().type === DimensionType.THE_END)
        {
            worldIn.playSound(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), PhxSounds.CHANGE_STAGE, SoundCategory.BLOCKS, 1f, 1f, true)
            addPart((worldIn.getDimension() as EndDimension).biomeProvider)
            for (entity in worldIn.players)
            {
                entity.sendStatusMessage(StringTextComponent(I18n.format("phoenix.message.newstage")), false)
                entity.sendStatusMessage(StringTextComponent((stage + 1).toString() + " " + (part + 1) + " "), false)
            }
            PhxTriggers.CHANGE_STAGE.test((player as ServerPlayerEntity), stage)
            worldIn.setBlockState(pos, Blocks.AIR.defaultState)
        }
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit)
    }
}