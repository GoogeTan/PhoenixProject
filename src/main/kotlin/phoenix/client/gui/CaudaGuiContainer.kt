package phoenix.client.gui

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.screen.inventory.ContainerScreen
import net.minecraft.client.gui.screen.inventory.InventoryScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import phoenix.enity.CaudaEntity
import phoenix.init.PhxConfiguration

class CaudaGuiContainer(container: CaudaEntity.CaudaContainer, inv: PlayerInventory, titleIn: ITextComponent) : ContainerScreen<CaudaEntity.CaudaContainer>(container, inv, titleIn)
{
    private val GUI_TEXTURE = ResourceLocation("phoenix:textures/gui/cauda${ PhxConfiguration.gameMode.textureSuffix }.png")
    private var mousePosX = 0f
    private var mousePosY = 0f

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int)
    {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f)
        minecraft!!.getTextureManager().bindTexture(GUI_TEXTURE)
        val i = (width - xSize) / 2
        val j = (height - ySize) / 2
        this.blit(i, j, 0, 0, (xSize * 194.0 / 176.0).toInt(), ySize)
        InventoryScreen.drawEntityOnScreen(
            i + 51,
            j + 60,
            17,
            (i + 51).toFloat() - this.mousePosX,
            (j + 75 - 50).toFloat() + mousePosY,
            container.getCauda()
        )
    }

    override fun render(mouseX: Int, mouseY: Int, partialTicks: Float)
    {
        this.renderBackground()
        this.mousePosX = mouseX.toFloat()
        this.mousePosY = mouseY.toFloat()
        super.render(mouseX, mouseY, partialTicks)
        renderHoveredToolTip(mouseX, mouseY)
    }
}