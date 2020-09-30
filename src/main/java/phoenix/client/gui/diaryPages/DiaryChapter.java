package phoenix.client.gui.diaryPages;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import phoenix.client.gui.diaryPages.elements.IDiaryElement;
import phoenix.containers.DiaryContainer;
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
            //numderOfElements--;
        }
        if (!page.isEmpty())
        {
            pages.add((ArrayList<IDiaryElement>) page.clone());
        }
    }

    public void render(int number, ContainerScreen<DiaryContainer> gui, FontRenderer renderer, int xSize, int x, int y)
    {
        for (int i = 0; i < pages.get(number).size(); ++i)
        {
            pages.get(number).get(i).render(gui, renderer, xSize, x, y +  pages.get(number).get(i).getHeight() * 15);
        }
    }

    public DiaryChapter(ArrayList<IDiaryElement> elements,int xSize, FontRenderer font)
    {
        this(toArray(elements), xSize, font);
    }
    public static IDiaryElement[] toArray(ArrayList<IDiaryElement> elements)
    {
        IDiaryElement[] arr = new IDiaryElement[elements.size()];
        for (int i = 0; i < elements.size(); ++i)
        {
            arr[i] = elements.get(i);
        }
        return arr;
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
