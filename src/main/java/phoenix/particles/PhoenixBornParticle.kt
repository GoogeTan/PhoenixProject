package phoenix.particles

import net.minecraft.client.particle.IParticleRenderType
import net.minecraft.client.particle.SpriteTexturedParticle
import net.minecraft.world.World
import phoenix.init.PhoenixParticles

class PhoenixBornParticle(world: World, x : Double, y : Double, z : Double) : SpriteTexturedParticle(world, x, y, z)
{
    override fun getRenderType(): IParticleRenderType = IParticleRenderType.PARTICLE_SHEET_OPAQUE
}