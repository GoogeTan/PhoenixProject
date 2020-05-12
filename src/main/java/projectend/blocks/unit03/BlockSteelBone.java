package projectend.blocks.unit03;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockSteelBone extends Block
{
    public BlockSteelBone(Material materialIn)
    {
        super(Material.IRON);
        setHardness(300);
        setRegistryName("ceaomic_block");
    }
}
