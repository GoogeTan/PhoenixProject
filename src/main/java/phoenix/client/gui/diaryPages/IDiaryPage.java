package phoenix.client.gui.diaryPages;

import javafx.util.Pair;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public interface IDiaryPage
{
    String[] getText();
    ArrayList<Pair<Integer, ResourceLocation>> getImages();
}
