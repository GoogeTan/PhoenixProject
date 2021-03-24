package phoenix.mixin

import net.minecraft.item.Items
import net.minecraft.potion.PotionBrewing
import net.minecraft.potion.Potions
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import phoenix.init.PhoenixItems.GOLDEN_SETA
import phoenix.init.PhoenixPotions.LEVITATION
import phoenix.init.PhoenixPotions.LONG_LEVITATION
import phoenix.utils.LogManager.error


@Mixin(PotionBrewing::class)
class PotionBrewingMixin
{
    @Inject(method = ["init"], at = [At("TAIL")])
    private fun init(ci: CallbackInfo)
    {
        error("PotionBrewingMixin", "Mixed!")
        PotionBrewing.addMix(Potions.MUNDANE, GOLDEN_SETA.get(), LEVITATION.get())
        PotionBrewing.addMix(LEVITATION.get(), Items.REDSTONE, LONG_LEVITATION.get())
    }
}