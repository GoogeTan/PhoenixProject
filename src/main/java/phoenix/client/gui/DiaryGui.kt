package phoenix.client.gui

import net.minecraft.client.gui.screen.inventory.ContainerScreen
import net.minecraft.client.gui.widget.Widget
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import phoenix.Phoenix
import phoenix.client.gui.diaryPages.Chapters
import phoenix.client.gui.diaryPages.DiaryBook
import phoenix.client.gui.diaryPages.elements.ADiaryElement
import phoenix.client.gui.diaryPages.elements.RightAlignedTextElement
import phoenix.containers.DiaryContainer
import phoenix.utils.*
import phoenix.utils.Date
import java.util.*

class DiaryGui(screenContainer: DiaryContainer, inv: PlayerInventory, titleIn: ITextComponent) : ContainerScreen<DiaryContainer>(screenContainer, inv, titleIn)
{
    //private val DIARY_TEXTURE = ResourceLocation(Phoenix.MOD_ID, "textures/gui/diary_container.png")
    private val DIARY = ResourceLocation(Phoenix.MOD_ID, "textures/gui/diary_2.png")
    private lateinit var book: DiaryBook
    val container: DiaryContainer = screenContainer

    override fun init()
    {
        super.init()
        addButton(InvisibleButton(guiLeft - 40, guiTop, (ySize * 1.8), { book.prev() }, true))
        addButton(InvisibleButton(guiLeft + xSize - 10, guiTop, (ySize * 1.8), { book.next() }, true))
        book = DiaryBook(xSize - 30, (ySize * 1.8), mc.fontRenderer)
        val player = clientPlayer

        if(player is IChapterReader)
        {
            val els = ArrayList<ADiaryElement>()
            val chapters: ArrayList<Pair<Int, Date>> = player.getOpenedChapters()

            for (i in chapters)
            {
                val ch = Chapters.values()[i.first.toInt()]
                els.addAll(DiaryUtils.makeParagraph(font, xSize / 2 - 15, ch.getText()))
                els.add(RightAlignedTextElement(i.second.toString()))
            }

            book.add(els)
        }
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int)
    {
    }

    override fun renderBackground()
    {
        RenderUtils.drawRectScalable(DIARY, guiLeft, guiTop, xSize.toDouble(), ySize.toDouble(), blitOffset)
    }

    override fun render(p1: Int, p2: Int, p3: Float)
    {
        super.render(p1, p2, p3)
        this.renderBackground()
        book.render(this, font, xSize, ySize, guiLeft, guiTop, blitOffset)
        for (button: Widget in buttons)
        {
            button.render(p1, p2, p3)
        }
    }

    init
    {
        xSize = (xSize * 1.8).toInt()
        ySize = (ySize * 1.8).toInt()
    }
}