package projectend.blocks.title;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import projectend.ProjectEnd;

public class Updator01TitleEntity extends TileEntity implements ITickable
{
    @Override
    public void update()
    {
        IBlockState iblockstate01 = this.world.getBlockState(new BlockPos(this.pos.getX()       , this.pos.getY() - 1, this.pos.getZ()));
        IBlockState iblockstate02 = this.world.getBlockState(new BlockPos(this.pos.getX() - 1, this.pos.getY() - 1, this.pos.getZ()));
        IBlockState iblockstate03 = this.world.getBlockState(new BlockPos(this.pos.getX() + 1, this.pos.getY() - 1, this.pos.getZ()));
        IBlockState iblockstate04 = this.world.getBlockState(new BlockPos(this.pos.getX()       , this.pos.getY() - 1, this.pos.getZ() - 1));
        IBlockState iblockstate05 = this.world.getBlockState(new BlockPos(this.pos.getX()       , this.pos.getY() - 1, this.pos.getZ() + 1));
        IBlockState iblockstate06 = this.world.getBlockState(new BlockPos(this.pos.getX() + 1, this.pos.getY() - 1, this.pos.getZ() + 1));
        IBlockState iblockstate07 = this.world.getBlockState(new BlockPos(this.pos.getX() + 1, this.pos.getY() - 1, this.pos.getZ() - 1));
        IBlockState iblockstate08 = this.world.getBlockState(new BlockPos(this.pos.getX() - 1, this.pos.getY() - 1, this.pos.getZ() + 1));
        IBlockState iblockstate09 = this.world.getBlockState(new BlockPos(this.pos.getX() - 1, this.pos.getY() - 1, this.pos.getZ() - 1));

        if (iblockstate01.getBlock().getDefaultState() == Blocks.STAINED_GLASS.getDefaultState() &&
                iblockstate02.getBlock().getDefaultState() == Blocks.STAINED_GLASS.getDefaultState() &&
                iblockstate03.getBlock().getDefaultState() == Blocks.STAINED_GLASS.getDefaultState() &&
                iblockstate04.getBlock().getDefaultState() == Blocks.STAINED_GLASS.getDefaultState() &&
                iblockstate05.getBlock().getDefaultState() == Blocks.STAINED_GLASS.getDefaultState() &&
                iblockstate06.getBlock().getDefaultState() == Blocks.STAINED_GLASS.getDefaultState() &&
                iblockstate07.getBlock().getDefaultState() == Blocks.STAINED_GLASS.getDefaultState() &&
                iblockstate08.getBlock().getDefaultState() == Blocks.STAINED_GLASS.getDefaultState() &&
                iblockstate09.getBlock().getDefaultState() == Blocks.STAINED_GLASS.getDefaultState()   )
        {
            world.setBlockToAir(pos);
            if (world.getCapability(ProjectEnd.STAGER_CAPABILITY, null).getStageIn() > 3)
            {
                world.getCapability(ProjectEnd.STAGER_CAPABILITY, null).setStageIn(1);
                world.getCapability(ProjectEnd.STAGER_CAPABILITY, null).addStage();
            }
            else
            {
                world.getCapability(ProjectEnd.STAGER_CAPABILITY, null).addStageIn();
            }
        }
    }

}
