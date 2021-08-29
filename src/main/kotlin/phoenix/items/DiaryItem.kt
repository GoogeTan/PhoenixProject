package phoenix.items

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Rarity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.world.World
import phoenix.Phoenix
import phoenix.init.PhxContainers
import phoenix.network.OpenDiaryPacket
import phoenix.network.sendToPlayer

class DiaryItem : Item(Properties().rarity(Rarity.EPIC).group(Phoenix.ASH).maxStackSize(1))
{
    override fun onItemRightClick(worldIn: World, playerIn: PlayerEntity, handIn: Hand): ActionResult<ItemStack>
    {
        if (playerIn is ServerPlayerEntity)
            OpenDiaryPacket().sendToPlayer(playerIn)
        return super.onItemRightClick(worldIn, playerIn, handIn)
    }
}