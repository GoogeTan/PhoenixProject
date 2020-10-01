package phoenix.client.gui.diaryPages.elements;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
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
    public void render(ContainerScreen<DiaryContainer> gui, FontRenderer font, int xSize, int x, int y)
    {
        //font.drawString(text, x + 15, y + 15, color);
        StringUtils.drawRightAlignedString(font, text, x + 15, y + 15, color);
    }

}
