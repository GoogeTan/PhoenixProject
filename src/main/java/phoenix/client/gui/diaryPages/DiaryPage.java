package phoenix.client.gui.diaryPages;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.apache.commons.lang3.tuple.Pair;
import phoenix.utils.StringUtils;

import java.util.ArrayList;

public class DiaryPage
{
    static final int xSize = (int) (176 * 1.8);
    String[] strings = new String[14];
    static FontRenderer font = Minecraft.getInstance().fontRenderer;


    public static Pair<DiaryPage, ArrayList<String>> create(String[] text)
    {
        String[] strings = new String[14];
        int numder_of_string_on_page = 0;
        for (int P = 0; P < text.length; ++P)
        {
            String currect = text[P];//этот параграф

            ArrayList<String> words = StringUtils.stringToWords(currect);//слова данного параграфа
            for (int number_of_words = 0; number_of_words < words.size(); number_of_words++)//пока есть слова
            {
                String string_to_print = "";
                while (font.getStringWidth(string_to_print) < xSize / 2 - 30 && number_of_words < words.size())//пока строрка помещается на страницу
                {
                    string_to_print += words.get(number_of_words) + " ";
                    ++number_of_words;
                }
                strings[numder_of_string_on_page] = string_to_print;//добавляем в массив строк
                numder_of_string_on_page++;//увеличим кол-во строк


                if(numder_of_string_on_page >= 14)//если строк максимум
                {
                    DiaryPage res = new DiaryPage(strings);//страничка наша
                    ArrayList<String> other_text = new ArrayList<>();//оставшийся текст
                    //текст который остался возвращаем
                    for (int i = number_of_words; i < words.size(); i++)//из этого параграфа
                    {
                        other_text.add(words.get(i));
                    }
                    for (int Q = P; Q < text.length; ++Q) //возвращаем след - параграфы
                    {
                        other_text.add(words.get(Q));
                    }
                    return Pair.of(res, other_text);
                }
            }
            ++numder_of_string_on_page;//после параграфа красная строка
        }
        //если страница не полная
        for (int i = numder_of_string_on_page; i < 14; i++)
        {
            strings[i] = "";
        }
        DiaryPage res = new DiaryPage(strings);//страничка наша
        return Pair.of(res,  new ArrayList<>());
    }

    private DiaryPage(String[] text)
    {
        System.arraycopy(text, 0, strings, 0, 14);
    }

    public String[] getStrings()
    {
        return strings;
    }
}
