package phoenix.client.gui

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screen.inventory.ContainerScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.text.ITextComponent
import phoenix.Phoenix
import phoenix.api.entity.IPhoenixPlayer
import phoenix.client.gui.diaryPages.Chapter
import phoenix.client.gui.diaryPages.DiaryBook
import phoenix.client.gui.diaryPages.elements.ADiaryElement
import phoenix.client.gui.diaryPages.elements.RightAlignedTextElement
import phoenix.containers.DiaryContainer
import phoenix.utils.DiaryUtils.makeParagraph
import phoenix.utils.RenderUtils
import phoenix.utils.TextureLocation
import phoenix.utils.clientPlayer
import phoenix.utils.mc

private val backgoundTexture = TextureLocation(Phoenix.MOD_ID, "textures/gui/diary_2.png")

class DiaryGui(val container: DiaryContainer, inv: PlayerInventory, titleIn: ITextComponent) : ContainerScreen<DiaryContainer>(container, inv, titleIn)
{
    private lateinit var book: DiaryBook

    override fun init()
    {
        super.init()
        addButton(InvisibleButton(guiLeft - 40, guiTop, ySize, { book.prev() }, true))
        addButton(InvisibleButton(guiLeft + xSize - 10, guiTop, ySize, { book.next() }, true))
        book = DiaryBook(xSize - 30, ySize, mc.fontRenderer)

        val player = clientPlayer

        if(player is IPhoenixPlayer)
        {
           val els = ArrayList<ADiaryElement>()
           val chapters = player.getOpenedChapters()

           for ((id, date) in chapters)
           {
               els.addAll(makeParagraph(font, xSize / 2 - 5, Chapter.values()[id].getText()))
               els.add(RightAlignedTextElement(date.toString()))
           }
           book.add(els)
        }
    }

    override fun resize(mc: Minecraft, width: Int, height: Int)
    {
        super.resize(mc, width, height)
        book = DiaryBook(xSize - 30, ySize, phoenix.utils.mc.fontRenderer)

        val player = clientPlayer

        if(player is IPhoenixPlayer)
        {
            val els = ArrayList<ADiaryElement>()
            val chapters = player.getOpenedChapters()

            for ((id, date) in chapters)
            {
                els.addAll(makeParagraph(font, xSize / 2 - 5, Chapter.values()[id].getText()))
                els.add(RightAlignedTextElement(date.toString()))
            }
            book.add(els)
        }
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {}

    override fun renderBackground()
    {
        RenderUtils.drawRectScalable(backgoundTexture, guiLeft, guiTop, xSize.toDouble(), ySize.toDouble(), blitOffset)
    }

    override fun render(p1: Int, p2: Int, p3: Float)
    {
        super.render(p1, p2, p3)
        this.renderBackground()
        book.render(this, font, xSize, ySize, guiLeft, guiTop, blitOffset)
    }
}