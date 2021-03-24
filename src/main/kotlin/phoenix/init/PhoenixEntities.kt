package phoenix.init

import net.minecraft.entity.EntityClassification
import net.minecraft.entity.EntityType
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.fml.RegistryObject
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import phoenix.Phoenix
import phoenix.enity.KnifeEntity
import phoenix.enity.TalpaEntity
import phoenix.enity.boss.AbstractEnderDragonEntity
import phoenix.enity.boss.DragonAshStageEntity
import phoenix.enity.boss.DragonRedoStageEntity
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.MOD_BUS

object PhoenixEntities
{
    private val ENTITIES = KDeferredRegister(ForgeRegistries.ENTITIES, Phoenix.MOD_ID)

    val TALPA by ENTITIES.register("talpa")
    {
        EntityType.Builder.create({ type: EntityType<TalpaEntity>, worldIn: World -> TalpaEntity(type, worldIn) }, EntityClassification.CREATURE)
                .size(0.5f, 0.5f)
                .setTrackingRange(80)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true)
                .build(ResourceLocation(Phoenix.MOD_ID, "talpa").toString())
    }
    /*
    val CAUDA = ENTITIES.register("cauda")
    {
        EntityType.Builder.create({ type: EntityType<CaudaEntity>, worldIn: World -> CaudaEntity(type, worldIn) }, EntityClassification.CREATURE)
                .size(0.9f, 0.5f)
                .setTrackingRange(80)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true)
                .build(ResourceLocation(Phoenix.MOD_ID, "cauda").toString())
    }
     */

    val KNIFE by ENTITIES.register("zirconium_knife")
    {
        EntityType.Builder.create({ type: EntityType<KnifeEntity>, worldIn: World -> KnifeEntity(type, worldIn) }, EntityClassification.MISC)
                .size(0.1f, 0.1f)
                .setTrackingRange(80)
                .setUpdateInterval(1)
                .setShouldReceiveVelocityUpdates(true)
                .build(ResourceLocation(Phoenix.MOD_ID, "zirconium_knife").toString())
    }

    val DRAGON_ASH_STAGE by ENTITIES.register("dragon_ash_stage")
    {
        EntityType.Builder.create(::DragonAshStageEntity, EntityClassification.MONSTER)
            .immuneToFire()
            .size(16.0f, 8.0f)
            .build(ResourceLocation(Phoenix.MOD_ID, "dragon_ash_stage").toString())
    }

    val DRAGON_REDO_STAGE by ENTITIES.register("dragon_redo_stage")
    {
        EntityType.Builder.create(::DragonRedoStageEntity, EntityClassification.MONSTER)
            .immuneToFire()
            .size(16.0f, 8.0f)
            .build(ResourceLocation(Phoenix.MOD_ID, "dragon_redo_stage").toString())
    }

    fun register() = ENTITIES.register(MOD_BUS)
}