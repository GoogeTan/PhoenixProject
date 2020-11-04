package phoenix.utils.block;

import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
/*
 * Это стыбзено из туманного мира
 */
public interface IColoredBlock
{
    @OnlyIn(Dist.CLIENT)
    public IBlockColor getBlockColor();

    @OnlyIn(Dist.CLIENT)
    public IItemColor getItemColor();
}
