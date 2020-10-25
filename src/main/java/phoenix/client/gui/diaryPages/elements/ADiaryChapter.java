package phoenix.client.gui.diaryPages.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import phoenix.utils.ArrayUtils;

import java.util.ArrayList;

public abstract class ADiaryChapter
{
    FontRenderer font;
    ArrayList<IDiaryElement> elements;
    private int start = 0, end = 14, end2 = 28;
    public ADiaryChapter()
    {
        font = Minecraft.getInstance().fontRenderer;
    }


    public void add(ArrayList<IDiaryElement> elementsIn)
    {
        elements.addAll(elementsIn);
        recalculateSizes();
    }
    private void recalculateSizes()
    {
        int size = 0;
        int count  = 0;
        int count1 = 0;
        for (IDiaryElement element : elements)
        {
            if(size + element.getHeight() >= 28)
            {
                end2 += count;
            }
            else if (size + element.getHeight() >= 14)
            {
                start += 0;
                end   += count;
            }
            size += element.getHeight();
            if(count == count1)
                count++;

            count1++;
        }
    }
    public ArrayList<IDiaryElement> getCurrentPage1()
    {
        return ArrayUtils.part(elements, start, end);
    }

    public ArrayList<IDiaryElement> getCurrentPage2()
    {
        return ArrayUtils.part(elements, end, end2);
    }

    public void next()
    {
        int size  = 0;
        int count = 0;
        for (int i = end; i < elements.size(); i++)
        {
            if (size + elements.get(i).getHeight() >= 14)
            {
                start += count + 1;
                end   += count + 1;
                return;
            }
            size += elements.get(i).getHeight();
            count++;
        }
        start += count + 1;
        end   += count + 1;
    }

    public void prev()
    {
        int size  = 0;
        int count = 0;
        for (int i = start - 1; i >= 0; --i)
        {
            if (size + elements.get(i).getHeight() >= 14)
            {
                start -= count + 1;
                end   -= count + 1;
                return;
            }
            size += elements.get(i).getHeight();
            count++;
        }
        start -= count + 1;
        end   -= count + 1;
    }
}
