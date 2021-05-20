package phoenix.utils

import com.google.common.collect.ImmutableMap
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
import net.minecraft.fluid.Fluid
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.ShapedRecipe
import net.minecraft.network.PacketBuffer
import net.minecraft.state.IProperty
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityType
import net.minecraft.util.Direction
import net.minecraft.util.Hand
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
import net.minecraft.world.gen.IExtendedNoiseRandom
import net.minecraft.world.gen.area.IArea
import net.minecraft.world.gen.area.IAreaFactory
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.IFeatureConfig
import net.minecraft.world.gen.feature.NoFeatureConfig
import net.minecraft.world.gen.feature.OreFeatureConfig
import net.minecraft.world.gen.feature.structure.Structure
import net.minecraft.world.gen.layer.traits.IAreaTransformer0
import net.minecraft.world.gen.layer.traits.IAreaTransformer1
import net.minecraft.world.gen.layer.traits.IAreaTransformer2
import net.minecraft.world.gen.placement.CountRangeConfig
import net.minecraft.world.gen.placement.IPlacementConfig
import net.minecraft.world.gen.placement.Placement
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.fluids.FluidActionResult
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.FluidUtil
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.fluids.capability.IFluidHandler
import net.minecraftforge.fml.RegistryObject
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.IForgeRegistryEntry
import phoenix.client.gui.diaryPages.Chapters
import phoenix.init.PhxBlocks
import phoenix.network.NetworkHandler
import phoenix.network.SyncBookPacket
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import java.util.*
import javax.annotation.Nonnull
import kotlin.math.abs
import kotlin.math.roundToInt

data class MTuple<V, M, K>(var first: V, var second: M, var third: K)
data class MPair<V, M>(var first: V, var second: M)
{
    operator fun not() : MPair<M, V> = MPair(second, first)

    operator fun<T> contains(v : T) : Boolean = v != null && first == v || second == v
}

inline fun<V, M> mpairOf(first: V, second: M) = MPair(first, second)
inline fun<V, M> uniquePairOf(first: V, second: M) : MPair<V?, M?> = if (first != second) MPair(first, second) else MPair(null, second)

inline fun World.destroyBlock(pos: BlockPos, shouldDrop: Boolean, entity: Entity?, stack: ItemStack) : Boolean
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

inline fun <T : Comparable<T>, V : T> World.setProperty(pos: BlockPos, property: IProperty<T>, value: V) : Boolean
{
    val state = world.getBlockState(pos)
    return if(state.has(property))
    {
        world.setBlockState(pos, state.with(property, value))
        true
    }
    else
        false
}

inline fun ItemStack.getFluidContained() = FluidUtil.getFluidContained(this).orElse(FluidStack.EMPTY)
inline fun PlayerEntity.getFluidContainedInHand(hand: Hand) = FluidUtil.getFluidContained(this.getHeldItem(hand)).orElse(
    FluidStack.EMPTY
)

inline fun <V : IForgeRegistryEntry<V>> KDeferredRegister<V>.register(name: String, value: V) = register(name) { value }

inline fun <T : TileEntity> TileEntityType.Builder<T>.build() = this.build(null)

inline fun JsonObject.getFloat(nameIn: String, fallback: Float)           = JSONUtils.getFloat(this, nameIn, fallback)
inline fun JsonObject.getInt(nameIn: String)                               = JSONUtils.getInt(this, nameIn)
inline fun JsonObject.getString(nameIn: String, fallback: String): String = JSONUtils.getString(this, nameIn, fallback)

inline fun JsonObject.readItemStack(nameIn: String): ItemStack
{
    return if (get(nameIn).isJsonObject) ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(this, nameIn)) else
    {
        val name = JSONUtils.getString(this, nameIn)
        ItemStack(
            Registry.ITEM.getValue(ResourceLocation(name))
                .orElseThrow { IllegalStateException("Item: $name does not exist") })
    }
}

inline fun ItemStack.getEnchantmentLevel(enchantment: Enchantment) = EnchantmentHelper.getEnchantmentLevel(
    enchantment,
    this
)

inline fun IWorld.getDownHeight(pos: BlockPos, max: Int): BlockPos
{
    val pos2 = BlockPos(pos.x, 0, pos.z)
    for (i in 0 until max)
    {
        if (!this.isAirBlock(pos2.add(0, i, 0))) return pos2.add(0, i - 1, 0)
    }
    return pos
}

inline fun Random.nextInt(min: Int, max: Int) = (min - 0.5 + this.nextDouble() * (max - min + 1)).roundToInt()

inline fun PacketBuffer.writeDate(date: Date)
{
    this.writeLong(date.minute)
    this.writeLong(date.day)
    this.writeLong(date.year)
}

inline fun PacketBuffer.readDate() : Date = Date(readLong(), readLong(), readLong())

inline fun <T : Number> min(vararg vals: T) : T
{
    if(vals.isEmpty())
        throw NullPointerException()
    var res : T = vals[0]
    for(i in vals)
    {
        if(i.toDouble() < res.toDouble())
            res = i
    }
    return res
}

inline fun <T : Number> max(vararg vals: T) : T
{
    if(vals.isEmpty())
        throw NullPointerException()
    var res : T = vals[0]
    for(i in vals)
    {
        if(i.toDouble() > res.toDouble())
            res = i
    }
    return res
}


inline fun <T : TileEntity> create(tile: T, block: Block) : () -> TileEntityType<T> = { TileEntityType.Builder.create(
    { tile },
    block
).build(null) }

inline fun <T : TileEntity> create(tile: T, block: RegistryObject<Block>) : () -> TileEntityType<T> = { TileEntityType.Builder.create(
    { tile },
    block.get()
).build(null) }

