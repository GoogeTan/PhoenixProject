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

class DryerRecipeSerializer: ForgeRegistryEntry<IRecipeSerializer<*>?>(),
    IRecipeSerializer<DryerRecipe>
{
    @Nonnull
    override fun read(recipeId: ResourceLocation, json: JsonObject): DryerRecipe
    {
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
        val cookingtime = JSONUtils.getInt(json, "cookingtime", 100)
        return DryerRecipe(recipeId, ingredient, itemstack, cookingtime)
    }

    override fun read(recipeId: ResourceLocation, buffer: PacketBuffer): DryerRecipe
    {
        val ingredient = Ingredient.read(buffer)
        val result = buffer.readItemStack()
        val cookTime = buffer.readVarInt()
        return DryerRecipe(recipeId, ingredient, result, cookTime)
    }

    override fun write(buffer: PacketBuffer, recipe: DryerRecipe)
    {
        buffer.writeString(recipe.group)
        recipe.input.write(buffer)
        buffer.writeItemStack(recipe.result)
        buffer.writeVarInt(recipe.time)
    }
}