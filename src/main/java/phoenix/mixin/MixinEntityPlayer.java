package phoenix.mixin;

import org.jetbrains.annotations.NotNull;
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
            int id   = nbt.getInt("chid$i");
            long min  = nbt.getLong("chmin$i");
            long day  = nbt.getLong("chday$i");
            long year = nbt.getLong("chyear$i");
            addChapter(id, new Date(min, day, year));
        }
        LogManager.error(this, "Player ends loading");
    }

    public ArrayList<Pair<Integer, Date>> chapters = new ArrayList<>();
    public boolean addChapter(int id, @NotNull Date date) { return chapters.add(new Pair<>(date, id)); }
    @NotNull
    public ArrayList<Pair<Integer, Date>> getOpenedChapters() { return chapters; }
}