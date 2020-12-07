package phoenix.blocks.redo;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;
import phoenix.Phoenix;
import phoenix.utils.block.ICustomGroup;

public class FertileEndStoneBlock extends Block implements ICustomGroup
{
    public FertileEndStoneBlock()
    {
        super(Block.Properties.create(Material.ROCK));
    }

    @Override
    public ItemGroup getTab()
    {
        return Phoenix.getREDO();
    }
}
