package phoenix.utils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

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
}
