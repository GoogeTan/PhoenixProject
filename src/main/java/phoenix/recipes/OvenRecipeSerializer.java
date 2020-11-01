package phoenix.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;

public class OvenRecipeSerializer<T extends OvenRecipe> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T>
{
    private final int cookingTime;
    private final IFactory<T> factory;

    public OvenRecipeSerializer(IFactory<T> factory, int cookingTime)
    {
        this.cookingTime = cookingTime;
        this.factory = factory;
    }

    @Nonnull
    @Override
    public T read(ResourceLocation recipeId, JsonObject json)
    {
        String s = JSONUtils.getString(json, "group", "");
        JsonElement jsonelement = JSONUtils.isJsonArray(json, "ingredient") ? JSONUtils.getJsonArray(json, "ingredient") : JSONUtils.getJsonObject(json, "ingredient");
        Ingredient ingredient = Ingredient.deserialize(jsonelement);
        if (!json.has("result"))
            throw new JsonSyntaxException("Missing result, expected to find a string or object");
        ItemStack itemstack;
        if (json.get("result").isJsonObject())
            itemstack = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
        else
        {
            String s1 = JSONUtils.getString(json, "result");
            ResourceLocation resourcelocation = new ResourceLocation(s1);
            itemstack = new ItemStack(Registry.ITEM.getValue(resourcelocation).orElseThrow(() -> new IllegalStateException("Item: " + s1 + " does not exist")));
        }
        float exp = JSONUtils.getFloat(json, "experience", 0.0F);
        int cookingtime = JSONUtils.getInt(json, "cookingtime", this.cookingTime);
        return this.factory.create(recipeId, s, ingredient, itemstack, exp, cookingtime);
    }

    public T read(ResourceLocation recipeId, PacketBuffer buffer)
    {
        String     group      = buffer.readString(32767);
        Ingredient ingredient = Ingredient.read(buffer);
        ItemStack  result     = buffer.readItemStack();
        float      exp        = buffer.readFloat();
        int        cookTime   = buffer.readVarInt();
        cookTime = ((Object) cookingTime) == null ? 40 : cookingTime;
        return this.factory.create(recipeId, group, ingredient, result, exp, cookTime);
    }

    public void write(PacketBuffer buffer, T recipe)
    {
        buffer.writeString(recipe.getGroup());
        recipe.getIngredient().write(buffer);
        buffer.writeItemStack(recipe.getResult());
        buffer.writeFloat(recipe.getExperience());
        buffer.writeVarInt(recipe.getCookTime());
    }

    public interface IFactory<T extends AbstractCookingRecipe>
    {
        T create(ResourceLocation id, String group, Ingredient ingredient, ItemStack result, float exp, int cookTime);
    }
}