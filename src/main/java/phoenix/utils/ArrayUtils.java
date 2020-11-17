package phoenix.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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

    public static <T> List<T> resize(List<T> list, int newSize, T toFill)
    {
        if(list.size() > newSize)
        {
            list.subList(newSize, list.size()).clear();
        }
        else if(list.size() < newSize)
        {
            for (int i = list.size(); i < newSize; ++i)
                list.add(toFill);
        }
        return list;
    }

    public static <T> ArrayList<T> part(LinkedList<T> list, int from, int to)
    {
        ArrayList<T> res = new ArrayList<>();
        for (int i = from; i < to && i < list.size(); i++)
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
        if(index >= size)
            return size - 1;
        return index;
    }
}
