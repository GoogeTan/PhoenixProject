package phoenix.mixin

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.CompoundNBT
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import phoenix.client.gui.diaryPages.Chapters
import phoenix.utils.Date
import phoenix.utils.IPhoenixPlayer
import java.util.*
import kotlin.collections.HashSet

@Mixin(PlayerEntity::class)
abstract class MixinEntityPlayer : IPhoenixPlayer
{
    private val chaptersSet: MutableSet<Int> = HashSet()
    var chapters = ArrayList<Pair<Int, Date>>()

    @Inject(method = ["writeAdditional"], at = [At("TAIL")])
    fun onWriteEntityToNBT(nbt: CompoundNBT, ci: CallbackInfo?)
    {
        nbt.putInt("count", chapters.size)
        for (i in chapters.indices)
        {
            nbt.putInt("chid$i", chapters[i].first.toInt())
            nbt.putLong("chmin$i", chapters[i].second.minute)
            nbt.putLong("chday$i", chapters[i].second.day)
            nbt.putLong("chyear$i", chapters[i].second.year)
        }
    }

    @Inject(method = ["readAdditional"], at = [At("TAIL")])
    fun onReadEntityFromNBT(nbt: CompoundNBT, ci: CallbackInfo?)
    {
        val count = nbt.getInt("count")
        for (i in 0 until count)
        {
            val id = nbt.getInt("chid$i")
            val min = nbt.getLong("chmin$i")
            val day = nbt.getLong("chday$i")
            val year = nbt.getLong("chyear$i")
            addChapter(id, Date(min, day, year))
        }
        if (chapters.isEmpty()) addChapter(0, Date(795 % 12000 / 100, 2005 % 319, 2005 / 319))
    }

    @Deprecated("Use phoenix.utils.addChapter(chapters : Chapters)") //Use addChapter(Chapters)
    override fun addChapter(id: Int, date: Date): Boolean
    {
        val toAdd = Pair(id, date)
        return if (!chaptersSet.contains(id))
        {
            chaptersSet.add(id)
            chapters.add(toAdd)
        } else false
    }

    override fun getOpenedChapters(): ArrayList<Pair<Int, Date>> = chapters

    override fun hasChapter(id: Int): Boolean = chaptersSet.contains(id)
}