package phoenix.client.gui.diaryPages;

import javafx.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import phoenix.Phoenix;
import phoenix.utils.StringUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class DiaryChapter
{
    int xSize = (int) (176 * 1.8);
    FontRenderer font = Minecraft.getInstance().fontRenderer;
    ArrayList<ArrayList<String>> pages = new ArrayList<>();

    public DiaryChapter(String[] text)
    {
        int numder_of_string_on_page = 0;
        ArrayList<String> page = new ArrayList<>();
        for (String currect: text)
        {
            ArrayList<String> words = StringUtils.stringToWords(currect);
            for (int number_of_words = 0; number_of_words < words.size(); number_of_words++)
            {
                String string_to_print = "";
                while (font.getStringWidth(string_to_print) < this.xSize / 2 - 30 && number_of_words < words.size())
                {
                    string_to_print += words.get(number_of_words) + " ";
                    ++number_of_words;
                }
                page.add(string_to_print);
                numder_of_string_on_page++;

                if(numder_of_string_on_page >= 14)
                {
                    pages.add(page);
                    page.clear();
                    numder_of_string_on_page = 0;
                }
            }
            ++numder_of_string_on_page;
        }
        if(!page.isEmpty())
            pages.add(page);

        for (ArrayList<String> s: pages)
        {
            Phoenix.LOGGER.error(s);
        }
    }

    public DiaryChapter(ArrayList<String> text)
    {
        int numder_of_string_on_page = 0;
        ArrayList<String> page = new ArrayList<>();
        for (String currect: text)
        {
            ArrayList<String> words = StringUtils.stringToWords(currect);
            for (int number_of_words = 0; number_of_words < words.size(); number_of_words++)
            {
                String string_to_print = "";
                while (font.getStringWidth(string_to_print) < this.xSize / 2 - 30 && number_of_words < words.size())
                {
                    string_to_print += words.get(number_of_words) + " ";
                    ++number_of_words;
                }
                page.add(string_to_print);
                numder_of_string_on_page++;

                if(numder_of_string_on_page >= 14)
                {
                    pages.add(page);
                    page.clear();
                    numder_of_string_on_page = 0;
                }
            }
            ++numder_of_string_on_page;
        }
        if(!page.isEmpty())
            pages.add(page);

        Phoenix.LOGGER.error(pages.get(0));

    }

    public ArrayList<String> getTextForPage(int page)
    {
        return pages.get(page);
    }

    public ArrayList<String> getStringsForPage(int page)
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
