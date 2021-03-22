package phoenix.mixin;

import net.minecraft.item.Items;
import net.minecraft.potion.PotionBrewing;
import net.minecraft.potion.Potions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import phoenix.init.PhoenixItems;
import phoenix.init.PhoenixPotions;
import phoenix.utils.LogManager;

import static net.minecraft.potion.PotionBrewing.addMix;

@Mixin(PotionBrewing.class)
public class PotionBrewingMixin
{
    @Inject(method = {"init"}, at = {@At("TAIL")})
    private static void init(CallbackInfo ci)
    {
        LogManager.error("PotionBrewingMixin", "Mixed!");
        addMix(Potions.MUNDANE, PhoenixItems.INSTANCE.getGOLDEN_SETA().get(), PhoenixPotions.INSTANCE.getLEVITATION().get());
        addMix(PhoenixPotions.INSTANCE.getLEVITATION().get(), Items.REDSTONE, PhoenixPotions.INSTANCE.getLONG_LEVITATION().get());
    }
}
