package phoenix.items

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Rarity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.world.World
import net.minecraftforge.fml.network.NetworkHooks
import phoenix.Phoenix
import phoenix.init.PhxContainers

class DiaryItem : Item(Properties().rarity(Rarity.EPIC).group(Phoenix.ASH).maxStackSize(1))
{
    override fun onItemRightClick(worldIn: World, playerIn: PlayerEntity, handIn: Hand): ActionResult<ItemStack>
    {
        if (playerIn is ServerPlayerEntity)
            NetworkHooks.openGui(playerIn, PhxContainers.GUIDE.create(0, playerIn.inventory))
        return super.onItemRightClick(worldIn, playerIn, handIn)
    }
}