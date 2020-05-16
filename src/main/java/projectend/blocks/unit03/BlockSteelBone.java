package projectend.blocks.unit03;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import projectend.ProjectEnd;

public class BlockSteelBone extends Block
{
    public BlockSteelBone()
    {
        super(Material.IRON);
        setHardness(300);
        setRegistryName("ceraomic");
        setTranslationKey("ceraomic_block");
        setCreativeTab(ProjectEnd.TheEndOfCreativeTabs);
    }
}
