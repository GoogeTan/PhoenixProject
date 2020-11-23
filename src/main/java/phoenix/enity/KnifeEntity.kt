package phoenix.enity

import net.minecraft.block.Blocks
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.item.ItemEntity
import net.minecraft.entity.projectile.ProjectileItemEntity
import net.minecraft.entity.projectile.ThrowableEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.network.IPacket
import net.minecraft.particles.ParticleTypes
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.util.math.EntityRayTraceResult
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.RayTraceResult.Type.*
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.fml.network.NetworkHooks
import phoenix.init.PhoenixEntities
import phoenix.init.PhoenixItems
import phoenix.items.ash.KnifeItem


class KnifeEntity : ThrowableEntity
{
    var knife = ItemStack(PhoenixItems.ZIRCONIUM_KNIFE.get())
    var isReal = false;
    constructor(type: EntityType<KnifeEntity>, worldIn: World) : super(type, worldIn)
    constructor(worldIn: World, owner: LivingEntity, isReal: Boolean) : super(PhoenixEntities.KNIFE.get(), owner, worldIn)
    {
        this.isReal = isReal;
    }

    @OnlyIn(Dist.CLIENT)
    override fun handleStatusUpdate(id: Byte)
    {
        if (id.toInt() == 3)
        {
            for (i in 0..7)
            {
                //Появляются частицы лавы. В обычном майнкрафте они образуются на потолке, если сверху лава.
                world.addParticle(ParticleTypes.RAIN, posX, posY, posZ, 0.0, 0.0, 0.0)
            }
        }
    }

    override fun onImpact(result: RayTraceResult)
    {
        if (!world.isRemote)
        {
            var dropItem = isReal
            when (result.type)
            {
               ENTITY ->
               {
                   dropItem = dropItem && (knife.item as KnifeItem).onHitEntity(world, owner, this, (result as EntityRayTraceResult).entity, knife)
                   knife.attemptDamageItem(1, rand, null)
                   if (dropItem) world.addEntity(ItemEntity(world, posX, posY, posZ, knife))
                   onKillCommand()
               }
               BLOCK ->
               {
                   val block = world.getBlockState(((result as BlockRayTraceResult).pos))
                   dropItem = dropItem and (knife.item as KnifeItem).onHitBlock(world, owner, result.pos, this, knife)
                   if(block.block !== Blocks.GRASS || block.block !== Blocks.TALL_GRASS)
                   {
                       onKillCommand()
                       knife.attemptDamageItem(1, rand, null)
                       if (dropItem) world.addEntity(ItemEntity(world, posX, posY, posZ, knife))
                   }
               }
            }
        }
    }

    override fun registerData()
    {
    }

    override fun createSpawnPacket() = NetworkHooks.getEntitySpawningPacket(this)!!
}
