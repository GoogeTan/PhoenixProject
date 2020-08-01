package phoenix.world.capa;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.common.util.NonNullSupplier;
import phoenix.Phoenix;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static phoenix.Phoenix.capability;


public class StageProvider implements ICapabilityProvider
{
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
    {
        if (cap == capability) {
            return LazyOptional.of(new NonNullLazy<T>() {
                @Nonnull
                @Override
                public T get()
                {
                    return (T) capability;
                }
            });
        }
        return LazyOptional.empty();
    }
}


