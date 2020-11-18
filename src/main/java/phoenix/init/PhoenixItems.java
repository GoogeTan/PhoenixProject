package phoenix.init;

import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import phoenix.Phoenix;
import phoenix.items.ItemDiary;
import phoenix.items.ash.CrucibleItem;
import phoenix.items.ash.FormItem;
import phoenix.items.ash.HighQualityClayItem;

import java.util.function.Supplier;

public class PhoenixItems
{
    private static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Phoenix.MOD_ID);

    public static final RegistryObject<Item> GUIDE                   = ITEMS.register("diary",            ItemDiary::new);
    public static final RegistryObject<Item> HIGH_QUALITY_CLAY_ITEM  = ITEMS.register("high_quality_clay",HighQualityClayItem::new);
    public static final RegistryObject<Item> CRUCIBLE                = ITEMS.register("crucible",         CrucibleItem::new);

    public static final RegistryObject<Item> CRUCIBLE_WITH_IRON_ORE  = ITEMS.register("crucible_with_iron_ore", CrucibleItem::new);
    public static final RegistryObject<Item> CRUCIBLE_WITH_IRON      = ITEMS.register("crucible_with_iron",     CrucibleItem::new);
    public static final RegistryObject<Item> CRUCIBLE_WITH_GOLD_ORE  = ITEMS.register("crucible_with_gold_ore", CrucibleItem::new);
    public static final RegistryObject<Item> CRUCIBLE_WITH_GOLD      = ITEMS.register("crucible_with_gold",     CrucibleItem::new);

    public static final RegistryObject<Item> STEEL_INGOT             = ITEMS.register("steel_ingot",     basicItem());
    public static final RegistryObject<Item> ZIRCONIUM_INGOT         = ITEMS.register("zirconium_ingot", basicItem());

    public static final RegistryObject<Item> ZIRCONIUM_SWORD_BLADE   = ITEMS.register("zirconium_sword_blade",   basicItem());
    public static final RegistryObject<Item> ZIRCONIUM_KNIFE_BLADE   = ITEMS.register("zirconium_knife_blade",   basicItem());
    public static final RegistryObject<Item> ZIRCONIUM_BAYONET       = ITEMS.register("zirconium_bayonet",       basicItem());
    public static final RegistryObject<Item> ZIRCONIUM_BUTT          = ITEMS.register("zirconium_butt",          basicItem());


    public static final RegistryObject<Item> STEEL_FORM_KNIFE_BLADE  = ITEMS.register("steel_form_knife_blade",   basicItem());
    public static final RegistryObject<Item> STEEL_FORM_SWORD_BLADE  = ITEMS.register("steel_form_sword_blade",   basicItem());
    public static final RegistryObject<Item> STEEl_FORM_BAYONET      = ITEMS.register("steel_form_bayonet", basicItem());
    public static final RegistryObject<Item> STEEl_FORM_BUTT         = ITEMS.register("steel_form_butt",    basicItem());

    public static final RegistryObject<Item> STEEL_FORM_KNIFE_BLADE_FULL   = ITEMS.register("steel_form_knife_blade_full",   () -> new FormItem(STEEL_FORM_KNIFE_BLADE));
    public static final RegistryObject<Item> STEEL_FORM_SWORD_BLADE_FULL   = ITEMS.register("steel_form_sword_blade_full",   () -> new FormItem(STEEL_FORM_SWORD_BLADE));
    public static final RegistryObject<Item> STEEl_FORM_BAYONET_FULL       = ITEMS.register("steel_form_bayonet_full", () -> new FormItem(STEEl_FORM_BAYONET));
    public static final RegistryObject<Item> STEEl_FORM_BUTT_FULL          = ITEMS.register("steel_form_butt_full",    () -> new FormItem(STEEl_FORM_BUTT));

    public static final RegistryObject<Item> ZIRCONIUM_AXE          = ITEMS.register("ceramic_zirconium_axe", () -> new AxeItem(PhoenixTiers.ZIRCONIUM_TIER, 9.0F, 1.1F, new Item.Properties().group(Phoenix.PHOENIX)));
    public static final RegistryObject<Item> ZIRCONIUM_PICKAXE      = ITEMS.register("ceramic_zirconium_pickaxe",    () -> new FormItem(STEEl_FORM_BUTT));
    public static final RegistryObject<Item> ZIRCONIUM_SWORD        = ITEMS.register("ceramic_zirconium_sword", () -> new FormItem(STEEl_FORM_BAYONET));
    public static final RegistryObject<Item> ZIRCONIUM_KNIFE        = ITEMS.register("ceramic_zirconium_knife",    () -> new FormItem(STEEl_FORM_BUTT));

    public static void register()
    {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private static Supplier<? extends Item> basicItem()
    {
        return () -> new Item(new Item.Properties().group(Phoenix.PHOENIX));
    }
}
