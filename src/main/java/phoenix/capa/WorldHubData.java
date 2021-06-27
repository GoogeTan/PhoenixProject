package phoenix.capa;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

public class WorldHubData
{
    public static boolean debug = false;
    public ServerPlayerEntity player;
    public HashMap dimensionMap = new HashMap<Integer, String>();

    public ListNBT writeToNBT(ListNBT nbtTagList)
    {

        for (Object o : this.dimensionMap.entrySet())
        {
            Entry entry = (Entry) o;
            CompoundNBT dimensionEntry = new CompoundNBT();
            dimensionEntry.putInt("key", (Integer) entry.getKey());
            dimensionEntry.putString("value", (String) entry.getValue());
            nbtTagList.add(dimensionEntry);
        }

        return nbtTagList;
    }

    public void readFromNBT(ListNBT nbt)
    {
        this.dimensionMap.clear();

        for (int i = 0; i < nbt.size(); ++i)
        {
            CompoundNBT dimensionEntry = nbt.getCompound(i);
            this.dimensionMap.put(dimensionEntry.getInt("key"), dimensionEntry.getString("value"));
        }

    }

    public void set(WorldHubData oldData)
    {
        this.dimensionMap.clear();
        this.dimensionMap.putAll(oldData.dimensionMap);
        if (debug)
            System.out.println("Successfully cloned.");
    }

    public boolean add(Integer dimHash, String name)
    {
        if (this.dimensionMap.containsKey(dimHash))
        {
            return false;
        } else
        {
            this.dimensionMap.put(dimHash, name);
            return true;
        }
    }

    public void update(Integer dimHash, String name)
    {
        this.dimensionMap.put(dimHash, name);
    }

    public void get(Integer dimHash)
    {
        this.dimensionMap.get(dimHash);
    }

    public void remove(Integer dimHash)
    {
        this.dimensionMap.remove(dimHash);
    }

    public String toString()
    {
        return "WorldHubData: [" + String.join("", (Iterable) this.dimensionMap.entrySet().stream().map((x) -> x.toString()).collect(Collectors.toList())) + "]";
    }
}