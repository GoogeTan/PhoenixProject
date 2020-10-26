package phoenix.client.gui.diaryPages.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import phoenix.utils.ArrayUtils;

import java.util.ArrayList;

public class ADiaryChapter
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
        int size  = 0;
        int count = 0;
        boolean isEnded = false;
        for (int i = 0; i < elements.size() && !isEnded; i++)
        {
            if (size + elements.get(i).getHeight() >= 14)
            {
                isEnded = true;
            }
            size += elements.get(i).getHeight();
            count++;
        }
        start = end2 + 1;
        end   = end2 + count + 1;

        size  = 0;
        count = 0;
        isEnded = false;
        for (int i = end; i < elements.size() && !isEnded; i++)
        {
            if (size + elements.get(i).getHeight() >= 14)
            {
                isEnded = true;
            }
            size += elements.get(i).getHeight();
            count++;
        }
        end2 = end + count + 1;
    }

    public boolean isLast()
    {
        return end >= elements.size() - 1 || end2 >= elements.size() - 1;
    }

    public boolean isFirst()
    {
        return start == 0;
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
        boolean isEnded = false;
        for (int i = end2; i < elements.size() && !isEnded; i++)
        {
            if (size + elements.get(i).getHeight() >= 14)
            {
                isEnded = true;
            }
            size += elements.get(i).getHeight();
            count++;
        }
        start = end2 + 1;
        end   = end2 + count + 1;

        size  = 0;
        count = 0;
        isEnded = false;
        for (int i = end; i < elements.size() && !isEnded; i++)
        {
            if (size + elements.get(i).getHeight() >= 14)
            {
                isEnded = true;
            }
            size += elements.get(i).getHeight();
            count++;
        }
        end2 = end + count + 1;
    }

    public void prev()
    {
        int size  = 0;
        int count = 0;
        boolean isEnded = false;
        for (int i = start - 1; i >= 0 && !isEnded; --i)
        {
            if (size + elements.get(i).getHeight() >= 14)
            {
                isEnded = true;
            }
            size += elements.get(i).getHeight();
            count++;
        }
        end -= count + 1;
        end2   -= count + 1;

        size  = 0;
        count = 0;
        isEnded = false;
        for (int i = end - 1; i >= 0 && !isEnded; --i)
        {
            if (size + elements.get(i).getHeight() >= 14)
            {
                isEnded = true;
            }
            size += elements.get(i).getHeight();
            count++;
        }
        start = end - count - 1;
    }
}
