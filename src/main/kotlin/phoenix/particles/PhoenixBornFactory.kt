package phoenix.particles

import net.minecraft.client.particle.IAnimatedSprite
import net.minecraft.client.particle.IParticleFactory
import net.minecraft.client.particle.Particle
import net.minecraft.particles.IParticleData
import net.minecraft.particles.ParticleType
import net.minecraft.world.World
import javax.annotation.Nullable

class PhoenixBornFactory(private val sprites: IAnimatedSprite) : IParticleFactory<PhoenixParticleData>
{
    @Nullable
    override fun makeParticle(
        typeIn: PhoenixParticleData,
        worldIn: World,
        x: Double,
        y: Double,
        z: Double,
        xSpeed: Double,
        ySpeed: Double,
        zSpeed: Double
    ): Particle
    {
        val newParticle = PhoenixBornParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed,
            typeIn.getTint(), typeIn.getDiameter(), typeIn.getLifeTime(),
            sprites
        )
        newParticle.selectSpriteRandomly(sprites)
        return newParticle
    }

    class PhoenixBornType : ParticleType<PhoenixParticleData>(false, PhoenixParticleData.DESERIALIZER)
    {
        override fun getDeserializer(): IParticleData.IDeserializer<PhoenixParticleData>
        {
            return PhoenixParticleData.DESERIALIZER
        }
    }
}