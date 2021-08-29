@file:Suppress("NOTHING_TO_INLINE")

package phoenix.other

import com.google.common.collect.ImmutableMap
import net.minecraft.client.gui.FontRenderer
import net.minecraft.network.PacketBuffer
import net.minecraft.util.Direction
import net.minecraft.util.ResourceLocation
import net.minecraft.util.Rotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.LanguageMap
import net.minecraft.util.text.StringTextComponent
import net.minecraft.util.text.TextFormatting
import net.minecraft.util.text.TranslationTextComponent
import net.minecraftforge.fluids.capability.templates.FluidTank
import net.minecraftforge.registries.IForgeRegistryEntry
import phoenix.MOD_ID
import phoenix.Phoenix
import phoenix.api.entity.Date
import phoenix.client.gui.diary.elements.ADiaryElement
import phoenix.client.gui.diary.elements.TextElement
import phoenix.other.collections.SizedArrayList
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import java.math.BigDecimal
import java.util.*
import kotlin.Comparator
import kotlin.math.roundToInt

fun String.toWords(): ArrayList<String>
{
    val result = ArrayList<String>()
    var current = ""
    for (i in this.indices)
    {
        if (this[i] == '\n')
        {
            result.add(current)
            result.add("\n")
            current = ""
        }
        else if (this[i] == ' ' || i == this.length - 1)
        {
            if (this[i] != ' ')
                current += this[i]
            result.add(current)
            current = ""
        }
        else
        {
            current += this[i]
        }
    }
    return result
}

fun String.translate(): String = LanguageMap.getInstance().translateKey(this)

val rainbow = arrayOf(
    TextFormatting.RED,
    TextFormatting.YELLOW,
    TextFormatting.GREEN,
    TextFormatting.BLUE,
    TextFormatting.DARK_BLUE,
    TextFormatting.DARK_PURPLE
)

fun rainbow(string: String): String
{
    val s = StringBuilder()
    for (i in string.indices)
    {
        if (string[i] != ' ' && string[i] != '\n') s.append(rainbow[i % rainbow.size])
        s.append(string[i])
    }
    return s.toString()
}

fun makeParagraph(font: FontRenderer, xSize: Int, vararg text: String): ArrayList<ADiaryElement>
{
    val res = ArrayList<ADiaryElement>()
    val words: MutableList<String> = ArrayList()
    for (current in text)  //проходим по всем артиклам
    {
        words.addAll(current.toWords())
        words.add("\n")
    }

    var i = 0
    while (i < words.size)
    {
        var stringToAdd = ""
        var nextWord = words[i]

        while (i < words.size && font.getStringWidth("$stringToAdd $nextWord") < xSize * 1.8)
        {
            stringToAdd += " $nextWord"
            i++
            if (i < words.size)
                nextWord = words[i]
            if (nextWord == "\n")
                break
        }

        if(stringToAdd.isNotEmpty())
            res.add(TextElement(stringToAdd))
    }

    return res
}

fun keyOf  (name: String) = ResourceLocation(MOD_ID, name)
fun blockOf(name: String) = ResourceLocation(MOD_ID, "textures/blocks/$name.png")

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

fun PacketBuffer.readDate() : Date = Date(readLong(), readLong(), readLong())

fun PacketBuffer.writeDate(date: Date)
{
    this.writeLong(date.minute)
    this.writeLong(date.day)
    this.writeLong(date.year)
}

inline fun String.toTextComponent() = StringTextComponent(this)
inline fun String.toTranslationTextComponent() = TranslationTextComponent(this)

data class MutableTuple<V, M, K>(var first: V, var second: M, var third: K)

data class MutablePair<V, M>(var first: V, var second: M)
{
    operator fun not() : MutablePair<M, V> = MutablePair(second, first)

    operator fun<T> contains(v : T) : Boolean = v != null && (first == v || second == v)
}

