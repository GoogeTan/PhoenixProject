package phoenix.blocks;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class AntiAirBlock extends AirBlock
{
    public AntiAirBlock()
    {
        super(Block.Properties.create(Material.AIR).doesNotBlockMovement().noDrops().notSolid());
    }
}
