package phoenix.client.gui.diaryPages;

import javafx.util.Pair;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import phoenix.utils.StringUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class EDiaryChapter
{
    final int xSize;
    final FontRenderer font;
    ArrayList<ArrayList<String>> pages = new ArrayList<>();

    public EDiaryChapter(String[] text, int xSizeIn, FontRenderer renderer)
    {
        this.xSize = xSizeIn;
        this.font  = renderer;
        ArrayList<String> page = new ArrayList<>();
        int numder_of_string_on_page = 0;
        for (String currect: text) //проходим по всем параграфам
        {
            ArrayList<String> words = StringUtils.stringToWords(currect);//слова в параграфе
            for (int number_of_words = 0; number_of_words < words.size(); ++number_of_words)//проходим по всем словам
            {
                String string_to_add = "";//строка которую будем добавлять
                while (font.getStringWidth(string_to_add) < this.xSize / 2 - 30 && number_of_words < words.size())//пока меньше ширины страницы
                {
                    string_to_add += words.get(number_of_words) + " ";//добавляем слово
                    ++number_of_words;
                }
                page.add(string_to_add);//добавляем строку
                numder_of_string_on_page++;

                if(numder_of_string_on_page >= 14)//если больше чем почещается на странице
                {
                    pages.add((ArrayList<String>) page.clone());
                    page.clear();
                    numder_of_string_on_page = 0;
                }
            }
            ++numder_of_string_on_page;//после каждого параграфа перенос
        }
        if(!page.isEmpty())
        {
            pages.add((ArrayList<String>) page.clone());
        }
    }

    //возвращает все строки на странице
    public ArrayList<String> getTextForPage(int page)
    {
        return pages.get(page);
    }

    public int countPages()
    {
        return pages.size();
    }
    @Nullable
    public ArrayList<Pair<Integer, ResourceLocation>> getImages()
    {
        return null;
    }
}
