package phoenix.mixin;

import phoenix.utils.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import phoenix.utils.Date;
import phoenix.utils.IChapterReader;
import phoenix.utils.LogManager;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerEntity.class)
public class MixinEntityPlayer implements IChapterReader
{
    @Inject(method = "writeAdditional", at = @At("TAIL"))
    public void onWriteEntityToNBT(CompoundNBT nbt, CallbackInfo ci)
    {
        LogManager.log(this, "Player starts saving");
        nbt.putInt("count", chapters.size());
        for (int i = 0; i < chapters.size(); i++)
        {
            nbt.putInt("chid" + i, chapters.get(i).getM());
            nbt.putInt("chmin" + i, chapters.get(i).getV().getMinute());
            nbt.putInt("chday" + i, chapters.get(i).getV().getDay());
            nbt.putInt("chyear" + i, chapters.get(i).getV().getYear());
        }
        addChapter(0, new Date(1, 3 ,4));
        LogManager.log(this, "Player ends saving");
    }

    @Inject(method = "readAdditional", at = @At("TAIL"))
    public void onReadEntityFromNBT(CompoundNBT nbt, CallbackInfo ci)
    {
        LogManager.log(this, "Player starts loading");
        int count = nbt.getInt("count");
        for (int i = 0; i < count; ++i)
        {
            int id   = nbt.getInt("chid$i");
            int min  = nbt.getInt("chmin$i");
            int day  = nbt.getInt("chday$i");
            int year = nbt.getInt("chyear$i");
            addChapter(id, new Date(min, day, year));
        }
        addChapter(0, new Date(1, 3 ,4));
        LogManager.error(this, "Player ends loading");
    }

    public ArrayList<Pair<Integer, Date>> chapters = new ArrayList<>();
    public boolean addChapter(int id, Date date) { return chapters.add(new Pair<>(date, id)); }
    public List<Pair<Integer, Date>> getOpenedChapters() { return chapters; }
}