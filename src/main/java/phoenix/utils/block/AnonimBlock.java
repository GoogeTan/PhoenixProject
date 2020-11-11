package phoenix.utils.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import phoenix.Phoenix;

import java.util.function.Supplier;

public class AnonimBlock extends Block
{
    public static Supplier<Block> create(Block.Properties properties)
    {
        return () ->new AnonimBlock(properties);
    }

    public static Supplier<Block> create(Material material)
    {
        return () ->new AnonimBlock(Properties.create(material));
    }
    public static Supplier<Block> create(Material material, Class<TileEntity> entityClass)
    {
        return () ->new AnonimBlock(Properties.create(material), entityClass);
    }

    public static Supplier<Block> create(Block.Properties properties, Class<TileEntity> classIn)
    {
        return () -> new AnonimBlock(properties, classIn);
    }
    Class<TileEntity> tile = null;
    public AnonimBlock(Block.Properties properties)
    {
        super(properties);
    }
    public AnonimBlock(Block.Properties properties, Class<TileEntity> classIn)
    {
        super(properties);
        tile = classIn;
    }

    @Override public boolean hasTileEntity(BlockState state){ return tile != null;	}


    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        try
        {
            return tile.newInstance();
        }
        catch (Exception e)
        {
            Phoenix.LOGGER.error("Can not init tile: " + tile);
            return null;
        }
    }
}
