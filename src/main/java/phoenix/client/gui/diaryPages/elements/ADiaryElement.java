package phoenix.client.gui.diaryPages.elements;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.nbt.CompoundNBT;
import phoenix.containers.DiaryContainer;

public abstract class ADiaryElement
{
    abstract public int getHeight();
    abstract public void render(ContainerScreen<DiaryContainer> gui, FontRenderer renderer, int xSize, int ySize, int x, int y, int depth);
    abstract public CompoundNBT serialize();

    @Override
    abstract public String toString();
}
