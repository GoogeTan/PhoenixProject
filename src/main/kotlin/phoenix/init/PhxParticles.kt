package phoenix.init

import net.minecraftforge.registries.ForgeRegistries
import phoenix.MOD_ID
import phoenix.Phoenix
import phoenix.particles.PhoenixBornFactory
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.MOD_BUS

object PhxParticles
{
    val PARTICLES = KDeferredRegister(ForgeRegistries.PARTICLE_TYPES, MOD_ID)

    val PHOENIX_BORN by PARTICLES.register("phoenix_born") { PhoenixBornFactory.PhoenixBornType() }

    fun register() = PARTICLES.register(MOD_BUS)
}