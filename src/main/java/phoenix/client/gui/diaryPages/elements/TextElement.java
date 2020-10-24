package phoenix.client.gui.diaryPages.elements;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import phoenix.containers.DiaryContainer;

public class TextElement implements IDiaryElement
{
    ITextComponent text = new StringTextComponent("");
    Integer color = TextFormatting.BLACK.getColor();
    public TextElement(String text)
    {
        this.text = new StringTextComponent(text);
    }

    public TextElement(ITextComponent text)
    {
        this.text = text;
    }

    public TextElement(String text, Integer color)
    {
        this.text = new StringTextComponent(text);
        this.color = color;
    }

    @Override
    public int getHeight()
    {
        return 1;
    }

    @Override
    public void render(ContainerScreen<DiaryContainer> gui, FontRenderer font, int xSize, int ySize, int x, int y, int depth)
    {
        font.drawString(text.getFormattedText(), x + 15, y + 15, color);
    }

    @Override
    public void serialise(CompoundNBT nbt)
    {
        nbt.putString("text", text.getFormattedText());
    }
}
