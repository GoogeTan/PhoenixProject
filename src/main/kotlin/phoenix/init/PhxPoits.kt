package phoenix.init

import com.google.common.collect.ImmutableSet
import net.minecraft.block.Blocks
import net.minecraft.entity.merchant.villager.VillagerProfession
import net.minecraft.util.SoundEvents
import net.minecraft.village.PointOfInterestType
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.registries.ForgeRegistries
import phoenix.MOD_ID
import phoenix.Phoenix
import thedarkcolour.kotlinforforge.forge.KDeferredRegister

@Mod.EventBusSubscriber
object PhxPoits
{
    val poits = KDeferredRegister(ForgeRegistries.POI_TYPES, MOD_ID)

    val craftingTable by poits.register("crafting_table") { PointOfInterestType("herbsmith", PointOfInterestType.getAllStates(Blocks.CRAFTING_TABLE), 1, 1) }

    lateinit var herbsmithProfession : VillagerProfession

    @SubscribeEvent
    fun register(event : RegistryEvent.Register<VillagerProfession>)
    {
        herbsmithProfession = VillagerProfession("herbsmith", craftingTable, ImmutableSet.of(), ImmutableSet.of(), SoundEvents.ENTITY_VILLAGER_WORK_FARMER)
        event.registry.register(herbsmithProfession);
    }
}