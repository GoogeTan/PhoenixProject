package phoenix.blocks.ash;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.common.ToolType;

import java.util.List;

public class ZirconiumOreBlock extends OreBlock
{
    public ZirconiumOreBlock()
    {
        super(Properties.create(Material.ROCK).hardnessAndResistance(3).harvestTool(ToolType.PICKAXE));
    }
    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return ImmutableList.of(new ItemStack(this));
    }
}