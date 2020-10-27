package phoenix.utils;

import java.util.ArrayList;
import java.util.Arrays;

public class ArrayUtils
{
    @SafeVarargs
    static public <T> ArrayList<T> sumArrays(ArrayList<T> first, ArrayList<T> second, T... adv)
    {
        ArrayList<T> res = new ArrayList<>(first);
        res.addAll(second);

        res.addAll(Arrays.asList(adv));
        return res;
    }

    @SafeVarargs
    static public <T> ArrayList<T> sumArrays(ArrayList<T> first, T... adv)
    {
        ArrayList<T> res = new ArrayList<>(first);
        res.addAll(Arrays.asList(adv));
        return res;
    }

    public static <T> ArrayList<T> part(ArrayList<T> list, int from, int to)
    {
        ArrayList<T> res = new ArrayList<>();
        for (int i = from; i < to; i++)
        {
            res.add(list.get(getIndex(list.size(), i)));
        }
        return res;
    }

    private static int getIndex(int size, int index)
    {
        if(size == 0)
            return 0;
        while (index < 0)
            index += size;
        index %= size;
        return index;
    }
}
