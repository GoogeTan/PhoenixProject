package phoenix.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import phoenix.containers.DiaryContainer;
import phoenix.init.PhoenixContainers;

public class ItemDrLsDiary extends Item
{
    public ItemDrLsDiary()
    {
        super(new Properties().rarity(Rarity.EPIC));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        if(playerIn instanceof ServerPlayerEntity)
        {
            DiaryContainer container = PhoenixContainers.GUIDE.get().create(0, playerIn.inventory);
            NetworkHooks.openGui((ServerPlayerEntity) playerIn, container);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
