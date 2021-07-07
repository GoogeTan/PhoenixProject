package phoenix.utils

import com.google.common.collect.ImmutableMap
import com.google.gson.JsonObject
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.player.ClientPlayerEntity
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.renderer.ItemRenderer
import net.minecraft.client.renderer.texture.TextureManager
import net.minecraft.client.world.ClientWorld
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.ShapedRecipe
import net.minecraft.network.PacketBuffer
import net.minecraft.server.MinecraftServer
import net.minecraft.state.IProperty
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityType
import net.minecraft.util.Direction
import net.minecraft.util.JSONUtils
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.util.text.StringTextComponent
import net.minecraft.world.IWorld
import net.minecraft.world.IWorldReader
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraft.world.chunk.IChunk
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
import net.minecraftforge.fluids.capability.templates.FluidTank
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.fml.RegistryObject
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper
import net.minecraftforge.registries.IForgeRegistryEntry
import phoenix.Phoenix
import phoenix.api.entity.Date
import phoenix.api.entity.IPhoenixPlayer
import phoenix.client.gui.diaryPages.Chapter
import phoenix.init.PhxBlocks
import phoenix.mixin.serverInstance
import phoenix.network.NetworkHandler
import phoenix.network.SyncBookPacket
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import java.util.*
import kotlin.math.roundToInt

data class MutableTuple<V, M, K>(var first: V, var second: M, var third: K)
data class MutablePair<V, M>(var first: V, var second: M)
{
    operator fun not() : MutablePair<M, V> = MutablePair(second, first)

    operator fun<T> contains(v : T) : Boolean = v != null && first == v || second == v
}

fun<V, M> uniquePairOf(first: V? = null, second: M? = null) : MutablePair<V?, M?> = if (first != second) MutablePair(first, second) else MutablePair(null, second)

fun World.destroyBlock(pos: BlockPos, shouldDrop: Boolean, entity: Entity?, stack: ItemStack) : Boolean
{
    val state = this[pos]
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
        this.setBlockState(pos, fluidState.blockState)
    }
}

operator fun <T : Comparable<T>, V : T> World.set(pos: BlockPos, property: IProperty<T>, value: V)
{
    val state = world[pos]
    world[pos] = state.with(property, value)
}

operator fun IWorldReader.get(pos: BlockPos): BlockState = getBlockState(pos)
operator fun <T : Comparable<T>> World.get(pos: BlockPos, property: IProperty<T>) : T = this[pos][property]
operator fun IWorld.get(pos: BlockPos) : BlockState = this.getBlockState(pos)
operator fun IWorld.set(pos: BlockPos, state : BlockState, flag : Int) : Boolean = this.setBlockState(pos, state, flag)
operator fun IWorld.set(pos: BlockPos, state : BlockState) : Boolean = this.setBlockState(pos, state, 3)

operator fun IChunk.get(pos: BlockPos) : BlockState = this.getBlockState(pos)
operator fun IChunk.set(pos: BlockPos, state : BlockState) = this.setBlockState(pos, state, false)
operator fun IChunk.set(pos: BlockPos, state : BlockState, isMoving : Boolean) = this.setBlockState(pos, state, isMoving)

fun <V : IForgeRegistryEntry<V>> KDeferredRegister<V>.register(name: String, value: V) = register(name) { value }

fun <T : TileEntity> TileEntityType.Builder<T>.build(): TileEntityType<T> = this.build(null)

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
        //ItemStack(ForgeRegistries.ITEMS.getValue(ResourceLocation(name))!!.item)
        ItemStack(Registry.ITEM.getValue(ResourceLocation(name)).orElseThrow { IllegalStateException("Item: $name does not exist") })
    }
}

fun ItemStack.getEnchantmentLevel(enchantment: Enchantment) = EnchantmentHelper.getEnchantmentLevel(enchantment, this)

fun IWorld.getDownHeight(pos: BlockPos, max: Int): BlockPos
{
    val pos2 = BlockPos(pos.x, 0, pos.z)
    for (i in 0 until max)
    {
        if (!this.isAirBlock(pos2.add(0, i, 0))) return pos2.add(0, i - 1, 0)
    }
    return pos
}

fun Random.nextInt(min: Int, max: Int) = (min - 0.5 + this.nextDouble() * (max - min + 1)).roundToInt()

fun PacketBuffer.writeDate(date: Date)
{
    this.writeLong(date.minute)
    this.writeLong(date.day)
    this.writeLong(date.year)
}

fun PacketBuffer.readDate() : Date = Date(readLong(), readLong(), readLong())

fun <T : Number> min(vararg values: T) : T
{
    if(values.isEmpty())
        throw NullPointerException()
    var res : T = values[0]
    for(i in values)
    {
        if(i.toDouble() < res.toDouble())
            res = i
    }
    return res
}

