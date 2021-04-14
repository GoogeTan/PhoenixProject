package phoenix.enity.boss.balls

import net.minecraft.entity.AreaEffectCloudEntity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.DamagingProjectileEntity
import net.minecraft.particles.IParticleData
import net.minecraft.particles.ParticleTypes
import net.minecraft.potion.EffectInstance
import net.minecraft.potion.Effects
import net.minecraft.util.DamageSource
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.EntityRayTraceResult
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.Explosion
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import phoenix.init.PhoenixEntities

class ExplosiveBallEntity : DamagingProjectileEntity
{
    constructor(type: EntityType<out ExplosiveBallEntity>, world: World) :  super(type, world)

    @OnlyIn(Dist.CLIENT)
    constructor(worldIn: World, x: Double, y: Double, z: Double, accelX: Double, accelY: Double, accelZ: Double) : super(PhoenixEntities.explosiveBall, x, y, z, accelX, accelY, accelZ, worldIn)

    constructor(worldIn: World, shooter: LivingEntity, accelX: Double, accelY: Double, accelZ: Double) : super(PhoenixEntities.explosiveBall, shooter, accelX, accelY, accelZ, worldIn)

    override fun onImpact(result: RayTraceResult)
    {
        super.onImpact(result)
        if (result.type != RayTraceResult.Type.ENTITY || !(result as EntityRayTraceResult).entity.isEntityEqual(shootingEntity))
        {
            if (!world.isRemote)
            {
                world.createExplosion(this, this.posX, this.posY, this.posZ, 7.5f, false, Explosion.Mode.NONE)
                world.playEvent(2006, BlockPos(this), 0)
                this.remove()
            }
        }
    }

    override fun canBeCollidedWith(): Boolean = false

    override fun attackEntityFrom(source: DamageSource?, amount: Float): Boolean = false

    override fun getParticle(): IParticleData? = ParticleTypes.DRAGON_BREATH

    override fun isFireballFiery(): Boolean = false
}