package phoenix.client.gui.diaryPages.elements;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.util.text.TextFormatting;
import phoenix.containers.DiaryContainer;

public class TextElement implements IDiaryElement
{
    String text = "";
    Integer color = TextFormatting.BLACK.getColor();
    public TextElement(String text)
    {
        this.text = text;
    }

    public TextElement(String text, Integer color)
    {
        this.text = text;
        this.color = color;
    }

    @Override
    public int getHeight()
    {
        return 1;
    }

    @Override
    public void render(ContainerScreen<DiaryContainer> gui, FontRenderer font, int xSize, int x, int y)
    {
        font.drawString(text, x + 15, y + 15, color);
    }
}
