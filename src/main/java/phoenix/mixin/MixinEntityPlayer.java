package phoenix.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import phoenix.client.gui.diaryPages.Chapters;
import phoenix.utils.Date;
import phoenix.utils.IChapterReader;
import phoenix.utils.LogManager;
import phoenix.utils.Pair;

import java.util.ArrayList;

@Mixin(PlayerEntity.class)
public class MixinEntityPlayer implements IChapterReader
{
    public ArrayList<Pair<Integer, Date>> chapters = new ArrayList<>();

    @Inject(method = "writeAdditional", at = @At("TAIL"))
    public void onWriteEntityToNBT(CompoundNBT nbt, CallbackInfo ci)
    {
        LogManager.log(this, "Player starts saving");
        nbt.putInt("count", chapters.size());
        for (int i = 0; i < chapters.size(); i++)
        {
            nbt.putInt("chid" + i, chapters.get(i).getM());
            nbt.putLong("chmin" + i, chapters.get(i).getV().getMinute());
            nbt.putLong("chday" + i, chapters.get(i).getV().getDay());
            nbt.putLong("chyear" + i, chapters.get(i).getV().getYear());
        }
        LogManager.log(this, "Player ends saving");
    }

    @Inject(method = "readAdditional", at = @At("TAIL"))
    public void onReadEntityFromNBT(CompoundNBT nbt, CallbackInfo ci)
    {
        LogManager.log(this, "Player starts loading");
        int count = nbt.getInt("count");
        for (int i = 0; i < count; ++i)
        {
            int id   = nbt.getInt("chid" + i);
            long min  = nbt.getLong("chmin" + i);
            long day  = nbt.getLong("chday" + i);
            long year = nbt.getLong("chyear" + i);
            addChapter(id, new Date(min, day, year));
        }

        if(chapters.isEmpty())
            addChapter(0, new Date(795 % 12000 / 100, 2005 % 319, 2005 / 319));

        LogManager.log(this, "Player ends loading");
    }

    @Deprecated//Use addChapter(Chapters)
    public boolean addChapter(int id, @NotNull Date date)
    {
        Pair<Integer, Date> toAdd = new Pair<>(date, id);
        if(!hasChapter(id))
            return chapters.add(toAdd);
        else
            return false;
    }

    @NotNull
    public ArrayList<Pair<Integer, Date>> getOpenedChapters() { return chapters; }

    @Override
    public boolean hasChapter(int id)
    {
        for (Pair<Integer, Date> i : chapters)
        {
            if(i.getM() == id)
                return true;
        }
        return false;
    }

    @Override
    public boolean hasChapter(@NotNull Chapters ch)
    {
        return hasChapter(ch.getId());
    }
}