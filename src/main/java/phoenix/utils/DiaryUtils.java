package phoenix.utils;

import net.minecraft.client.gui.FontRenderer;
import org.apache.commons.lang3.tuple.Pair;
import phoenix.Phoenix;
import phoenix.client.gui.diaryPages.DiaryChapter;
import phoenix.client.gui.diaryPages.EDiaryChapter;
import phoenix.client.gui.diaryPages.elements.IDiaryElement;
import phoenix.client.gui.diaryPages.elements.TextElement;
import phoenix.init.PhoenixEntities;
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

    public static ArrayList<IDiaryElement> makeParagraph(FontRenderer font, int xSize, String... text)
    {
        ArrayList<IDiaryElement> res = new ArrayList<>();
        for (String currect : text) //проходим по всем параграфам
        {
            ArrayList<String> words = StringUtils.stringToWords(currect);//слова в параграфе
            for (int number_of_words = 0; number_of_words < words.size(); ++number_of_words)//проходим по всем словам
            {
                String string_to_add = "";//строка которую будем добавлять
                while (font.getStringWidth(string_to_add) < xSize / 2 - 30 && number_of_words < words.size())//пока меньше ширины страницы
                {
                    string_to_add += words.get(number_of_words) + " ";//добавляем слово
                    ++number_of_words;
                }
                res.add(new TextElement(string_to_add));
            }
            res.add(new TextElement(""));//после каждого параграфа перенос
        }
        return res;
    }

    public static ArrayList<IDiaryElement> add(ArrayList<IDiaryElement> chapter, Pair<Integer, IDiaryElement>... toAdd)
    {
        for (Pair<Integer, IDiaryElement> pair : toAdd)
        {
            if (chapter.size() < pair.getLeft())
            {
                chapter.add(pair.getRight());
            }
            else
            {
                chapter.add(pair.getLeft(), pair.getRight());
            }
        }
        return chapter;
    }
}
