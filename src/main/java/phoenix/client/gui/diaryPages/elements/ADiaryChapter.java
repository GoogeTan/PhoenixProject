package phoenix.client.gui.diaryPages.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.util.ArrayList;

public abstract class ADiaryChapter
{
    FontRenderer font;
    ArrayList<IDiaryElement> elements;
    private int start = 0, end = 14;
    public ADiaryChapter()
    {
        font = Minecraft.getInstance().fontRenderer;
    }


    public void add(ArrayList<IDiaryElement> elements)
    {
        elements.addAll(elements);
        int size = 0;
        for (IDiaryElement element : elements)
        {
            if (size + element.getHeight() >= 14)
            {
                start = 0;
                end = size;
                return;
            }
            size += element.getHeight();
        }
    }

    public ArrayList<IDiaryElement> getCurrectPages()
    {

    }
    abstract ArrayList<IDiaryElement> next();
}
