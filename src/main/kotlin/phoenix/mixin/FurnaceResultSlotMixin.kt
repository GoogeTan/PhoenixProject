package phoenix.mixin

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.inventory.container.FurnaceResultSlot
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import phoenix.client.gui.diaryPages.Chapter
import phoenix.utils.IPhoenixPlayer
import phoenix.utils.addChapter
import phoenix.utils.sendMessage

@Mixin(FurnaceResultSlot::class)
class FurnaceResultSlotMixin
{
    @Inject(at = [At("HEAD")], method = ["onTake"])
    fun onTake(thePlayer: PlayerEntity?, stack: ItemStack?, cir: CallbackInfoReturnable<ItemStack>)
    {
        if(thePlayer is ServerPlayerEntity && thePlayer is IPhoenixPlayer && stack?.item == Items.IRON_INGOT)
        {
            thePlayer.addChapter(Chapter.STEEL)
            thePlayer.sendMessage("Chapter ${thePlayer.getOpenedChapters()}")
        }
    }
}