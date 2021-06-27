package phoenix.capa;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CapabilityWorldHubList implements ICapabilitySerializable<ListNBT>
{
    @CapabilityInject(WorldHubData.class)
    public static final Capability HUB_LIST_CAPABILITY = null;
    private WorldHubData instance;

    public CapabilityWorldHubList(ServerPlayerEntity player)
    {
        this.instance = (WorldHubData) HUB_LIST_CAPABILITY.getDefaultInstance();
        this.instance.player = player;
    }

    public static void register()
    {
        CapabilityManager.INSTANCE.register(WorldHubData.class, new CapabilityWorldHubList.HubListStorage(), WorldHubData::new);
    }

    public boolean hasCapability(Capability capability, Direction facing)
    {
        return capability == HUB_LIST_CAPABILITY;
    }

    @Override
    public<T> @NotNull LazyOptional<T> getCapability(Capability<T> capability, Direction facing)
    {
        return this.hasCapability(capability, facing) ? HUB_LIST_CAPABILITY.orEmpty(capability, null) : null;
    }

    @Override
    public ListNBT serializeNBT()
    {
        return (ListNBT) HUB_LIST_CAPABILITY.writeNBT(this.instance, null);
    }

    @Override
    public void deserializeNBT(ListNBT nbt)
    {
        HUB_LIST_CAPABILITY.readNBT(this.instance, null, nbt);
    }

    public static class HubListStorage implements Capability.IStorage<WorldHubData>
    {
        @Nullable
        @Override
        public INBT writeNBT(Capability<WorldHubData> capability, WorldHubData instance, Direction side)
        {
            return instance.writeToNBT(new ListNBT());
        }

        @Override
        public void readNBT(Capability<WorldHubData> capability, WorldHubData instance, Direction side, INBT nbt)
        {
            if (nbt instanceof ListNBT)
            {
                instance.readFromNBT((ListNBT) nbt);
            }
        }
    }
}