inline fun <T : IForgeRegistryEntry<T>> DeferredRegister<T>.registerValue(nameIn: String, value: T): RegistryObject<T> = this.register(
    nameIn
) { value }

inline fun FontRenderer.drawCenterAlignedString(string: ITextComponent, x: Float, y: Float)
{
    drawString(string.formattedText, x, y, BossInfo.Color.RED.ordinal)
}
private const val daysAYear = 319
private const val dayLength = 12000
private const val secondLength = 12000
fun World.getDate() = Date(
    (795 + dayTime) % dayLength / secondLength,
    (gameTime + 2005) % daysAYear,
    (gameTime + 2005) / daysAYear
)

inline fun ServerPlayerEntity.addChapter(chapter: Chapters)
{
    if(this is IPhoenixPlayer)
    {
        this.addChapter(chapter.id, world.getDate())
        NetworkHandler.sendTo(SyncBookPacket(this.getOpenedChapters()), this)
        sendMessage("Chapters ${getOpenedChapters()}")
    }
}

inline fun <reified T> IWorld.getTileAt(pos: BlockPos): T?
{
    val tile = getTileEntity(pos)
    return if(tile is T) tile else null
}

inline fun IWorld.getTileAt(pos: BlockPos) = getTileAt<TileEntity>(pos)

inline fun JsonObject.addProp(property: String, value: Number) : JsonObject
{
    this.addProperty(property, value)
    return this
}


val mc : Minecraft
    @OnlyIn(Dist.CLIENT)
    inline get() = Minecraft.getInstance()
val clientPlayer : ClientPlayerEntity?
    @OnlyIn(Dist.CLIENT)
    inline get() = mc.player
val clientWorld : ClientWorld?
    @OnlyIn(Dist.CLIENT)
    inline get() = mc.world

inline fun PlayerEntity.sendMessage(text: String) = sendMessage(StringTextComponent(text))

class BookException(message: String) : Exception(message)

inline fun Biome.addStructure(structure: Structure<NoFeatureConfig>)
{
    addStructure(structure.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG))
    addFeature(
        GenerationStage.Decoration.SURFACE_STRUCTURES,
        structure.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG)
            .withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG))
    )
}

inline fun Biome.addZirconiumOre()
{
    addFeature(
        GenerationStage.Decoration.UNDERGROUND_ORES,
        Feature.ORE.withConfiguration(
            OreFeatureConfig(
                OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                PhxBlocks.zirconium.defaultState,
                4
            )
        ).withPlacement(
            Placement.COUNT_RANGE.configure(CountRangeConfig(20, 0, 0, 64))
        )
    )
}

inline operator fun <R : IArea> IAreaTransformer1.invoke(context: IExtendedNoiseRandom<R>, area: IAreaFactory<R>) : IAreaFactory<R> = apply(
    context,
    area
)
inline operator fun <R : IArea> IAreaTransformer2.invoke(
    context: IExtendedNoiseRandom<R>,
    area1: IAreaFactory<R>,
    area2: IAreaFactory<R>
) : IAreaFactory<R> = apply(context, area1, area2)
inline operator fun <R : IArea> IAreaTransformer0.invoke(context: IExtendedNoiseRandom<R>) : IAreaFactory<R> = apply(
    context
)
inline fun TileEntity.getFluid(direction: Direction?) : LazyOptional<IFluidHandler> = getCapability(
    CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,
    direction
)

fun interactWithFluidHandler
    (
        container: ItemStack,
        fluidHandler: IFluidHandler?,
        player: PlayerEntity?
    ): FluidActionResult
{
    if (container.isEmpty || fluidHandler == null || player == null)
        return FluidActionResult.FAILURE

    val playerInventory: IItemHandler = PlayerMainInvWrapper(player.inventory)
    val fillResult = FluidUtil.tryFillContainerAndStow(container, fluidHandler, playerInventory, Int.MAX_VALUE, player, true)
    return if (fillResult.isSuccess)
        fillResult
     else
        FluidUtil.tryEmptyContainerAndStow(container, fluidHandler, playerInventory, Int.MAX_VALUE, player, true)
}

inline fun areFluidsCompatible(f: FluidStack, s: FluidStack) : Boolean = f.isEmpty xor s.isEmpty || f.fluid === s.fluid

fun interactBetweenPipes(f: IFluidHandler, s: IFluidHandler, max: Int) : Boolean
{
    val ff = f.getFluidInTank(0)
    val sf = s.getFluidInTank(0)
    return if (areFluidsCompatible(sf, ff))
    {
        val amount = min(abs(ff.amount - sf.amount) / 2, max, f.getTankCapacity(0) - ff.amount, s.getTankCapacity(0) - sf.amount)
        if(amount != 0)
        {
            if (ff.amount > sf.amount)
            {
                s.fill(f.drain(amount, IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE)
            } else
            {
                f.fill(s.drain(amount, IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE)
            }
        }
        amount != 0
    } else false
}


val next: ImmutableMap<Direction, Direction> = ImmutableMap.of(Direction.NORTH, Direction.EAST, Direction.EAST, Direction.SOUTH, Direction.SOUTH, Direction.WEST, Direction.WEST, Direction.NORTH)

inline fun Direction.next() : Direction = next[this] ?: Direction.NORTH

val axisToFace: ImmutableMap<Direction.Axis, Direction> = ImmutableMap.of(Direction.Axis.X, Direction.NORTH, Direction.Axis.Z, Direction.SOUTH)

inline fun Direction.Axis.getMainDirection() : Direction = axisToFace[this] ?: Direction.NORTH