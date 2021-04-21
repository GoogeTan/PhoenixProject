package phoenix.init

import net.minecraftforge.registries.ForgeRegistries
import phoenix.Phoenix
import phoenix.particles.PhoenixBornFactory
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.MOD_BUS

object PhxParticles
{
    val PARTICLES = KDeferredRegister(ForgeRegistries.PARTICLE_TYPES, Phoenix.MOD_ID)

    val PHOENIX_BORN by PARTICLES.register("phoenix_born") { PhoenixBornFactory.PhoenixBornType() }

    fun register() = PARTICLES.register(MOD_BUS)
}