package projectend.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import projectend.BlocksRegister;
import projectend.EntityRegister;
import projectend.util.ParticleConfEndRodFactory;

public class Client extends Common
{
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
        EntityRegister.initModels();
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        super.init(event);
        BlocksRegister.registerRender();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        super.postInit(event);
        Minecraft.getMinecraft().effectRenderer.registerParticle(EnumParticleTypes.END_ROD.getParticleID(), new ParticleConfEndRodFactory());
    }
}
