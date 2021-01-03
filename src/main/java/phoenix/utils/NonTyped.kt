package phoenix.utils

import com.google.gson.JsonObject
import net.minecraft.block.Block
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.ShapedRecipe
import net.minecraft.network.PacketBuffer
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityType
import net.minecraft.util.JSONUtils
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.world.IWorld
import net.minecraft.world.World
import net.minecraftforge.fml.RegistryObject
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.IForgeRegistryEntry
import java.util.*
import kotlin.math.roundToInt

data class Pair<M, V>(var v : V, var m : M)
data class Tuple<M, V, K>(var first : V, var second : M, var third : K)

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

fun IWorld.getDownHeight(pos : BlockPos, max: Int): BlockPos
{
    val pos2 = BlockPos(pos.x, 0, pos.z)
    for (i in 0 until max)
    {
        if (!this.isAirBlock(pos2.add(0, i, 0))) return pos2.add(0, i - 1, 0)
    }
    return pos
}

fun Random.nextInt(min : Int, max : Int) = (min - 0.5 + this.nextDouble() * (max - min + 1)).roundToInt()

fun PacketBuffer.writeDate(date : Date)
{
    this.writeInt(date.minute)
    this.writeInt(date.day)
    this.writeInt(date.year)
}

fun PacketBuffer.readDate() : Date
{
    val res = Date(0, 0, 0)
    res.minute = readInt()
    res.day = readInt()
    res.year = readInt()
    return res;
}

fun<T : TileEntity> create(tile: T, block: Block) : () -> TileEntityType<T> = { TileEntityType.Builder.create({ tile }, block).build(null) }

fun<T : TileEntity> create(tile: T, block: RegistryObject<Block>) : () -> TileEntityType<T> = { TileEntityType.Builder.create({ tile }, block.get()).build(null) }

fun<T : IForgeRegistryEntry<T>> DeferredRegister<T>.registerValue(nameIn: String, value : T): RegistryObject<T> = this.register(nameIn) { value }