package phoenix.mixin

import net.minecraft.item.Items.REDSTONE
import net.minecraft.potion.PotionBrewing
import net.minecraft.potion.Potions.MUNDANE
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
    private companion object
    {
        @Inject(method = ["init"], at = [At("TAIL")])
        @JvmStatic
        private fun init(ci: CallbackInfo)
        {
            error("PotionBrewingMixin", "Mixed!")
            PotionBrewing.addMix(MUNDANE, GOLDEN_SETA, LEVITATION)
            PotionBrewing.addMix(LEVITATION, REDSTONE, LONG_LEVITATION)
        }
    }
}