package phoenix.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;
import phoenix.world.StageSaveData;

public class UpdaterBlock extends Block
{
    public UpdaterBlock()
    {
        super(Block.Properties.create(Material.ROCK).lightValue(5).hardnessAndResistance(-1));
    }


    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if(!worldIn.isRemote)
        {
            StageSaveData data = StageSaveData.get((ServerWorld) worldIn);
            data.addPart();
            for (PlayerEntity entity : worldIn.getPlayers())
            {
                entity.sendStatusMessage(new TranslationTextComponent("phoenix.message.newstage"), false);
                entity.sendStatusMessage(new StringTextComponent((data.getStage() + 1) + " " + (data.getPart() + 1) + " "), false);
            }
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }
}
