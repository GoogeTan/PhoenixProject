package phoenix.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import phoenix.client.gui.diaryPages.DiaryChapter;
import phoenix.client.gui.diaryPages.elements.IDiaryElement;
import phoenix.client.gui.diaryPages.elements.ImageElement;
import phoenix.client.gui.diaryPages.elements.RightAlignedTextElement;
import phoenix.client.gui.diaryPages.elements.TextElement;

import java.util.ArrayList;

public class DiaryUtils
{
    public static DiaryChapter makeChapter(FontRenderer renderer, int xSizeIn, String... text)
    {
        return new DiaryChapter(makeParagraph(text), xSizeIn, renderer);
    }


    //принимает ключи параграфоф
    public static ArrayList<IDiaryElement> makeParagraphFromTranslate(int xSizeIn, FontRenderer renderer, String... keys)
    {
        return makeParagraph(xSizeIn, StringUtils.translateAll(keys));
    }

    public static DiaryChapter makeChapterFromTranslate(int xSizeIn, FontRenderer renderer, String... keys)
    {
        return new DiaryChapter(makeParagraphFromTranslate(xSizeIn, renderer, keys), xSizeIn, renderer);
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
            if(currect != null)
            {
                ArrayList<String> words = StringUtils.stringToWords(currect);//слова в параграфе
                for (int number_of_words = 0; number_of_words < words.size(); ++number_of_words)//проходим по всем словам
                {
                    String string_to_add = "";//строка которую будем добавлять
                    while (font.getStringWidth(string_to_add) < xSize / 2 - 30 && number_of_words < words.size())//пока меньше ширины страницы
                    {
                        if(words.get(number_of_words).equals("[break]"))
                        {
                            res.add(new TextElement(string_to_add));
                            string_to_add = "";
                            ++number_of_words;
                        }
                        else
                        {
                            string_to_add += words.get(number_of_words) + " ";//добавляем слово
                            ++number_of_words;
                        }
                    }
                    res.add(new TextElement(string_to_add));//добавляем строку
                }
                res.add(new TextElement(""));//после каждого параграфа перенос
            }
        }
        return res;
    }

    public static ArrayList<IDiaryElement> makeParagraph(int xSize, ArrayList<String> text)
    {
        ArrayList<IDiaryElement> res = new ArrayList<>();
        for (String currect : text) //проходим по всем параграфам
        {
            ArrayList<String> words = StringUtils.stringToWords(currect);//слова в параграфе
            for (int number_of_words = 0; number_of_words < words.size(); ++number_of_words)//проходим по всем словам
            {
                String string_to_add = "";//строка которую будем добавлять
                while (Minecraft.getInstance().fontRenderer.getStringWidth(string_to_add) < xSize / 2 - 30 && number_of_words < words.size())//пока меньше ширины страницы
                {
                    if(words.get(number_of_words).equals("[break]"))//если перенос
                    {
                        res.add(new TextElement(string_to_add));//добавляем строку
                        string_to_add = "";//обнуляем строку
                        ++number_of_words;//проходим это слово
                    }
                    else
                    {
                        string_to_add += words.get(number_of_words) + " ";//добавляем слово
                        ++number_of_words;
                    }
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


    public static ImageElement readImageElement(CompoundNBT nbt, int maxSizeX, int maxSizeY)
    {
        return new ImageElement(new ResourceLocation(nbt.getString("res")), maxSizeX, maxSizeY);
    }

    public static TextElement readTextElement(CompoundNBT nbt)
    {
        String s = nbt.getString("text");
        if(s.length() >= 2 && s.charAt(0) == '\\' && s.charAt(2) == 'r')
        {
            return new RightAlignedTextElement(s.substring(2));
        }
        else
        {
            return new TextElement(s);
        }
    }
}
