package phoenix.init.events;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import phoenix.Phoenix;
import phoenix.utils.Truple;

import java.util.ArrayList;

@Mod.EventBusSubscriber
public class PhoenixEventsOther
{
    public static ArrayList<Truple<Integer, Integer, Runnable>> tasks = new ArrayList<>();

    @SubscribeEvent
    public static void deferredTasks(TickEvent.WorldTickEvent event)
    {
        if(!event.world.isRemote)
        {
            Phoenix.getLOGGER().error(tasks);
            if (event.phase == TickEvent.Phase.END)
                for (int i = 0; i < tasks.size(); ++i)
                {
                    Truple<Integer, Integer, Runnable> current = tasks.get(i);
                    current.first++;
                    if (current.first >= current.second)
                    {
                        current.third.run();
                        tasks.remove(i);
                        i--;
                    }
                }
        }
    }

    public static void addTask(int time, Runnable r)
    {
        tasks.add(new Truple<>(0, time, r));
    }
}