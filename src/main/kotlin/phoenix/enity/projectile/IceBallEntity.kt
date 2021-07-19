package phoenix.enity.projectile

import net.minecraft.block.AirBlock
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.SnowBlock
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.ThrowableEntity
import net.minecraft.util.DamageSource
import net.minecraft.util.math.EntityRayTraceResult
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.World
import net.minecraftforge.fml.network.NetworkHooks
import phoenix.init.PhxEntities
import phoenix.utils.get
import phoenix.utils.isServer
import phoenix.utils.set

class IceBallEntity : ThrowableEntity
{
    constructor(type: EntityType<out IceBallEntity>, world: World) : super(type, world)
    constructor(worldIn: World, owner: LivingEntity) : super(PhxEntities.iceBall, owner, worldIn)

    override fun registerData() { }

    override fun tick()
    {
        super.tick()
        if (world.isServer && !world.getFluidState(position).isEmpty)
            onImpact(null)
    }

    override fun onImpact(result: RayTraceResult?)
    {
        if (!world.isRemote)
        {
            if (result?.type == RayTraceResult.Type.ENTITY)
            {
                val entity = (result as EntityRayTraceResult).entity
                entity.attackEntityFrom(DamageSource.causeMobDamage(this.owner), 6.0f)
                applyEnchantments(owner, entity)
            }

            val explosionPower = 1 + rand.nextInt(2)
            for (dX in -explosionPower..explosionPower)
            {
                for (dZ in -explosionPower..explosionPower)
                {
                    for (dY in -explosionPower..explosionPower)
                    {
                        val pos = position.add(dX, dY, dZ)
                        val dist = pos.distanceSq(position)
                        if (dist > 3)
                            continue
                        val blockState = world[pos]
                        val block: Block = blockState.block
                        world[pos] =
                            when
                            {
                                block == Blocks.WATER                                               ->
                                {
                                    val fluidState = world.getFluidState(pos)
                                    if (!fluidState.isSource)
                                        Blocks.SNOW.defaultState.with(SnowBlock.LAYERS, fluidState.level)
                                    else
                                    {
                                        val rand = world.rand.nextInt(10)
                                        when
                                        {
                                            rand < 6 -> Blocks.ICE.defaultState
                                            rand < 8 -> Blocks.PACKED_ICE.defaultState
                                            else     -> Blocks.BLUE_ICE.defaultState
                                        }
                                    }
                                }
                                block == Blocks.LAVA                                                -> if (!world.getFluidState(pos).isSource) Blocks.GRAVEL.defaultState else Blocks.OBSIDIAN.defaultState
                                block == Blocks.AIR && world.getBlockState(pos.down()) !is AirBlock -> Blocks.SNOW.defaultState
                                else                                                                -> continue
                            }
                    }
                }
            }
            this.remove()
        }
    }

    override fun createSpawnPacket() = NetworkHooks.getEntitySpawningPacket(this)!!
}