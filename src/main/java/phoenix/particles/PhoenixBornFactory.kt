package phoenix.particles

import net.minecraft.client.particle.IParticleFactory
import net.minecraft.client.particle.Particle
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class PhoenixBornFactory : IParticleFactory<PhoenixParticleData>
{
    override fun makeParticle(typeIn: PhoenixParticleData, worldIn: World, x: Double, y: Double, z: Double, xSpeed: Double, ySpeed: Double, zSpeed: Double): Particle
    {
        return PhoenixBornParticle(worldIn, x, y, z)
    }
}