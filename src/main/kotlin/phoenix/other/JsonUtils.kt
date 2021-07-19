package phoenix.other

import com.google.gson.JsonObject
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.ShapedRecipe
import net.minecraft.util.JSONUtils
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistries

fun JsonObject.getFloat(nameIn: String, fallback: Float)           = JSONUtils.getFloat(this, nameIn, fallback)
fun JsonObject.getInt(nameIn: String)                              = JSONUtils.getInt(this, nameIn)
fun JsonObject.getString(nameIn: String, fallback: String): String = JSONUtils.getString(this, nameIn, fallback)

fun JsonObject.getItemStack(nameIn: String): ItemStack
{
    return if (get(nameIn).isJsonObject)
        ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(this, nameIn))
    else
    {
        val name = JSONUtils.getString(this, nameIn)
        ItemStack(ForgeRegistries.ITEMS.getValue(ResourceLocation(name)) ?: throw IllegalStateException("Item: $name does not exist"))
    }
}

fun JsonObject.addProp(property: String, value: Number) : JsonObject
{
    this.addProperty(property, value)
    return this
}