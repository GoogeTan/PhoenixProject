package phoenix.init

import net.minecraftforge.registries.ForgeRegistries
import phoenix.Phoenix
import phoenix.recipes.DryerRecipeSerializer
import phoenix.recipes.OvenRecipeSerializer
import phoenix.recipes.tank.TankRecipeSerializer
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.MOD_BUS

object PhxRecipeSerializers
{
    private val RS = KDeferredRegister(ForgeRegistries.RECIPE_SERIALIZERS, Phoenix.MOD_ID)

    val OVEN  by RS.register("oven_recipe", ::OvenRecipeSerializer)
    val TANK  by RS.register("tank_recipe", ::TankRecipeSerializer)
    val DRYER by RS.register("dryer_recipe", ::DryerRecipeSerializer)
    fun register() = RS.register(MOD_BUS)
}
