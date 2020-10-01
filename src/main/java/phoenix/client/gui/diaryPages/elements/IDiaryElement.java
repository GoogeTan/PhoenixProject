package phoenix.client.gui.diaryPages.elements;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import phoenix.containers.DiaryContainer;

public interface IDiaryElement
{
    int getHeight();
    void render(ContainerScreen<DiaryContainer> gui, FontRenderer renderer, int xSize, int x, int y);
}
