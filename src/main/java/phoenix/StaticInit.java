package phoenix;

import net.minecraft.client.audio.MusicTicker;
import net.minecraft.util.SoundEvent;
import phoenix.init.PhoenixSounds;
import phoenix.utils.EnumUtil;
import phoenix.utils.PhoenixMusicTracks;

public class StaticInit
{
    public static void init()
    {
        Class<?>[] arr = { SoundEvent.class, int.class, int.class};
        Object[] params = { PhoenixSounds.INSTANCE.getREDO_MUSIC(), 16000, 24000 };
        PhoenixMusicTracks.INSTANCE.setREDO_MUSIC(EnumUtil.addEnum(MusicTicker.MusicType.class, "redo_music", arr, params));
    }
}
