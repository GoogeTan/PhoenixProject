package phoenix.particles

import net.minecraft.network.PacketBuffer
import net.minecraft.particles.IParticleData
import net.minecraft.particles.ParticleType
import net.minecraft.util.registry.Registry
import phoenix.init.PhoenixParticles

class PhoenixParticleData(private val typeIn : ParticleType<*>) : IParticleData
{
    override fun getType() = typeIn

    override fun write(buffer: PacketBuffer)
    {
    }

    override fun getParameters(): String = Registry.PARTICLE_TYPE.getKey(PhoenixParticles.PHOENIX_BORN.get()).toString()
}
