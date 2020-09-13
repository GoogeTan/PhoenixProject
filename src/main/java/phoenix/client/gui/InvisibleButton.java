package phoenix.client.gui;

import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.SoundEvents;

public class InvisibleButton extends Button
{
    private final boolean isForward;
    private final boolean playTurnSound;

    public InvisibleButton(int xIn, int yIn, int height, boolean isForward, Button.IPressable pressable, boolean playTurnSound) {
        super(xIn, yIn, 20, height, "", pressable);
        this.isForward = isForward;
        this.playTurnSound = playTurnSound;
    }

    @Override
    public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_)
    {
    }

    @Override
    public void playDownSound(SoundHandler soundHandler) {
        if (this.playTurnSound) {
            soundHandler.play(SimpleSound.master(SoundEvents.ITEM_BOOK_PAGE_TURN, 1.0F));
        }

    }
}