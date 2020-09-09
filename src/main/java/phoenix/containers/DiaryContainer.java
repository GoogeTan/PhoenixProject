package phoenix.containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import phoenix.init.PhoenixContainers;

import javax.annotation.Nullable;

public class DiaryContainer extends Container implements INamedContainerProvider
{
    public DiaryContainer(int id) {
        super(PhoenixContainers.GUIDE.get(), id);
    }

    public DiaryContainer()
    {
        super(PhoenixContainers.GUIDE.get(), 0);
    }

    public static DiaryContainer fromNetwork(int id, PlayerInventory inventory) {
        return new DiaryContainer(id);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn)
    {
        return true;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new StringTextComponent("DR L. Diary");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inventory, PlayerEntity entity)
    {
        return this;
    }
}
