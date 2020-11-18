package phoenix.utils;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import phoenix.client.gui.diaryPages.elements.ADiaryElement;
import phoenix.client.gui.diaryPages.elements.ImageElement;
import phoenix.client.gui.diaryPages.elements.RightAlignedTextElement;
import phoenix.client.gui.diaryPages.elements.TextElement;
import phoenix.utils.exeptions.BookException;

import java.util.ArrayList;
import java.util.List;

public class DiaryUtils
{
    //принимает ключи параграфоф
    public static ArrayList<ADiaryElement> makeParagraphFromTranslate(int xSizeIn, FontRenderer font, String... keys)
    {
        return makeParagraph(font, xSizeIn, StringUtils.translateAll(keys));
    }

    public static ArrayList<ADiaryElement> makeParagraph(FontRenderer font, int xSize, ArrayList<String> text)
    {
        return makeParagraph(font, xSize, (String[]) text.toArray());
    }

    public static ArrayList<ADiaryElement> makeParagraph(FontRenderer font, int xSize, String... text)
    {
        ArrayList<ADiaryElement> res = new ArrayList<>();
        List<String> words = new ArrayList<>();

        for (String current : text) //проходим по всем параграфам
        {
            if (current != null)
            {
                words.addAll(StringUtils.stringToWords(current));
                //words.addAll(ImmutableList.copyOf(current.split(" ")));
                words.add("[break]");
            }
        }

        for (int number_of_words = 0; number_of_words < words.size();)//проходим по всем словам
        {
            String string_to_add = "";//строка которую будем добавлять
            String next_word = words.get(number_of_words);
            while (font.getStringWidth(string_to_add + " " + next_word) < xSize / 2 - 30)//пока меньше ширины страницы
            {
                if (words.get(number_of_words).equals("\\n") || words.get(number_of_words).equals("[break]"))
                {
                    res.add(new TextElement(string_to_add));
                    string_to_add = "";
                } else
                {
                    string_to_add += next_word + " ";//добавляем слово
                }
                ++number_of_words;
                if(number_of_words < words.size())
                    next_word = words.get(number_of_words);
                else
                    break;
            }
            res.add(new TextElement(string_to_add));//добавляем строку
        }
        res.add(new TextElement(""));//после каждого параграфа перенос

        return res;
    }

    public static ArrayList<ADiaryElement> add(ArrayList<ADiaryElement> chapter, Pair<Integer, ADiaryElement>... toAdd)
    {
        for (Pair<Integer, ADiaryElement> pair : toAdd)
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

    public static ADiaryElement read(CompoundNBT nbt) throws BookException
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
