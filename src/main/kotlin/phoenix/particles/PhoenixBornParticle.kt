package phoenix.particles

import net.minecraft.client.particle.IAnimatedSprite
import net.minecraft.client.particle.IParticleRenderType
import net.minecraft.client.particle.SpriteTexturedParticle
import net.minecraft.world.World
import java.awt.Color

class PhoenixBornParticle(
    world: World,
    x: Double,
    y: Double,
    z: Double,
    velocityX: Double,
    velocityY: Double,
    velocityZ: Double,
    tint: Color?,
    diameter: Double,
    lifeTime: Int,
    private val sprites: IAnimatedSprite
) :
    SpriteTexturedParticle(world, x, y, z, velocityX, velocityY, velocityZ)
{
    override fun getRenderType(): IParticleRenderType = IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT

    override fun tick()
    {
        prevPosX = posX
        prevPosY = posY
        prevPosZ = posZ
        move(motionX, motionY, motionZ)
        if (onGround)
        {
            setExpired()
        }
        if (prevPosY == posY && motionY > 0)
        {
            setExpired()
        }
        if (age++ >= maxAge)
        {
            setExpired()
        }
    }

    init
    {
        setColor(tint!!.red / 255.0f, tint.green / 255.0f, tint.blue / 255.0f)
        setSize(diameter.toFloat(), diameter.toFloat())
        maxAge = lifeTime
        particleScale = 0.5f * diameter.toFloat()
        particleAlpha = 1.0f
        motionX = velocityX
        motionY = velocityY
        motionZ = velocityZ
        canCollide = true
    }
}