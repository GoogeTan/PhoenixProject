package phoenix.recipes.tank

import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipeSerializer
import net.minecraft.item.crafting.Ingredient
import net.minecraft.item.crafting.ShapedRecipe
import net.minecraft.network.PacketBuffer
import net.minecraft.util.JSONUtils
import net.minecraft.util.ResourceLocation
import net.minecraft.util.registry.Registry
import net.minecraftforge.registries.ForgeRegistryEntry

class TankRecipeSerializer : IRecipeSerializer<TankRecipe>, ForgeRegistryEntry<IRecipeSerializer<*>>()
{
    override fun read(id: ResourceLocation, json: JsonObject): TankRecipe
    {
        val group = JSONUtils.getString(json, "group", "")
        val cookingTime = JSONUtils.getInt(json, "cookintTime")
        val jsonelement = if (JSONUtils.isJsonArray(json, "ingredient")) JSONUtils.getJsonArray(json, "ingredient") else JSONUtils.getJsonObject(json, "ingredient")
        val ingredient = Ingredient.deserialize(jsonelement)
        if (!json.has("result")) throw JsonSyntaxException("Missing result, expected to find a string or object")
        val result: ItemStack
        result = if (json["result"].isJsonObject) ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"))
        else
        {
            val s1 = JSONUtils.getString(json, "result")
            val resourcelocation = ResourceLocation(s1)
            ItemStack(Registry.ITEM.getValue(resourcelocation).orElseThrow { IllegalStateException("Item: $s1 does not exist") })
        }
        val exp = JSONUtils.getFloat(json, "experience", 0.0f)
        return TankRecipe(id, group, ingredient, result, exp, cookingTime)
    }

    override fun read(recipeId: ResourceLocation, buffer: PacketBuffer): TankRecipe?
    {
        TODO("Not yet implemented")
    }

    override fun write(buffer: PacketBuffer, recipe: TankRecipe)
    {
        TODO("Not yet implemented")
    }
}