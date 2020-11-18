package phoenix.recipes.tank

import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipeSerializer
import net.minecraft.network.PacketBuffer
import net.minecraft.util.JSONUtils
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistryEntry
import phoenix.utils.SerializeUtils

class TankRecipeSerializer : IRecipeSerializer<TankRecipe>, ForgeRegistryEntry<IRecipeSerializer<*>>()
{
    override fun read(id: ResourceLocation, json: JsonObject): TankRecipe
    {
        val group = JSONUtils.getString(json, "group", "")
        if (!json.has("input")) throw JsonSyntaxException("Missing result, expected to find a string or object")
        if (!json.has("result")) throw JsonSyntaxException("Missing result, expected to find a string or object")
        val input:  ItemStack = SerializeUtils.readItemStack(json, "input")
        val result: ItemStack = SerializeUtils.readItemStack(json, "result")
        val exp = JSONUtils.getFloat(json, "experience", 0.0f)
        val cookingTime = JSONUtils.getInt(json, "cookingtime")
        return TankRecipe(id, group, input, result, exp, cookingTime)
    }

    override fun read(recipeId: ResourceLocation, buffer: PacketBuffer): TankRecipe?
    {
        val group = buffer.readString(32767)
        val input = buffer.readItemStack()
        val result = buffer.readItemStack()
        val exp = buffer.readFloat()
        val cookTime = buffer.readVarInt()
        return TankRecipe(recipeId, group, input, result, exp, cookTime)
    }

    override fun write(buffer: PacketBuffer, recipe: TankRecipe)
    {
        buffer.writeString(recipe.group)
        buffer.writeItemStack(recipe.getInput())
        buffer.writeItemStack(recipe.getResult())
        buffer.writeFloat(recipe.experience)
        buffer.writeVarInt(recipe.cookTime)
    }
}