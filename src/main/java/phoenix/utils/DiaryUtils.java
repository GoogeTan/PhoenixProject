package phoenix.utils;

import net.minecraft.client.gui.FontRenderer;
import org.apache.commons.lang3.tuple.Pair;
import phoenix.Phoenix;
import phoenix.client.gui.diaryPages.DiaryChapter;
import phoenix.client.gui.diaryPages.EDiaryChapter;
import phoenix.client.gui.diaryPages.elements.IDiaryElement;
import phoenix.client.gui.diaryPages.elements.TextElement;
import phoenix.utils.exeptions.BookException;

import java.util.ArrayList;

public class DiaryUtils
{
    public static EDiaryChapter makeChapter(int xSizeIn, FontRenderer renderer, String... text)
    {
        return new EDiaryChapter(text, xSizeIn, renderer);
    }
    public static DiaryChapter makeChapter(FontRenderer renderer, int xSizeIn, String... text)
    {
        return new DiaryChapter(makeParagraph(text), xSizeIn, renderer);
    }


    //принимает ключи параграфоф
    public static EDiaryChapter makeChapterFromTranslate(int xSizeIn, FontRenderer renderer, String... keys)
    {
        return new EDiaryChapter(StringUtils.translateAll(keys), xSizeIn, renderer);
    }

    public static ArrayList<IDiaryElement> makeParagraph(String... text)
    {
        ArrayList<IDiaryElement> res = new ArrayList<>();
        for (String s : text)
        {
            res.add(new TextElement(s));
        }
        return res;
    }

    public static ArrayList<IDiaryElement> add(ArrayList<IDiaryElement> chapter, Pair<Integer, IDiaryElement>... toAdd) throws BookException
    {

        for (Pair<Integer, IDiaryElement> pair : toAdd)
        {
            try
            {
                chapter.add(pair.getLeft(), pair.getRight());
            }
            catch (Exception e)
            {
                throw new BookException("Someone badly adds elements to the chapter of the book! Oh, damn it...");
            }
        }
        return chapter;
    }
}
