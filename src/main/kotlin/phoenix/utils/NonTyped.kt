package phoenix.utils

import com.google.gson.JsonObject
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.player.ClientPlayerEntity
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.world.ClientWorld
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.ShapedRecipe
import net.minecraft.network.PacketBuffer
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityType
import net.minecraft.util.JSONUtils
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import net.minecraft.world.BossInfo
import net.minecraft.world.IWorld
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.GenerationStage
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.IFeatureConfig
import net.minecraft.world.gen.feature.NoFeatureConfig
import net.minecraft.world.gen.feature.OreFeatureConfig
import net.minecraft.world.gen.feature.structure.Structure
import net.minecraft.world.gen.placement.CountRangeConfig
import net.minecraft.world.gen.placement.IPlacementConfig
import net.minecraft.world.gen.placement.Placement
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.fml.RegistryObject
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.IForgeRegistryEntry
import phoenix.client.gui.diaryPages.Chapters
import phoenix.init.PhoenixBlocks
import phoenix.network.NetworkHandler
import phoenix.network.SyncBookPacket
import java.util.*
import kotlin.math.roundToInt

data class Tuple<V, M, K>(var first : V, var second : M, var third : K)

inline fun World.destroyBlock(pos : BlockPos, shouldDrop : Boolean, entity : Entity?, stack : ItemStack) : Boolean
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

inline fun JsonObject.getFloat(nameIn: String, fallback : Float)           = JSONUtils.getFloat (this, nameIn, fallback)
inline fun JsonObject.getInt(nameIn: String)                               = JSONUtils.getInt   (this, nameIn)
inline fun JsonObject.getString(nameIn: String, fallback : String): String = JSONUtils.getString(this, nameIn, fallback)

inline fun JsonObject.readItemStack(nameIn: String): ItemStack
{
    return if (get(nameIn).isJsonObject) ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(this, nameIn)) else
    {
        val name = JSONUtils.getString(this, nameIn)
        ItemStack(Registry.ITEM.getValue(ResourceLocation(name)).orElseThrow { IllegalStateException("Item: $name does not exist") })
    }
}

inline fun ItemStack.getEnchantmentLevel(enchantment: Enchantment) = EnchantmentHelper.getEnchantmentLevel(enchantment, this)

fun IWorld.getDownHeight(pos : BlockPos, max: Int): BlockPos
{
    val pos2 = BlockPos(pos.x, 0, pos.z)
    for (i in 0 until max)
    {
        if (!this.isAirBlock(pos2.add(0, i, 0))) return pos2.add(0, i - 1, 0)
    }
    return pos
}

inline fun Random.nextInt(min : Int, max : Int) = (min - 0.5 + this.nextDouble() * (max - min + 1)).roundToInt()

fun PacketBuffer.writeDate(date : Date)
{
    this.writeLong(date.minute)
    this.writeLong(date.day)
    this.writeLong(date.year)
}

inline fun PacketBuffer.readDate() : Date = Date(readLong(), readLong(), readLong())

fun<T : TileEntity> create(tile: T, block: Block) : () -> TileEntityType<T> = { TileEntityType.Builder.create({ tile }, block).build(null) }

fun<T : TileEntity> create(tile: T, block: RegistryObject<Block>) : () -> TileEntityType<T> = { TileEntityType.Builder.create({ tile }, block.get()).build(null) }

fun<T : IForgeRegistryEntry<T>> DeferredRegister<T>.registerValue(nameIn: String, value : T): RegistryObject<T> = this.register(nameIn) { value }

fun FontRenderer.drawCenterAlignedString(string : ITextComponent, x : Float, y : Float)
{
    drawString(string.formattedText, x, y, BossInfo.Color.RED.ordinal)
}
private const val daysAYear = 319
private const val dayLength = 12000
private const val secondLength = 12000
fun World.getDate() = Date((795 + dayTime) % dayLength / secondLength, (gameTime + 2005) % daysAYear, (gameTime + 2005) / daysAYear)

fun ServerPlayerEntity.addChapter(chapter : Chapters)
{
    if(this is IPhoenixPlayer)
    {
        this.addChapter(chapter.id, world.getDate())
        NetworkHandler.sendTo(SyncBookPacket(this.getOpenedChapters()), this)
        sendMessage("Chapters ${getOpenedChapters()}")
    }
}

fun <T> IWorld.getTileAt(pos: BlockPos): T?
{
    return getTileEntity(pos) as? T
}

fun JsonObject.addProp( property : String,  value : Number) : JsonObject
{
    this.addProperty(property, value)
    return this
}


val mc : Minecraft
        @OnlyIn(Dist.CLIENT)
        get() = Minecraft.getInstance()
val clientPlayer : ClientPlayerEntity?
        @OnlyIn(Dist.CLIENT)
        get() = mc.player
val clientWorld : ClientWorld?
    @OnlyIn(Dist.CLIENT)
    get() = mc.world

fun PlayerEntity.sendMessage(text : String) = sendMessage(StringTextComponent(text))

class BookException(message: String) : Exception(message)

fun Biome.addStructure(structure: Structure<NoFeatureConfig>)
{
    addStructure(structure.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG))
    addFeature(
        GenerationStage.Decoration.SURFACE_STRUCTURES,
        structure.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG)
            .withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG))
    )
}

fun Biome.addZirconiumOre()
{
    addFeature(
        GenerationStage.Decoration.UNDERGROUND_ORES,
        Feature.ORE.withConfiguration(OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, PhoenixBlocks.ZIRCONIUM.defaultState, 4)).withPlacement(
            Placement.COUNT_RANGE.configure(CountRangeConfig(20, 0, 0, 64)))
    )
}