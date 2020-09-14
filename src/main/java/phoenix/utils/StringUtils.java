package phoenix.utils;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class StringUtils
{
    @Nonnull
    public static ArrayList<String> stringToWords(@Nonnull String s)
    {
        ArrayList<String> result = new ArrayList<>();
        String currect = "";
        for (int i = 0; i < s.length(); i++)
        {
            if(s.charAt(i) == ' ' || i == s.length() - 1)
            {
                result.add(currect);
                currect = "";
            }
            else
            {
                currect += s.charAt(i);
            }
        }
        return result;
    }


    public static ArrayList<String> translateAllListed(String... strings)
    {
        ArrayList<String> res = new ArrayList<>();

        for(String s : strings)
        {
            res.add(new TranslationTextComponent(s).getFormattedText());
        }

        return res;
    }


    public static String[] translateAll(String... strings)
    {
        String[] res = new String[strings.length];

        for(int i = 0; i < strings.length; ++i)
        {
            //res[i] = (new TranslationTextComponent(strings[i]).getFormattedText());
            res[i] = I18n.format(strings[i]);
        }

        return res;
    }
}
