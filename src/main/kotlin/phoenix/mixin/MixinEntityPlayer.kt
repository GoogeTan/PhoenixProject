package phoenix.mixin

import com.google.common.collect.ImmutableMap
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.IItemProvider
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import phoenix.api.entity.Date
import phoenix.api.entity.IPhoenixPlayer
import phoenix.client.gui.diary.Chapter
import phoenix.init.PhxItems
import phoenix.other.addChapter
import phoenix.other.toSet

@Mixin(PlayerEntity::class)
abstract class MixinEntityPlayer : IPhoenixPlayer
{
    private val chaptersSet: MutableSet<Int> = LinkedHashSet()
    private lateinit var chapters : ArrayList<Pair<Int, Date>>
    private lateinit var specialChaptersIndices : MutableSet<Int>

    @Inject(method = ["writeAdditional"], at = [At("TAIL")])
    fun onWriteEntityToNBT(nbt: CompoundNBT, ci: CallbackInfo?)
    {
        nbt.putInt("count_of_opened", chapters.size)
        for (i in chapters.indices)
        {
            nbt.putInt("id$i", chapters[i].first)
            nbt.putLong("min$i", chapters[i].second.minute)
            nbt.putLong("day$i", chapters[i].second.day)
            nbt.putLong("year$i", chapters[i].second.year)
        }


        specialChaptersIndices = HashSet()
        nbt.putInt("count_of_predicates", chapters.size)
        for (i in chapters.indices)
            nbt.putInt("index$i", i)
    }

    @Inject(method = ["readAdditional"], at = [At("TAIL")])
    fun onReadEntityFromNBT(nbt: CompoundNBT, ci: CallbackInfo?)
    {
        chapters = arrayListOf()

        if (nbt.contains("count_of_opened"))
        {
            val countOfOpened = nbt.getInt("count_of_opened")
            for (i in 0 until countOfOpened)
            {
                val id = nbt.getInt("id$i")
                val min = nbt.getLong("min$i")
                val day = nbt.getLong("day$i")
                val year = nbt.getLong("year$i")
                addChapter(id, Date(min, day, year))
            }
        }
        else if (chapters.isEmpty()) addChapter(0, Date((795 % 12000 / 100), (2005 % 319), (2005 / 319)))


        if (nbt.contains("count_of_predicates"))
        {
            specialChaptersIndices = mutableSetOf()
            val countOfPredicates = nbt.getInt("count_of_predicates")
            for (i in 0 until countOfPredicates)
                specialChaptersIndices.add(nbt.getInt("index$i"))
        }
        else specialChaptersIndices = specialChapters.indices.toSet()
    }

    @Deprecated("Use phoenix.other.addChapter(chapters : Chapter)")
    override fun addChapter(id: Int, date: Date): Boolean
    {
        val toAdd = Pair(id, date)
        return if (!hasChapter(id))
        {
            chaptersSet.add(id)
            chapters.add(toAdd)
        } else false
    }

    override fun getOpenedChapters(): ArrayList<Pair<Int, Date>> = chapters

    override fun hasChapter(id: Int): Boolean = chaptersSet.contains(id)

    @OnlyIn(Dist.DEDICATED_SERVER)
    override fun testItem(stack: ItemStack)
    {
        val player = this
        if (player is ServerPlayerEntity)
        {
            player.addChapter(itemToChapter[stack.getItem()])
            val toRemove = mutableListOf<Int>()
            for ((index, test) in specialChapters.withIndex())
            {
                val toAdd = test(stack)
                if (toAdd != null)
                {
                    player.addChapter(toAdd)
                    toRemove.add(index)
                }
            }
            specialChaptersIndices.removeAll(toRemove)
        }
    }

    companion object
    {
        val itemToChapter : Map<IItemProvider, Chapter> = ImmutableMap.of(
            Items.IRON_INGOT, Chapter.STEEL,
            Items.CLAY, Chapter.CLAY,
            Items.CLAY_BALL, Chapter.CLAY,
            PhxItems.HIGH_QUALITY_CLAY_ITEM, Chapter.OVEN
        )

        val specialChapters = listOf<(ItemStack) -> Chapter?> { stack -> if (stack.isEnchanted) Chapter.KNIFE_ENCHANTING else null }
    }
}