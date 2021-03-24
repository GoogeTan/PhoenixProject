package phoenix.init

import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import phoenix.Phoenix
import phoenix.particles.PhoenixBornFactory

object PhoenixParticles
{
    @JvmStatic
    val PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Phoenix.MOD_ID)!!

    val PHOENIX_BORN = PARTICLES.register("phoenix_born") { PhoenixBornFactory.PhoenixBornType() }!!
    fun register()
    {
        PARTICLES.register(FMLJavaModLoadingContext.get().modEventBus)
    }
}