package phoenix.init;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import phoenix.Phoenix;
import phoenix.items.ItemDiary;
import phoenix.items.ash.BucketWithClayItem;
import phoenix.items.ash.CrucibleItem;

public class PhoenixItems
{
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Phoenix.MOD_ID);

    public static final RegistryObject<Item> GUIDE             = ITEMS.register("diary",            ItemDiary::new);
    public static final RegistryObject<Item> BUCKET_WITH_CLAY  = ITEMS.register("bucket_with_clay", BucketWithClayItem::new);
    public static final RegistryObject<Item> CRUCIBLE          = ITEMS.register("crucible",         CrucibleItem::new);

    public static final RegistryObject<Item> CRUCIBLE_WITH_IRON_ORE  = ITEMS.register("crucible_with_iron_ore",  CrucibleItem::new);
    public static final RegistryObject<Item> CRUCIBLE_WITH_IRON      = ITEMS.register("crucible_with_iron",      CrucibleItem::new);
    public static final RegistryObject<Item> CRUCIBLE_WITH_GOLD_ORE  = ITEMS.register("crucible_with_gold_ore",  CrucibleItem::new);
    public static final RegistryObject<Item> CRUCIBLE_WITH_GOLD      = ITEMS.register("crucible_with_gold",      CrucibleItem::new);

    public static void register()
    {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
