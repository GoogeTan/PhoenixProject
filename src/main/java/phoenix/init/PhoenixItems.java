package phoenix.init;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import phoenix.Phoenix;
import phoenix.items.ItemDrLsDiary;
import phoenix.items.ash.BucketWithClayItem;

public class PhoenixItems
{
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Phoenix.MOD_ID);

    public static final RegistryObject<Item> guide             = ITEMS.register("drls_diary",       ItemDrLsDiary     ::new);
    public static final RegistryObject<Item> bucket_with_clay  = ITEMS.register("bucket_with_clay", BucketWithClayItem::new);

    public static void register()
    {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
