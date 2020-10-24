package phoenix.client.gui.diaryPages;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.nbt.CompoundNBT;
import phoenix.Phoenix;
import phoenix.client.gui.diaryPages.elements.IDiaryElement;
import phoenix.client.gui.diaryPages.elements.ImageElement;
import phoenix.containers.DiaryContainer;

import java.util.ArrayList;

public class DiaryBook
{
    final int xSize;
    final FontRenderer font;
    final private ArrayList<ArrayList<IDiaryElement>> pages = new ArrayList<>();

    public DiaryBook(int xSizeIn, FontRenderer renderer)
    {
        this.xSize = xSizeIn;
        this.font = renderer;
    }

    public void add(ArrayList<IDiaryElement> elements)
    {
        ArrayList<IDiaryElement> page = new ArrayList<>();
        int sum = 0;
        for(IDiaryElement element : pages.get(pages.size() - 1))
            sum += element.getHeight();
        if(sum < 14)
            page = pages.get(pages.size() - 1);

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

    public void render(int number, ContainerScreen<DiaryContainer> gui, FontRenderer renderer, int xSize, int ySize, int x, int y, int depth)
    {
        if(number < pages.size() && number >= 0)
        {
            ArrayList<IDiaryElement> page = pages.get(number);
            int sum = 0;
            for (IDiaryElement element : page)
            {
                element.render(gui, renderer, xSize, ySize, x, y + sum * 15, depth);
                sum += element.getHeight();
            }
        }
    }

    public CompoundNBT serialize()
    {
        CompoundNBT res = new CompoundNBT();
        int number = 0;
        for (ArrayList<IDiaryElement> page : pages)
        {
            for (IDiaryElement iDiaryElement : page)
            {
                res.put(number + "", iDiaryElement.serialize());
                number++;
            }
        }
        res.putInt("size", number);
        return res;
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