fun<V : Comparable<V>, M : Comparable<M>> MutablePair<V, M>.compareTo(other: MutablePair<V, M>) : Int
{
    val i = first.compareTo(other.first)
    return if (i != 0) i else second.compareTo(other.second)
}

fun<V, M> uniquePairOf(first: V? = null, second: M? = null) : MutablePair<V?, M?> = if (first != second) MutablePair(first, second) else MutablePair(null, second)

fun Random.nextInt(min: Int, max: Int) = (min - 0.5 + this.nextDouble() * (max - min + 1)).roundToInt()

fun min(vararg values: Number) : Number
{
    if(values.isEmpty())
        throw ArrayIndexOutOfBoundsException()
    var res = values[0]
    for(i in values)
    {
        if (i.toBigDecimal() < res.toBigDecimal())
            res = i
    }
    return res
}

fun min(vararg values: Int) : Int
{
    if(values.isEmpty())
        throw ArrayIndexOutOfBoundsException()
    var res = values[0]
    for(i in values)
        if (i < res)
            res = i
    return res
}

fun min(vararg values : Float) : Float
{
    if(values.isEmpty())
        throw ArrayIndexOutOfBoundsException()
    var res = values[0]
    for(i in values)
        if (i < res)
            res = i
    return res
}

fun min(vararg values : Double) : Double
{
    if(values.isEmpty())
        throw ArrayIndexOutOfBoundsException()
    var res = values[0]
    for(i in values)
        if (i < res)
            res = i
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

fun Number.toBigDecimal() = BigDecimal(this.toString())

fun IntRange.toSet() : MutableSet<Int>
{
    val res = kotlin.collections.HashSet<Int>()
    for (i in this)
        res.add(i)
    return res
}

inline fun<T : Comparable<T>, K> comparePairByFirst() : Comparator<Pair<T, K>>  = Comparator { p1, p2 -> p1.first.compareTo(p2.first) }

inline fun<T> variable(task : () -> T) : T = task()

inline fun<T, Res> foreach(iterator: Iterator<T>, res : Res, tack : Res.(value : T) -> Unit) : Res
{
    while (iterator.hasNext())
        res.tack(iterator.next())
    return res
}

inline fun<T, Res> Iterable<T>.foreach(res : Res, tack : Res.(value : T) -> Unit) : Res
{
    val iterator = this.iterator()
    while (iterator.hasNext())
        res.tack(iterator.next())
    return res
}

fun<T, R> T?.ifNotNull(block : T.() -> R) : R? = if (this == null) null else block(this)

inline fun<T> MutableList<T>.unique(isNotEqual : (f : T, s : T) -> Boolean = { f, s -> f != s }) : ArrayList<T>
{
    val res = ArrayList<T>()
    for (i in this)
        if (res.isEmpty() || isNotEqual(res.last(), i))
            res.add(i)
    return res
}

inline fun<T, L : MutableList<T>> L.sortedBy(comparator: Comparator<T>) : L
{
    this.sortWith(comparator)
    return this
}

inline fun<T : Comparable<T>, L : MutableList<T>> L.sortedBy() : L
{
    this.sort()
    return this
}

val fromDirectionToRotationMap = ImmutableMap.of(Direction.NORTH, Rotation.NONE, Direction.EAST, Rotation.CLOCKWISE_90, Direction.SOUTH, Rotation.CLOCKWISE_180, Direction.WEST, Rotation.COUNTERCLOCKWISE_90)

fun rotationOf(direction: Direction) = fromDirectionToRotationMap[direction] ?: Rotation.NONE

fun BlockPos.rotate(rotation: Rotation) : BlockPos
{
    return when(rotation)
    {
        Rotation.NONE -> this
        Rotation.CLOCKWISE_90 -> BlockPos(-z, y, x)
        Rotation.CLOCKWISE_180 -> BlockPos(-z, y, -x)
        Rotation.COUNTERCLOCKWISE_90 -> BlockPos(-x, y, z)
    }
}

inline fun<Source, Result> Source.map(block: Source.() -> Result) = block(this)
