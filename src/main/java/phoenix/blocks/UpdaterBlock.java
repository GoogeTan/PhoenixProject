package phoenix.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import phoenix.init.PhoenixSounds;
import phoenix.init.PhoenixTriggers;
import phoenix.world.EndDimension;
import phoenix.world.StageManager;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class UpdaterBlock extends Block
{
    public UpdaterBlock()
    {
        super(Block.Properties.create(Material.ROCK).lightValue(5).hardnessAndResistance(-1));
    }

    @Nonnull
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if(!worldIn.isRemote && worldIn.getDimension().getType() == DimensionType.THE_END)
        {
            int stageOld = StageManager.getStage();
            worldIn.playSound(pos.getX(), pos.getY(), pos.getZ(), PhoenixSounds.INSTANCE.getCHANGE_STAGE(), SoundCategory.BLOCKS, 1, 1, true);
            StageManager.addPart(((EndDimension)worldIn.getDimension()).biomeProvider);
            for (PlayerEntity entity : worldIn.getPlayers())
            {
                entity.sendStatusMessage(new TranslationTextComponent("phoenix.message.newstage"), false);
                entity.sendStatusMessage(new StringTextComponent((StageManager.getStage() + 1) + " " + (StageManager.getPart() + 1) + " "), false);
            }
            PhoenixTriggers.INSTANCE.getCHANGE_STAGE().test((ServerPlayerEntity) player, stageOld, StageManager.getStage());
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());

        }
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }
}