package phoenix.utils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;

public class ArrayUtils
{
    static public <T extends Object> ArrayList<T> sumArrays(ArrayList<T> first, ArrayList<T> second, T... adv)
    {
        ArrayList<T> res = new ArrayList(first);
        res.addAll(second);

        res.addAll(Arrays.asList(adv));
        return res;
    }

    @SafeVarargs
    static public <T extends Object> ArrayList<T> sumArrays(ArrayList<T> first, T... adv)
    {
        ArrayList<T> res = new ArrayList<>(first);
        res.addAll(Arrays.asList(adv));
        return res;
    }

    @SafeVarargs
    @Nonnull
    static public <T extends Object> T[] toArray(@Nonnull ArrayList<T> first, T... adv)
    {
        ArrayList<T> arr = sumArrays(first, adv);
        return (T[]) arr.toArray();
    }
}
