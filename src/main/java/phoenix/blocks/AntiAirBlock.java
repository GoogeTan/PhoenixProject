package phoenix.blocks;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import phoenix.utils.INonItem;

public class AntiAirBlock extends AirBlock implements INonItem
{
    public AntiAirBlock()
    {
        super(Block.Properties.create(Material.AIR).doesNotBlockMovement().noDrops().notSolid());
    }
}
