package phoenix.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.screen.ReadBookScreen;
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
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bindTexture(ReadBookScreen.BOOK_TEXTURES);
        int i = 0;
        int j = 192;
        if (this.isHovered()) {
            i += 23;
        }

        if (!this.isForward) {
            j += 13;
        }

        this.blit(this.x, this.y, i, j, 23, 13);
    }

    @Override
    public void playDownSound(SoundHandler soundHandler) {
        if (this.playTurnSound) {
            soundHandler.play(SimpleSound.master(SoundEvents.ITEM_BOOK_PAGE_TURN, 1.0F));
        }
    }
}