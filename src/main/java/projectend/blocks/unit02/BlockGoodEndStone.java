package projectend.blocks.unit02;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import projectend.ProjectEnd;

public class BlockGoodEndStone extends Block
{
    public BlockGoodEndStone()
    {
        super(Material.ROCK);
        setRegistryName("fertile_end_stone");
        setTranslationKey("fertile_end_stone");
        setCreativeTab(ProjectEnd.TheEndOfCreativeTabs);
    }
}
