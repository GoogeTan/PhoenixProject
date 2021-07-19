package phoenix.init

import net.minecraft.entity.EntityClassification
import net.minecraft.entity.EntityType
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistries
import phoenix.Phoenix
import phoenix.enity.*
import phoenix.enity.boss.DragonAshStageEntity
import phoenix.enity.boss.DragonRedoStageEntity
import phoenix.enity.boss.balls.ExplosiveBallEntity
import phoenix.enity.projectile.IceBallEntity
import phoenix.enity.projectile.KnifeEntity
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.MOD_BUS

object PhxEntities
{
    private val ENTITIES = KDeferredRegister(ForgeRegistries.ENTITIES, Phoenix.MOD_ID)

    val talpa: EntityType<TalpaEntity> by ENTITIES.register("talpa")
    {
        EntityType.Builder.create(::TalpaEntity, EntityClassification.CREATURE)
                .size(0.5f, 0.5f)
                .setTrackingRange(80)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true)
                .build(ResourceLocation(Phoenix.MOD_ID, "talpa").toString())
    }

    val cauda: EntityType<CaudaEntity> by ENTITIES.register("cauda")
    {
        EntityType.Builder.create(::CaudaEntity, EntityClassification.CREATURE)
                .size(0.9f, 0.5f)
                .setTrackingRange(80)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true)
                .build(ResourceLocation(Phoenix.MOD_ID, "cauda").toString())
    }

    val zirconiumKnife: EntityType<KnifeEntity> by ENTITIES.register("zirconium_knife")
    {
        EntityType.Builder.create(::KnifeEntity, EntityClassification.MISC)
                .size(0.1f, 0.1f)
                .setTrackingRange(80)
                .setUpdateInterval(1)
                .setShouldReceiveVelocityUpdates(true)
                .build(ResourceLocation(Phoenix.MOD_ID, "zirconium_knife").toString())
    }

    val enderCrystal: EntityType<EnderCrystalEntity> by ENTITIES.register("ender_crystal")
    {
        EntityType.Builder.create(::EnderCrystalEntity, EntityClassification.MONSTER)
            .immuneToFire()
            .size(2.0f, 2.0f)
            .setTrackingRange(80)
            .setUpdateInterval(1)
            .build(ResourceLocation(Phoenix.MOD_ID, "ender_crystal").toString())
    }

    val dragonAshStage: EntityType<DragonAshStageEntity> by ENTITIES.register("dragon_ash_stage")
    {
        EntityType.Builder.create(::DragonAshStageEntity, EntityClassification.MONSTER)
            .immuneToFire()
            .size(16.0f, 8.0f)
            .build(ResourceLocation(Phoenix.MOD_ID, "dragon_ash_stage").toString())
    }

    val dragonRedoStage: EntityType<DragonRedoStageEntity> by ENTITIES.register("dragon_redo_stage")
    {
        EntityType.Builder.create(::DragonRedoStageEntity, EntityClassification.MONSTER)
            .immuneToFire()
            .size(16.0f, 8.0f)
            .build(ResourceLocation(Phoenix.MOD_ID, "dragon_redo_stage").toString())
    }

    val explosiveBall : EntityType<ExplosiveBallEntity> by ENTITIES.register("explosiveball")
    {
        EntityType.Builder.create(::ExplosiveBallEntity, EntityClassification.MISC)
                .immuneToFire()
                .size(1.0f, 1.0f)
                .build(ResourceLocation(Phoenix.MOD_ID, "explosiveball").toString())
    }

    val ancientGolemEntity : EntityType<AncientGolemEntity> by ENTITIES.register("ancient_golem")
    {
        EntityType.Builder.create(::AncientGolemEntity, EntityClassification.MONSTER).immuneToFire().size(1.0f, 1.0f).build(ResourceLocation(Phoenix.MOD_ID, "ancient_golem").toString())
    }

    val iceBall : EntityType<IceBallEntity> by ENTITIES.register("iceball")
    {
        EntityType.Builder.create(::IceBallEntity, EntityClassification.MISC)
            .immuneToFire()
            .size(0.5f, 0.5f)
            .build(ResourceLocation(Phoenix.MOD_ID, "iceball").toString())
    }

    fun register() = ENTITIES.register(MOD_BUS)
}