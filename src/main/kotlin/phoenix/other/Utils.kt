@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package phoenix.other

import com.google.common.collect.ImmutableMap
import com.google.gson.JsonObject
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.player.ClientPlayerEntity
import net.minecraft.client.gui.AbstractGui
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.texture.TextureManager
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
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
import net.minecraft.util.math.Vec3d
import net.minecraft.util.text.LanguageMap
import net.minecraft.util.text.StringTextComponent
import net.minecraft.util.text.TextFormatting
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
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilderConfig
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder
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
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.IForgeRegistryEntry
import org.lwjgl.opengl.GL11
import phoenix.Phoenix
import phoenix.api.entity.Date
import phoenix.api.entity.IPhoenixPlayer
import phoenix.client.gui.diary.Chapter
import phoenix.client.gui.diary.elements.ADiaryElement
import phoenix.client.gui.diary.elements.TextElement
import phoenix.init.PhxBlocks
import phoenix.mixin.serverInstance
import phoenix.network.SyncBookPacket
import phoenix.network.sendToPlayer
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import java.math.BigDecimal
import java.util.*
import kotlin.math.roundToInt
import kotlin.math.sqrt

fun <V : IForgeRegistryEntry<V>> KDeferredRegister<V>.register(name: String, value: V) = register(name) { value }

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
fun <T : TileEntity> TileEntityType.Builder<T>.build(): TileEntityType<T> = this.build(null)

fun ItemStack.getEnchantmentLevel(enchantment: Enchantment) = EnchantmentHelper.getEnchantmentLevel(enchantment, this)


//fun <T : TileEntity> create(tile: T, block: Block) : () -> TileEntityType<T> = { TileEntityType.Builder.create({ tile }, block).build() }
//fun <T : TileEntity> create(tile: T, block: RegistryObject<Block>) : () -> TileEntityType<T> = { TileEntityType.Builder.create({ tile }, block.get()).build() }

val mc : Minecraft?
    @OnlyIn(Dist.CLIENT)
    inline get()
    {
        return try
        {
            Minecraft.getInstance()
        } catch (e : Throwable)
        {
            null
        }
    }
val clientPlayer : ClientPlayerEntity?
    @OnlyIn(Dist.CLIENT)
    inline get() = mc?.player
val clientWorld : ClientWorld?
    @OnlyIn(Dist.CLIENT)
    inline get() = mc?.world
val World.isServer
    get() = !this.isRemote

val PlayerEntity.isServer
    get() = !world.isRemote

val PlayerEntity.isRemote
    get() = world.isRemote

val server : MinecraftServer?
    get() = mc?.integratedServer ?: serverInstance

val textureManager : TextureManager?
    get() = mc?.textureManager

val itemRenderer : ItemRenderer?
    get() = mc?.itemRenderer

val font : FontRenderer?
    get() = mc?.fontRenderer

fun PlayerEntity.sendMessage(text: String) = sendMessage(StringTextComponent(text))

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

fun<T> client(task : (mc : Minecraft, player : ClientPlayerEntity?, world : ClientWorld?) -> T) : T = DistExecutor.safeCallWhenOn(Dist.CLIENT) { DistExecutor.SafeCallable { task(mc!!, clientPlayer, clientWorld) }}
fun<T> server(task : (MinecraftServer?) -> T) : T = DistExecutor.safeCallWhenOn(Dist.DEDICATED_SERVER) { DistExecutor.SafeCallable { task(server) } }

