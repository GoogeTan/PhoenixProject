package phoenix.init;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import phoenix.Phoenix;
import phoenix.items.ItemDiary;
import phoenix.items.ash.CrucibleItem;
import phoenix.items.ash.HighQualityClayItem;

import java.util.function.Supplier;

public class PhoenixItems
{
    private static final DeferredRegister<Item> ITEMS = new DeferredRegister(ForgeRegistries.ITEMS, Phoenix.MOD_ID);

    public static final RegistryObject<Item> GUIDE                   = ITEMS.register("diary",            ItemDiary::new);
    public static final RegistryObject<Item> HIGH_QUALITY_CLAY_ITEM  = ITEMS.register("high_quality_clay",HighQualityClayItem::new);
    public static final RegistryObject<Item> CRUCIBLE                = ITEMS.register("crucible",         CrucibleItem::new);

    public static final RegistryObject<Item> CRUCIBLE_WITH_IRON_ORE  = ITEMS.register("crucible_with_iron_ore", CrucibleItem::new);
    public static final RegistryObject<Item> CRUCIBLE_WITH_IRON      = ITEMS.register("crucible_with_iron",     CrucibleItem::new);
    public static final RegistryObject<Item> CRUCIBLE_WITH_GOLD_ORE  = ITEMS.register("crucible_with_gold_ore", CrucibleItem::new);
    public static final RegistryObject<Item> CRUCIBLE_WITH_GOLD      = ITEMS.register("crucible_with_gold",     CrucibleItem::new);

    public static final RegistryObject<Item> STEEL_INGOT             = ITEMS.register("steel_ingot",     nullItem());
    public static final RegistryObject<Item> ZIRCONIUM_INGOT         = ITEMS.register("zirconium_ingot", nullItem());

    public static void register()
    {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private static Supplier<? extends Item> nullItem()
    {
        return () -> new Item(new Item.Properties().group(Phoenix.PHOENIX));
    }
}
