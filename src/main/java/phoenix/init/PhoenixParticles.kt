package phoenix.init

import net.minecraft.particles.BasicParticleType
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import phoenix.Phoenix


object PhoenixParticles
{
    @JvmStatic
    val PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Phoenix.MOD_ID)!!

    val PHOENIX_BORN = PARTICLES.register("phoenix_born") { BasicParticleType(true) }!!

    fun register()
    {
        PARTICLES.register(FMLJavaModLoadingContext.get().modEventBus)
    }
}