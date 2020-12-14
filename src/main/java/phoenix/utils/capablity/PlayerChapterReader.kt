package phoenix.utils.capablity

import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.INBT
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.Capability.IStorage
import java.util.*

class PlayerChapterReader : IChapterReader
{
    private val chapters = ArrayList<Pair<Int, Date>>()
    override fun getOpenedChapters() = chapters
    override fun addChapter(id: Int, date: Date) = chapters.add(Pair(id, date))
}

class SaveHandler : IStorage<IChapterReader>
{
    override fun writeNBT(capability: Capability<IChapterReader>, instance: IChapterReader, side: Direction): INBT
    {
        val nbt = CompoundNBT()
        val chapters = instance.getOpenedChapters();
        nbt.putInt("count", chapters.size)
        for (i in chapters.indices)
        {
            nbt.putInt("chid$i", chapters[i].first)
            nbt.putInt("chmin$i", chapters[i].second.minute)
            nbt.putInt("chday$i", chapters[i].second.day)
            nbt.putInt("chyear$i", chapters[i].second.year)
        }
        return nbt;
    }

    override fun readNBT(capability: Capability<IChapterReader>, instance: IChapterReader, side: Direction, nbtIn: INBT)
    {
        val nbt = (nbtIn as CompoundNBT)
        val count = nbt.getInt("count")
        for (i in 0 until count)
        {
            val id   = nbt.getInt("chid$i")
            val min  = nbt.getInt("chmin$i")
            val day  = nbt.getInt("chday$i")
            val year = nbt.getInt("chyear$i")
            instance.addChapter(id, Date(min, day, year))
        }
    }
}
