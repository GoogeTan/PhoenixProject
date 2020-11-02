package phoenix.utils;

import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.LinkedList;

public class RingLinkedList<T> extends LinkedList<T>
{
    public RingLinkedList()
    {
    }

    public RingLinkedList(T[] t)
    {
        addAll(ImmutableList.copyOf(t));
    }

    public RingLinkedList(Collection<T> collection)
    {
        addAll(collection);
    }

    @Nonnull
    @Override
    public T get(int index)
    {
        while (index < 0)
            index += size();
        index %= size();
        return super.get(index);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c)
    {
        while (index < 0)
            index += size();
        index %= size();
        return super.addAll(index, c);
    }

    @Nonnull
    public T[] toArray()
    {
        return (T[]) super.toArray();
    }
}
