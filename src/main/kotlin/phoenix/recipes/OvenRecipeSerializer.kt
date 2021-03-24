package phoenix.recipes

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
import javax.annotation.Nonnull


class OvenRecipeSerializer : ForgeRegistryEntry<IRecipeSerializer<*>?>(),
    IRecipeSerializer<OvenRecipe>
{
    @Nonnull
    override fun read(recipeId: ResourceLocation, json: JsonObject): OvenRecipe
    {
        val s = JSONUtils.getString(json, "group", "")
        val jsonelement = if (JSONUtils.isJsonArray(json, "ingredient")) JSONUtils.getJsonArray(json, "ingredient") else JSONUtils.getJsonObject(json, "ingredient")
        val ingredient = Ingredient.deserialize(jsonelement)
        if (!json.has("result")) throw JsonSyntaxException("Missing result, expected to find a string or object")
        val itemstack: ItemStack =
            if (json["result"].isJsonObject) ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result")) else
            {
                val s1 = JSONUtils.getString(json, "result")
                val resourcelocation = ResourceLocation(s1)
                ItemStack(Registry.ITEM.getValue(resourcelocation).orElseThrow {
                    IllegalStateException(
                        "Item: $s1 does not exist"
                    )
                })
            }
        val exp = JSONUtils.getFloat(json, "experience", 0.0f)
        val cookingTime = 100
        val cookingtime = JSONUtils.getInt(json, "cookingtime", cookingTime)
        return OvenRecipe(recipeId, s, ingredient, itemstack, exp, cookingtime)
    }

    override fun read(recipeId: ResourceLocation, buffer: PacketBuffer): OvenRecipe
    {
        val group = buffer.readString(32767)
        val ingredient = Ingredient.read(buffer)
        val result = buffer.readItemStack()
        val exp = buffer.readFloat()
        val cookTime = buffer.readVarInt()
        return OvenRecipe(recipeId, group, ingredient, result, exp, cookTime)
    }

    override fun write(buffer: PacketBuffer, recipe: OvenRecipe)
    {
        buffer.writeString(recipe.group)
        recipe.ingredient.write(buffer)
        buffer.writeItemStack(recipe.result)
        buffer.writeFloat(recipe.experience)
        buffer.writeVarInt(recipe.cookTime)
    }
}