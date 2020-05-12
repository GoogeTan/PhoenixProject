package projectend.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class TestBlock extends Block
{
    public TestBlock()
    {
        super(Material.ROCK);
        setRegistryName("test");
    }
}
