package phoenix.utils;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;

public class StringUtils
{
      
    public static ArrayList<String> stringToWords(String s)
    {
        ArrayList<String> result = new ArrayList<>();
        String current = "";
        for (int i = 0; i < s.length(); i++)
        {
            if(s.charAt(i) == '\n')
            {
                result.add(current);
                result.add("[break]");
                current = "";
            }
            else if(s.charAt(i) == ' ' || i == s.length() - 1)
            {
                result.add(current);
                current = "";
            }
            else
            {
                current += s.charAt(i);
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
    public static String translate(String key)
    {
        return LanguageMap.getInstance().translateKey(key);
    }
    public static void drawRightAlignedString(FontRenderer font, String string, int x, int y, int colour)
    {
        font.drawStringWithShadow(string, (float)(x - font.getStringWidth(string)), (float)y, colour);
    }

    static TextFormatting[] rainbow = {TextFormatting.RED, TextFormatting.YELLOW, TextFormatting.GREEN, TextFormatting.BLUE, TextFormatting.DARK_BLUE, TextFormatting.DARK_PURPLE};

    public static String rainbowColor(String string)
    {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < string.length(); i++)
        {
            if(string.charAt(i) != ' ' && string.charAt(i) != '\n')
                s.append(rainbow[i % rainbow.length]);
            s.append(string.charAt(i));
        }
        return s.toString();
    }
}
