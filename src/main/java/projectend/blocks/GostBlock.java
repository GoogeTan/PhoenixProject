package projectend.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import projectend.blocks.title.BlockTileEntity;
import projectend.blocks.title.GostBlockTile;

import javax.annotation.Nullable;

public class GostBlock extends BlockTileEntity
{
    public GostBlock()
    {
      super("gost", Material.ROCK, 3.0F,15.0F, SoundType.GROUND);
    }

    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
    {
        int j = 1;
        if (!worldIn.isRemote)
        {
            j = ((GostBlockTile) getTileEntity(worldIn, pos)).getCount();
        }
        if(entityIn instanceof EntityPlayer || entityIn instanceof EntityPlayerMP)
        {

            for (int x = pos.getX() - 10; x < pos.getX() + 10; x++)
            {
                for (int z = pos.getZ() - 10; z < pos.getZ() + 10; z++)
                {
                    for (int y = pos.getY() - 10; y < pos.getY() + 10; y++)
                    {
                        if (worldIn.getBlockState(new BlockPos(x, y, z)).getBlock().getDefaultState().equals(this.getDefaultState()))
                        {
                            if (j == ((GostBlockTile) getTileEntity(worldIn, new BlockPos(x, y ,z))).getCount())
                            {
                                worldIn.setBlockToAir(new BlockPos(x, y, z));
                                if (worldIn instanceof WorldServer)
                                {
                                ((WorldServer)worldIn).spawnParticle(EnumParticleTypes.BLOCK_CRACK, (double)x + 0.5D, (double) y + 0.25D, (double)z + 0.5D, 8, 0.5D, 0.25D, 0.5D, 0.0D);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos position, IBlockState blockState, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {

        if (!world.isRemote)
        {
            GostBlockTile tileEntity = (GostBlockTile) getTileEntity(world, position);
            if (side == EnumFacing.DOWN) {
                tileEntity.decrementCount();
            }

            else if (side == EnumFacing.UP) {
                tileEntity.incrementCount();
            }
            player.sendMessage(new TextComponentString("Count: " + tileEntity.getCount()));
        }
        return true;
    }
    @Override
    public Class getTileEntityClass() {
        return GostBlockTile.class;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState blockState) {
        return new GostBlockTile();
    }
}
