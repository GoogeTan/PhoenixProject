package phoenix.init

import net.minecraftforge.registries.ForgeRegistries
import phoenix.Phoenix
import phoenix.world.structures.CustomEndSpike
import phoenix.world.structures.remains.RemainsStructure
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.MOD_BUS

object PhoenixFeatures
{
    val FEATURES = KDeferredRegister(ForgeRegistries.FEATURES, Phoenix.MOD_ID)

    val REMAINS   by FEATURES.register("remains",       ::RemainsStructure)
    val END_SPIKE by FEATURES.register("new_end_spike", ::CustomEndSpike)

    fun register()
    {
        FEATURES.register(MOD_BUS)
    }
}