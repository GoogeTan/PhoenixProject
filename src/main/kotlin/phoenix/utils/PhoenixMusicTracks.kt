package phoenix.utils

import net.minecraft.client.audio.MusicTicker
import net.minecraft.util.SoundEvent
import phoenix.init.PhoenixSounds

object PhoenixMusicTracks
{
    val REDO_MUSIC : MusicTicker.MusicType = EnumUtil.addEnum(MusicTicker.MusicType::class.java, "redo_music",
        arrayOf(SoundEvent::class.java, Int::class.java, Int::class.java),
        arrayOf(PhoenixSounds.REDO_MUSIC, 16000, 24000))
}