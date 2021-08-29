package phoenix.init

import net.minecraftforge.registries.ForgeRegistries
import phoenix.MOD_ID
import phoenix.recipes.DryerRecipeSerializer
import phoenix.recipes.OvenRecipeSerializer
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.MOD_BUS

object PhxRecipeSerializers
{
    private val RS = KDeferredRegister(ForgeRegistries.RECIPE_SERIALIZERS, MOD_ID)

    val OVEN  by RS.register("oven_recipe")  { OvenRecipeSerializer }
    val DRYER by RS.register("dryer_recipe") { DryerRecipeSerializer }

    fun register() = RS.register(MOD_BUS)
}
