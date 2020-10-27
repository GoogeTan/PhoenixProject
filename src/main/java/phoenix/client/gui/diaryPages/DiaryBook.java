package phoenix.client.gui.diaryPages;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import phoenix.client.gui.diaryPages.elements.ADiaryChapter;
import phoenix.client.gui.diaryPages.elements.ADiaryElement;
import phoenix.containers.DiaryContainer;

import java.util.ArrayList;

public class DiaryBook
{
    public final ADiaryChapter current_chapter;
    final int xSize, ySize;
    final FontRenderer font;

    public DiaryBook(int xSizeIn, int ySizeIn, FontRenderer renderer)
    {
        this.xSize = xSizeIn;
        this.ySize = (int) (ySizeIn * 0.6);
        this.font = renderer;
        current_chapter = new ADiaryChapter(xSizeIn, (int) (ySizeIn * 0.6));
    }

    public void next()
    {
        if(!current_chapter.isLast())
            current_chapter.next();
    }

    public void prev()
    {
        if(!current_chapter.isFirst())
            current_chapter.prev();
    }

    public void add(ArrayList<ADiaryElement> elements)
    {
        current_chapter.add(elements);
    }

    public void render(ContainerScreen<DiaryContainer> gui, FontRenderer renderer, int xSize, int ySize, int x, int y, int depth)
    {
        ArrayList<ADiaryElement> page = current_chapter.getCurrentPage1();;
        int sum = 0;
        for (ADiaryElement element : page)
        {
            element.render(gui, renderer, xSize, ySize, x, y + sum * (font.FONT_HEIGHT + 2), depth);
            sum += element.getHeight();
        }


        page = current_chapter.getCurrentPage2();;
        sum = 0;
        for (ADiaryElement element : page)
        {
            element.render(gui, renderer, xSize, ySize, x + xSize / 2 - 10, y + sum * (font.FONT_HEIGHT + 2), depth);
            sum += element.getHeight();
        }
    }
    /*
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
    //*/
}
