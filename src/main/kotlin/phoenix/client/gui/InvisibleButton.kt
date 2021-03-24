package phoenix.client.gui

import net.minecraft.client.audio.SimpleSound
import net.minecraft.client.audio.SoundHandler
import net.minecraft.client.gui.widget.button.Button
import net.minecraft.util.SoundEvents


class InvisibleButton(xIn: Int, yIn: Int, height: Int, pressable: IPressable, private val playTurnSound: Boolean) :
    Button(xIn, yIn, 40, height, "", pressable)
{
    override fun renderButton(p1: Int, p2: Int, p3: Float)
    {
    }

    override fun playDownSound(soundHandler: SoundHandler)
    {
        if (playTurnSound)
            soundHandler.play(SimpleSound.master(SoundEvents.ITEM_BOOK_PAGE_TURN, 1.0f))
    }
}