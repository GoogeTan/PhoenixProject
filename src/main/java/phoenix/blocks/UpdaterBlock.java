package phoenix.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import phoenix.utils.BlockWithTile;
import phoenix.world.StageSaveData;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class UpdaterBlock extends Block
{
    public UpdaterBlock()
    {
        super(Block.Properties.create(Material.ROCK).lightValue(5).hardnessAndResistance(-1));
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    @OnlyIn(Dist.DEDICATED_SERVER)
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        StageSaveData data = StageSaveData.get((ServerWorld) worldIn);
        data.addPart();
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }
}
