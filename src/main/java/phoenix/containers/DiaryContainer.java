package phoenix.containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import phoenix.client.gui.diaryPages.elements.ADiaryElement;
import phoenix.init.PhoenixContainers;

import java.util.ArrayList;

public class DiaryContainer extends Container implements INamedContainerProvider
{
    ITextComponent name = new StringTextComponent("Steve");
    ArrayList<ADiaryElement> allOpened;
    int page = 0;
    public DiaryContainer(int id)
    {
        super(PhoenixContainers.GUIDE.get(), id);
    }

    public int getPage()
    {
        return page;
    }

    public void setPage(int page)
    {
        this.page = page;
    }

    @Override
    public boolean enchantItem(PlayerEntity playerIn, int id)
    {
        return super.enchantItem(playerIn, id);
    }

    public void nextPage()
    {
        this.page++;
    }

    public void prevPage()
    {
        this.page--;
    }

    public DiaryContainer setName(ITextComponent nameIn)
    {
        this.name = nameIn;
        return this;
    }

    public static DiaryContainer fromNetwork(int id, PlayerInventory inventory) {
        return new DiaryContainer(id).setName(inventory.player.getName());
    }

    public static ContainerType<DiaryContainer> fromNetwork() {
        return new ContainerType<>((id, player) -> new DiaryContainer(id));
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn)
    {
        return true;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new StringTextComponent(name.getFormattedText() + "'s Diary");
    }

      
    @Override
    public Container createMenu(int id, PlayerInventory inventory, PlayerEntity entity)
    {
        return this;
    }
}
