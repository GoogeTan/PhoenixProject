package phoenix.api.block

import net.minecraft.client.renderer.color.IBlockColor
import net.minecraft.client.renderer.color.IItemColor
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

/*
* Это стыбзено из туманного мира
*/
interface IColoredBlock
{
    @OnlyIn(Dist.CLIENT)
    fun getBlockColor(): IBlockColor?

    @OnlyIn(Dist.CLIENT)
    fun getItemColor(): IItemColor?
}