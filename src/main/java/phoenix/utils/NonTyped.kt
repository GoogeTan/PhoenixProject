package phoenix.utils

import com.google.gson.JsonObject
import net.minecraft.block.Block
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.ShapedRecipe
import net.minecraft.util.JSONUtils
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.world.World

fun World.destroyBlock(pos : BlockPos, shouldDrop : Boolean, entity : Entity?, stack : ItemStack) : Boolean
{
    val state = this.getBlockState(pos)
    return if (state.isAir(this, pos))
    {
        false
    } else
    {
        val fluidState = this.getFluidState(pos)
        if (shouldDrop)
        {
            val tile = if (state.hasTileEntity()) this.getTileEntity(pos) else null
            Block.spawnDrops(state, this, pos, tile, entity, stack)
        }
        this.setBlockState(pos, fluidState.blockState, 3)
    }
}

fun JsonObject.getFloat(nameIn: String, fallback : Float)           = JSONUtils.getFloat (this, nameIn, fallback)
fun JsonObject.getInt(nameIn: String)                               = JSONUtils.getInt   (this, nameIn)
fun JsonObject.getString(nameIn: String, fallback : String): String = JSONUtils.getString(this, nameIn, fallback)

fun JsonObject.readItemStack(nameIn: String): ItemStack
{
    return if (get(nameIn).isJsonObject) ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(this, nameIn)) else
    {
        val name = JSONUtils.getString(this, nameIn)
        ItemStack(Registry.ITEM.getValue(ResourceLocation(name)).orElseThrow { IllegalStateException("Item: $name does not exist") })
    }
}

fun ItemStack.getEnchantmentLevel(enchantment: Enchantment) = EnchantmentHelper.getEnchantmentLevel(enchantment, this)