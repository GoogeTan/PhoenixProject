package projectend;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import projectend.proxy.Common;
import projectend.world.capablity.IStager;

@Mod
(
   modid = Projectend.MOD_ID,
    name = Projectend.MOD_NAME,
 version = Projectend.VERSION
)
public class Projectend
{
    public static final String MOD_ID = "projectend";
    public static final String MOD_NAME = "projectend";
    public static final String VERSION = "0.0.1a";
    public static Logger logger = LogManager.getLogger();
    public static final CreativeTabs            TheEndOfCreativeTabs = new EndOfTheTabs(CreativeTabs.getNextID(), "end_of_the_tabs");
    public static final Item.ToolMaterial       siliconlife          = EnumHelper.addToolMaterial ("projectend:silinonlife", 2, 9000, 100.0F, 6.0F, 12);
    public static final ItemArmor.ArmorMaterial armorMaterial        = EnumHelper.addArmorMaterial("projectend:lifedarmor", "projectend:lifedarmor", 9000, new int[]{1, 1, 1, 1}, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 20.0F);
    static{ FluidRegistry.enableUniversalBucket(); }

    @CapabilityInject(IStager.class)
    public static final Capability<IStager> STAGER_CAPABILITY = null;

    @Mod.Instance(MOD_ID)
    public static Projectend INSTANCE;

    public Projectend() {this.INSTANCE = this;}

    @SidedProxy(clientSide = "projectend.proxy.Client", serverSide = "projectend.proxy.Common") public static Common proxy;
    @Mod.EventHandler  public void preInit(FMLPreInitializationEvent event)  { proxy.preInit(event); }
    @Mod.EventHandler  public void init(FMLInitializationEvent event)        { proxy.init(event);    }
    @Mod.EventHandler  public void postInit(FMLPostInitializationEvent event){ proxy.postInit(event);}
}
