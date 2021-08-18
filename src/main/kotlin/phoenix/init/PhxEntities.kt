package phoenix.init

import net.minecraft.entity.EntityClassification
import net.minecraft.entity.EntityType
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistries
import phoenix.MOD_ID
import phoenix.Phoenix
import phoenix.enity.AncientGolemEntity
import phoenix.enity.CaudaEntity
import phoenix.enity.TalpaEntity
import phoenix.enity.projectile.ExplosiveBallEntity
import phoenix.enity.projectile.IceBallEntity
import phoenix.enity.projectile.KnifeEntity
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.MOD_BUS

object PhxEntities
{
    private val ENTITIES = KDeferredRegister(ForgeRegistries.ENTITIES, MOD_ID)

    val talpa: EntityType<TalpaEntity> by ENTITIES.register("talpa")
    {
        EntityType.Builder.create(::TalpaEntity, EntityClassification.CREATURE)
                .size(0.5f, 0.5f)
                .setTrackingRange(80)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true)
                .build(ResourceLocation(MOD_ID, "talpa").toString())
    }

    val cauda: EntityType<CaudaEntity> by ENTITIES.register("cauda")
    {
        EntityType.Builder.create(::CaudaEntity, EntityClassification.CREATURE)
                .size(0.9f, 0.5f)
                .setTrackingRange(80)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true)
                .build(ResourceLocation(MOD_ID, "cauda").toString())
    }

    val zirconiumKnife: EntityType<KnifeEntity> by ENTITIES.register("zirconium_knife")
    {
        EntityType.Builder.create(::KnifeEntity, EntityClassification.MISC)
                .size(0.1f, 0.1f)
                .setTrackingRange(80)
                .setUpdateInterval(1)
                .setShouldReceiveVelocityUpdates(true)
                .build(ResourceLocation(MOD_ID, "zirconium_knife").toString())
    }

    val explosiveBall : EntityType<ExplosiveBallEntity> by ENTITIES.register("explosiveball")
    {
        EntityType.Builder.create(::ExplosiveBallEntity, EntityClassification.MISC)
                .immuneToFire()
                .size(1.0f, 1.0f)
                .build(ResourceLocation(MOD_ID, "explosiveball").toString())
    }

    val ancientGolemEntity : EntityType<AncientGolemEntity> by ENTITIES.register("ancient_golem")
    {
        EntityType.Builder.create(::AncientGolemEntity, EntityClassification.MONSTER).immuneToFire().size(1.0f, 1.0f).build(ResourceLocation(MOD_ID, "ancient_golem").toString())
    }

    val iceBall : EntityType<IceBallEntity> by ENTITIES.register("iceball")
    {
        EntityType.Builder.create(::IceBallEntity, EntityClassification.MISC)
            .immuneToFire()
            .size(0.5f, 0.5f)
            .build(ResourceLocation(MOD_ID, "iceball").toString())
    }

    fun register() = ENTITIES.register(MOD_BUS)
}