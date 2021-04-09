package phoenix.other

import net.minecraft.client.audio.MusicTicker
import net.minecraft.client.audio.MusicTicker.MusicType
import net.minecraft.util.math.MathHelper
import phoenix.utils.LogManager
import phoenix.utils.mc
import phoenix.world.StageManager
import kotlin.math.min

class MusicTicker(old : MusicTicker) : MusicTicker(mc)
{
    init
    {
        random = old.random
        client = old.client
        currentMusic = old.currentMusic
        timeUntilNextMusic = old.timeUntilNextMusic
    }

    override fun tick()
    {
        val music = getMusicType()
        if (currentMusic != null)
        {
            if (music!!.sound.name != currentMusic.soundLocation)
            {
                client.getSoundHandler().stop(currentMusic)
                timeUntilNextMusic = MathHelper.nextInt(random, 0, music.minDelay / 2)
            }
            if (!client.getSoundHandler().isPlaying(currentMusic))
            {
                currentMusic = null
                timeUntilNextMusic = min(
                    MathHelper.nextInt(random, music.minDelay, music.maxDelay),
                    timeUntilNextMusic
                )
            }
        }

        timeUntilNextMusic = min(timeUntilNextMusic, music?.maxDelay?:24000)
        if (currentMusic == null && timeUntilNextMusic-- <= 0)
        {
            play(music)
        }
    }

    private fun getMusicType() : MusicType?
    {
        val selector: MusicType? = client.ambientMusicType
        return if(selector == MusicType.END) StageManager.stageEnum.music else selector
    }
}