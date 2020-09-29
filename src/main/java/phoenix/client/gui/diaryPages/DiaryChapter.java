package phoenix.client.gui.diaryPages;

import net.minecraft.client.gui.FontRenderer;
import phoenix.client.gui.diaryPages.elements.IDiaryElement;
import phoenix.utils.ArrayUtils;

import java.util.ArrayList;

public class DiaryChapter
{
    final int xSize;
    final FontRenderer font;
    ArrayList<ArrayList<IDiaryElement>> pages = new ArrayList<>();

    public DiaryChapter(IDiaryElement[] elements, int xSizeIn, FontRenderer renderer)
    {
        this.xSize = xSizeIn;
        this.font = renderer;
        ArrayList<IDiaryElement> page = new ArrayList<>();
        for (int numderOfElements = 0; numderOfElements < elements.length; ++numderOfElements)
        {
            int size = 0;
            if(size + elements[numderOfElements].getHeight() <= 14)
            {
                page.add(elements[numderOfElements]);
                size += elements[numderOfElements].getHeight();
            }
            else
            {
                pages.add((ArrayList<IDiaryElement>) page.clone());
                page.clear();
            }
            numderOfElements--;
        }
        if (!page.isEmpty())
        {
            pages.add((ArrayList<IDiaryElement>) page.clone());
        }
    }

    public DiaryChapter(ArrayList<IDiaryElement> elements,int xSize, FontRenderer font)
    {
        this(ArrayUtils.toArray(elements), xSize, font);
    }

    //возвращает все строки на странице
    public ArrayList<IDiaryElement> getElementsForPage(int page)
    {
        return pages.get(page);
    }

    public int countOfPages()
    {
        return pages.size();
    }
}
