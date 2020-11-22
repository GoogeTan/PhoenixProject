package phoenix.client.gui.diaryPages.elements;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import phoenix.utils.ArrayUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class DiaryChapter
{
    FontRenderer font;
    int xSize, ySize;
    private final LinkedList<ADiaryElement> elements = new LinkedList<>();
    private int start = 0, end = 0, end2 = 0;

    public DiaryChapter(int xSizeIn, int ySizeIn)
    {
        font = Minecraft.getInstance().fontRenderer;
        xSize = xSizeIn;
        ySize = ySizeIn;
    }

    public DiaryChapter(int xSizeIn, int ySizeIn, ADiaryElement... elementsIn)
    {
        this(xSizeIn, ySizeIn, ImmutableList.copyOf(elementsIn));
    }

    public DiaryChapter(int xSizeIn, int ySizeIn, Collection<ADiaryElement> elementsIn)
    {
        font = Minecraft.getInstance().fontRenderer;
        xSize = xSizeIn;
        ySize = ySizeIn;
        add(elementsIn);
    }

    public void add(Collection<ADiaryElement> elementsIn)
    {
        elements.addAll(elementsIn);
        recalculateSizes();
    }

    private void recalculateSizes()
    {
        int size  = 0;
        int count = 0;
        for (ADiaryElement element : elements)
        {
            if ((size + element.getHeight(xSize, ySize)) * (font.FONT_HEIGHT + 2) >= ySize - 30)
            {
                break;
            }
            else
            {
                size += element.getHeight(xSize, ySize);
                count++;
            }
        }
        start = 0;
        end   = count;

        size  = 0;
        count = 0;
        for (int i = end; i < elements.size(); i++)
        {
            if ((size + elements.get(i).getHeight(xSize, ySize)) * font.FONT_HEIGHT >= ySize - 30)
            {
                break;
            }
            else
            {
                size += elements.get(i).getHeight(xSize, ySize);
                count++;
            }
        }
        end2 = end + count + 1;
    }

    public void toFirst()
    {
        start = 0;
        end = 0;
        end2 = 0;
        recalculateSizes();
    }

    public boolean isLast()
    {
        return end >= elements.size() - 1 || end2 >= elements.size() - 1;
    }

    public boolean isFirst()
    {
        return start <= 0;
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
        System.out.println(elements.size());
        if(!isLast())
        {
            int size = 0;
            int count = 0;
            boolean isEnded = false;
            for (int i = end2; i < elements.size() && !isEnded; i++)
            {
                if ((size + elements.get(i).getHeight(xSize, ySize)) * font.FONT_HEIGHT >= ySize - 30)
                {
                    isEnded = true;
                }
                size += elements.get(i).getHeight(xSize, ySize);
                count++;
            }
            start = end2 + 1;
            end = end2 + count + 1;

            size = 0;
            count = 0;
            isEnded = false;
            for (int i = end; i < elements.size() && !isEnded; i++)
            {
                if (size + elements.get(i).getHeight(xSize, ySize) >= 14)
                {
                    isEnded = true;
                }
                size += elements.get(i).getHeight(xSize, ySize);
                count++;
            }
            end2 = end + count + 1;
        }
    }

    public void prev()
    {
        System.out.println(elements.size());
        if(!isFirst())
        {
            int size = 0;
            int count = 0;
            boolean isEnded = false;
            for (int i = start - 1; i >= 0 && !isEnded; --i)
            {
                if ((size + elements.get(i).getHeight(xSize, ySize)) * font.FONT_HEIGHT >= ySize - 30)
                {
                    isEnded = true;
                } else
                {
                    size += elements.get(i).getHeight(xSize, ySize);
                    count++;
                }
            }
            end -= count + 1;
            end2 -= count + 1;

            size = 0;
            count = 0;
            isEnded = false;
            for (int i = end - 1; i >= 0 && !isEnded; --i)
            {
                if ((size + elements.get(i).getHeight(xSize, ySize)) * font.FONT_HEIGHT >= ySize - 30)
                {
                    isEnded = true;
                } else
                {
                    size += elements.get(i).getHeight(xSize, ySize);
                    count++;
                }
            }
            start = end - count - 1;
        }
    }
}