fun <T : Number> max(vararg values: T) : T
{
    if(values.isEmpty())
        throw NullPointerException()
    var res : T = values[0]
    for(i in values)
    {
        if(i.toDouble() > res.toDouble())
            res = i
    }
    return res
}


fun <T : TileEntity> create(tile: T, block: Block) : () -> TileEntityType<T> = { TileEntityType.Builder.create({ tile }, block).build() }
fun <T : TileEntity> create(tile: T, block: RegistryObject<Block>) : () -> TileEntityType<T> = { TileEntityType.Builder.create({ tile }, block.get()).build() }

private const val daysAYear = 319
private const val dayLength = 12000
private const val secondLength = 12000

fun World.getDate() = Date((795 + dayTime) % dayLength / secondLength, (gameTime + 2005) % daysAYear, (gameTime + 2005) / daysAYear)

fun ServerPlayerEntity.addChapter(chapter: Chapter?)
{
    if (chapter == null)
        return
    if(this is IPhoenixPlayer)
    {
        this.addChapter(chapter.id, world.getDate())
        SyncBookPacket(this.getOpenedChapters()).sendTo(this)
        sendMessage("Chapter ${getOpenedChapters()}")
    }
}

fun IntRange.toSet() : MutableSet<Int>
{
    val res = kotlin.collections.HashSet<Int>()
    for (i in this)
        res.add(i)
    return res
}

inline fun <reified T> IWorld.getTileAt(pos: BlockPos): T?
{
    val tile = getTileEntity(pos)
    return if(tile is T) tile else null
}

fun IWorld.getTileAt(pos: BlockPos) = getTileAt<TileEntity>(pos)

fun JsonObject.addProp(property: String, value: Number) : JsonObject
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
val World.isServer
    get() = !this.isRemote

val PlayerEntity.isServer
    get() = !world.isRemote

val PlayerEntity.isRemote
    get() = world.isRemote

val server : MinecraftServer?
    get() = try { mc.integratedServer } catch(e : Throwable) { serverInstance }

val textureManager : TextureManager
    get() = Minecraft.getInstance().textureManager

val itemRenderer : ItemRenderer
    get() = Minecraft.getInstance().itemRenderer

val font : FontRenderer
    get() = Minecraft.getInstance().fontRenderer

fun PlayerEntity.sendMessage(text: String) = sendMessage(StringTextComponent(text))

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
        Feature.ORE.withConfiguration(
            OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, PhxBlocks.zirconium.defaultState, 4)
        ).withPlacement(
            Placement.COUNT_RANGE.configure(CountRangeConfig(20, 0, 0, 64))
        )
    )
}

operator fun <R : IArea> IAreaTransformer1.invoke(context: IExtendedNoiseRandom<R>, area: IAreaFactory<R>) : IAreaFactory<R> = apply(context, area)
operator fun <R : IArea> IAreaTransformer2.invoke(context: IExtendedNoiseRandom<R>, area1: IAreaFactory<R>, area2: IAreaFactory<R>) : IAreaFactory<R> = apply(context, area1, area2)
operator fun <R : IArea> IAreaTransformer0.invoke(context: IExtendedNoiseRandom<R>) : IAreaFactory<R> = apply(context)

fun TileEntity.getFluid(direction: Direction?) : LazyOptional<IFluidHandler> = getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction)

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

fun areFluidsCompatible(f: FluidStack, s: FluidStack) : Boolean = f.isEmpty xor s.isEmpty || f.fluid === s.fluid

val next: ImmutableMap<Direction, Direction> = ImmutableMap.of(Direction.NORTH, Direction.EAST, Direction.EAST, Direction.SOUTH, Direction.SOUTH, Direction.WEST, Direction.WEST, Direction.NORTH)

fun Direction.next() : Direction = next[this] ?: Direction.NORTH

fun<T> client(task : (mc : Minecraft, player : ClientPlayerEntity?, world : ClientWorld?) -> T) : T = DistExecutor.safeCallWhenOn(Dist.CLIENT) { DistExecutor.SafeCallable { task(mc, clientPlayer, clientWorld) }}
fun<T> server(task : (MinecraftServer?) -> T) : T = DistExecutor.safeCallWhenOn(Dist.DEDICATED_SERVER) { DistExecutor.SafeCallable { task(server) } }

fun PacketBuffer.writeFluidTank(tank: FluidTank) : PacketBuffer
{
    this.writeFluidStack(tank.fluid)
    this.writeInt(tank.capacity)
    return this
}

fun PacketBuffer.readFluidTank(): FluidTank
{
    val stack = this.readFluidStack()
    val capacity = this.readInt()

    val res = FluidTank(capacity)
    res.fluid = stack
    return res
}

fun key  (name: String) = ResourceLocation(Phoenix.MOD_ID, name)
fun block(name: String) = ResourceLocation(Phoenix.MOD_ID, "textures/blocks/$name.png")