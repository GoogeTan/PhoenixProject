package phoenix.client.gui.diaryPages.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import phoenix.utils.ArrayUtils;

import java.util.ArrayList;

public class ADiaryChapter
{
    FontRenderer font;
    int xSize, ySize;
    ArrayList<ADiaryElement> elements = new ArrayList<>();
    private int start = 0, end = 14, end2 = 28;

    public ADiaryChapter(int xSizeIn, int ySizeIn)
    {
        font = Minecraft.getInstance().fontRenderer;
        xSize = xSizeIn;
        ySize = ySizeIn;
    }


    public void add(ArrayList<ADiaryElement> elementsIn)
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
            if ((size + elements.get(i).getHeight()) * (font.FONT_HEIGHT + 2) >= ySize - 30)
            {
                isEnded = true;
            }
            else
            {
                size += elements.get(i).getHeight();
                count++;
            }
        }
        start = end2 + 1;
        end   = end2 + count + 1;

        size  = 0;
        count = 0;
        isEnded = false;
        for (int i = end; i < elements.size() && !isEnded; i++)
        {
            if ((size + elements.get(i).getHeight()) * font.FONT_HEIGHT >= ySize - 30)
            {
                isEnded = true;
            }
            else
            {
                size += elements.get(i).getHeight();
                count++;
            }
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

    public ArrayList<ADiaryElement> getCurrentPage1()
    {
        return ArrayUtils.part(elements, start, end);
    }

    public ArrayList<ADiaryElement> getCurrentPage2()
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
            if ((size + elements.get(i).getHeight()) * font.FONT_HEIGHT >= ySize - 30)
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
            if ((size + elements.get(i).getHeight()) * font.FONT_HEIGHT >= ySize - 30)
            {
                isEnded = true;
            }
            else
            {
                size += elements.get(i).getHeight();
                count++;
            }
        }
        end -= count + 1;
        end2   -= count + 1;

        size  = 0;
        count = 0;
        isEnded = false;
        for (int i = end - 1; i >= 0 && !isEnded; --i)
        {
            if ((size + elements.get(i).getHeight()) * font.FONT_HEIGHT >= ySize - 30)
            {
                isEnded = true;
            }
            else
            {
                size += elements.get(i).getHeight();
                count++;
            }
        }
        start = end - count - 1;
    }
}
