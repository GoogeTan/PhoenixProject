package phoenix.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import phoenix.client.gui.diaryPages.elements.IDiaryElement;
import phoenix.client.gui.diaryPages.elements.ImageElement;
import phoenix.client.gui.diaryPages.elements.RightAlignedTextElement;
import phoenix.client.gui.diaryPages.elements.TextElement;
import phoenix.utils.exeptions.BookException;

import java.util.ArrayList;

public class DiaryUtils
{

    //принимает ключи параграфоф
    public static ArrayList<IDiaryElement> makeParagraphFromTranslate(int xSizeIn, FontRenderer renderer, String... keys)
    {
        return makeParagraph(xSizeIn, StringUtils.translateAll(keys));
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

    public static IDiaryElement read(CompoundNBT nbt) throws BookException
    {
        try
        {
            String type = nbt.getString("type");
            switch (type)
            {
                case "text":
                {
                    String text = nbt.getString("text");
                    return new TextElement(text);
                }
                case "rtext":
                {
                    String text = nbt.getString("text");
                    return new RightAlignedTextElement(text);
                }
                case "img":
                {
                    String res = nbt.getString("res");
                    int maxSizeX = nbt.getInt("maxx"), maxSizeY = nbt.getInt("maxy");
                    return new ImageElement(new ResourceLocation(res), maxSizeX, maxSizeY);
                }
                default:
                {
                    return null;
                }
            }
        }
        catch (Exception e)
        {
            throw new BookException("Can not read element");
        }
    }
}
