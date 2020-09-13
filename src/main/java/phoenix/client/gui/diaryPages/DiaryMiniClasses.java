package phoenix.client.gui.diaryPages;

import net.minecraft.client.Minecraft;

import java.util.List;

public class DiaryMiniClasses
{
    public interface IChapterComponent
    {
        List<IPage> pages();
    }

    public interface IPage
    {
        List<IStructuralGuiElement> elements();
    }

    public interface IStructuralGuiElement
    {
        default Minecraft mc()
        {// вспомогательный метод, чтобы каждый раз не писать Minecraft.getMinecraft()
            return Minecraft.getInstance();
        }

        void render(int mouseX, int mouseY);
    }
}
