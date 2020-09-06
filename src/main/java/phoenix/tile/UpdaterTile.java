package phoenix.tile;

import net.minecraft.block.Blocks;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import phoenix.init.PhoenixTile;
import phoenix.world.StageSaveData;

public class UpdaterTile extends TileEntity implements ITickableTileEntity
{
    public UpdaterTile(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    public UpdaterTile()
    {
        super(PhoenixTile.UPDATOR.get());
    }


    @Override
    public void tick()
    {
        if(!world.isRemote)
        {
            StageSaveData data = StageSaveData.get((ServerWorld) world);
            BlockPos posbase = pos.up(-1);
            BlockPos[] blocks = {posbase, posbase.south(), posbase.north(), posbase.east(), posbase.west(), posbase.east().south(), posbase.east().north(), posbase.west().south(), posbase.west().north()};
            for (BlockPos block : blocks)
            {
                if (Blocks.OBSIDIAN != world.getBlockState(block).getBlock())
                {
                    return;
                }
            }
            world.removeBlock(pos, false);
            for (BlockPos block : blocks)
            {
                world.removeBlock(block, false);
            }
            data.addPart();
            if (data.getPart() > 4)
            {
                data.setPart(1);
                data.addStage();
            }

            world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ()).sendMessage(new StringTextComponent(data.getStage() + " " + data.getPart()));
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.MUSIC, 1, 1, false);
        }
    }
}