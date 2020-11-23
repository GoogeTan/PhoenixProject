package phoenix.integration.jei

import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import net.minecraft.client.Minecraft
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.util.ResourceLocation
import phoenix.init.PhoenixBlocks
import phoenix.init.PhoenixRecipes

@JeiPlugin
class PhoenixJEIPlugin : IModPlugin
{
    private lateinit var ovenCategory: OvenCategory
    private lateinit var tankCategory: TankCategory

    override fun getPluginUid(): ResourceLocation = ResourceLocation("phoenix", "jei_plugin")

    override fun registerCategories(registration: IRecipeCategoryRegistration)
    {
        val jeiHelpers = registration.jeiHelpers
        val guiHelper = jeiHelpers.guiHelper
        val modIdHelper = jeiHelpers.modIdHelper
        ovenCategory = OvenCategory(guiHelper)
        registration.addRecipeCategories(ovenCategory)
        tankCategory = TankCategory(guiHelper)
        registration.addRecipeCategories(tankCategory)
    }

    override fun registerRecipes(register: IRecipeRegistration)
    {
        assert(Minecraft.getInstance().world != null)
        val manager = Minecraft.getInstance().world!!.recipeManager
        // oven
        val ovenRecipes: Collection<IRecipe<IInventory>> = manager.getRecipes(PhoenixRecipes.OVEN).values
        register.addRecipes(ovenRecipes, ovenCategory.uid)
        // tank
        val tankRecipes: Collection<IRecipe<IInventory>> = manager.getRecipes(PhoenixRecipes.TANK).values
        register.addRecipes(tankRecipes, tankCategory.uid)
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration)
    {
        registration.addRecipeCatalyst(ItemStack(PhoenixBlocks.OVEN.get()), ovenCategory.uid)
        registration.addRecipeCatalyst(ItemStack(PhoenixBlocks.TANK.get()), tankCategory.uid)
    }


}