@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "NOTHING_TO_INLINE")

package phoenix.other

import com.google.common.collect.ImmutableMap
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.player.ClientPlayerEntity
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.renderer.ItemRenderer
import net.minecraft.client.renderer.texture.TextureManager
import net.minecraft.client.world.ClientWorld
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.MinecraftServer
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityType
import net.minecraft.util.Direction
import net.minecraft.util.text.StringTextComponent
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.fluids.FluidActionResult
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.FluidUtil
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.fluids.capability.IFluidHandler
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.fml.server.ServerLifecycleHooks
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper
import net.minecraftforge.registries.IForgeRegistryEntry
import thedarkcolour.kotlinforforge.forge.KDeferredRegister

fun <V : IForgeRegistryEntry<V>, T : V> KDeferredRegister<V>.register(name: String, value: T) = register(name) { value }

fun<T : TileEntity> KDeferredRegister<TileEntityType<*>>.register(name: String, value: () -> T, vararg block : Block) = register(name) { TileEntityType.Builder.create(value, *block).build() }

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
fun <T : TileEntity> TileEntityType.Builder<T>.build(): TileEntityType<T> = this.build(null)

fun ItemStack.getEnchantmentLevel(enchantment: Enchantment) = EnchantmentHelper.getEnchantmentLevel(enchantment, this)

@get:OnlyIn(Dist.CLIENT)
val mc : Minecraft?
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

@get:OnlyIn(Dist.CLIENT)
val clientPlayer : ClientPlayerEntity?
    inline get() = mc?.player
@get:OnlyIn(Dist.CLIENT)
val clientWorld : ClientWorld?
    inline get() = mc?.world

val World.isServer        get() = !this.isRemote
val PlayerEntity.isServer get() = !world.isRemote
val PlayerEntity.isRemote get() = world.isRemote

val server : MinecraftServer? get() = mc?.integratedServer ?: ServerLifecycleHooks.getCurrentServer()

@get:OnlyIn(Dist.CLIENT)
val textureManager : TextureManager?
    get() = mc?.textureManager

@get:OnlyIn(Dist.CLIENT)
val itemRenderer : ItemRenderer?
    get() = mc?.itemRenderer

@get:OnlyIn(Dist.CLIENT)
val font : FontRenderer?
    get() = mc?.fontRenderer

fun PlayerEntity.sendMessage(text: String) = sendMessage(StringTextComponent(text))

fun TileEntity.getFluid(direction: Direction?) : LazyOptional<IFluidHandler> = getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction)

fun interactWithFluidHandler(container: ItemStack, fluidHandler: IFluidHandler?, player: PlayerEntity?): FluidActionResult
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

fun<T> client(task : (mc : Minecraft, player : ClientPlayerEntity?, world : ClientWorld?) -> T) : T =
    DistExecutor.safeCallWhenOn(Dist.CLIENT) { DistExecutor.SafeCallable { task(mc!!, clientPlayer, clientWorld) }}
fun<T> server(task : (MinecraftServer?) -> T) : T =
    DistExecutor.safeCallWhenOn(Dist.DEDICATED_SERVER) { DistExecutor.SafeCallable { task(server) } }