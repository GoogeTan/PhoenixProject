package phoenix.client.gui.diaryPages.elements;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.nbt.CompoundNBT;
import phoenix.containers.DiaryContainer;
import phoenix.utils.StringUtils;

public class RightAlignedTextElement extends TextElement
{
    public RightAlignedTextElement(String text)
    {
        super(text);
    }

    public RightAlignedTextElement(String text, Integer color)
    {
        super(text, color);
    }

    @Override
    public void render(ContainerScreen<DiaryContainer> gui, FontRenderer font, int xSize, int ySize, int x, int y, int depth)
    {
        StringUtils.drawRightAlignedString(font, text.getFormattedText(), x + 15, y + 15, color);
    }

    @Override
    public void serialise(CompoundNBT nbt)
    {
        nbt.putString("text", "\\r" + text.getFormattedText());
    }
}
