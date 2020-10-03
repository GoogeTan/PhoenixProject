package phoenix.utils;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.LanguageMap;
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
            if(s.charAt(i) == '\n')
            {
                result.add(currect);
                result.add("[break]");
                currect = "";
            }
            else if(s.charAt(i) == ' ' || i == s.length() - 1)
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
        return ArrayUtils.sumArrays(new ArrayList<String>(), translateAll(strings));
    }

    public static ArrayList<String> translateAll(String... strings)
    {
        ArrayList<String> res = new ArrayList<>();
        for (String string : strings) {
            res.add(LanguageMap.getInstance().translateKey(string));
        }

        return res;
    }
    public static void drawRightAlignedString(FontRenderer font, String string, int x, int y, int colour)
    {
        font.drawStringWithShadow(string, (float)(x - font.getStringWidth(string)), (float)y, colour);
    }
}
