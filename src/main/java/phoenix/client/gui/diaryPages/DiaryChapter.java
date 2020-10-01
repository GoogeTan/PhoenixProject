package phoenix.client.gui.diaryPages;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import phoenix.client.gui.diaryPages.elements.IDiaryElement;
import phoenix.containers.DiaryContainer;

import java.util.ArrayList;

public class DiaryChapter
{
    final int xSize;
    final FontRenderer font;
    final private ArrayList<ArrayList<IDiaryElement>> pages = new ArrayList<>();

    public DiaryChapter(IDiaryElement[] elements, int xSizeIn, FontRenderer renderer)
    {
        this.xSize = xSizeIn;
        this.font = renderer;
        ArrayList<IDiaryElement> page = new ArrayList<>();
        int size = 0;
        for (IDiaryElement element : elements)
        {
            if (size + element.getHeight() >= 14)
            {
                pages.add((ArrayList<IDiaryElement>) page.clone());
                page.clear();
                size = 0;
            }
            page.add(element);
            size += element.getHeight();
        }
        if (!page.isEmpty())
        {
            pages.add((ArrayList<IDiaryElement>) page.clone());
        }
    }

    public DiaryChapter(ArrayList<IDiaryElement> elements, int xSizeIn, FontRenderer renderer)
    {
        this.xSize = xSizeIn;
        this.font = renderer;
        ArrayList<IDiaryElement> page = new ArrayList<>();
        int size = 0;
        for (IDiaryElement element : elements)
        {
            if (size + element.getHeight() >= 14)
            {
                pages.add((ArrayList<IDiaryElement>) page.clone());
                page.clear();
                size = 0;
            }
            page.add(element);
            size += element.getHeight();
        }
        if (!page.isEmpty())
        {
            pages.add((ArrayList<IDiaryElement>) page.clone());
        }
    }

    public void render(int number, ContainerScreen<DiaryContainer> gui, FontRenderer renderer, int xSize, int x, int y)
    {
        if(number < pages.size() && number >= 0)
        {
            ArrayList<IDiaryElement> page = pages.get(number);
            int sum = 0;
            for (IDiaryElement element : page)
            {
                //element.render(gui, renderer, xSize, x, y + sum * 15);
                sum += element.getHeight();
            }
        }
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
