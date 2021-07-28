package phoenix.client.gui

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screen.Screen
import phoenix.Phoenix
import phoenix.api.entity.IPhoenixPlayer
import phoenix.client.gui.diary.Chapter
import phoenix.client.gui.diary.DiaryBook
import phoenix.client.gui.diary.elements.ADiaryElement
import phoenix.client.gui.diary.elements.RightAlignedTextElement
import phoenix.other.*

class DiaryGui : Screen("Diary".toTextComponent())
{
    val guiLeft
        get() = (width - xSize) / 2
    val guiTop
        get() = (height - ySize) / 2
    val xSize = textureWidth
    val ySize = textureHeight

    private lateinit var book: DiaryBook

    override fun init()
    {
        super.init()
        addButton(InvisibleButton(guiLeft - 40, guiTop, ySize, { book.prev() }, true))
        addButton(InvisibleButton(guiLeft +  - 10, guiTop, ySize, { book.next() }, true))
        initBook()
    }

    override fun resize(mc: Minecraft, width: Int, height: Int)
    {
        super.resize(mc, width, height)
        initBook()
    }

    override fun renderBackground()
    {
        drawRectScalable(backgroundTexture, guiLeft, guiTop, xSize.toDouble(), ySize.toDouble(), blitOffset)
    }

    override fun render(p1: Int, p2: Int, p3: Float)
    {
        super.render(p1, p2, p3)
        this.renderBackground()
        translate(guiLeft.toFloat(), guiTop.toFloat()) {
            book.render(this, font, xSize, ySize, blitOffset)
        }
    }

    private fun initBook()
    {
        try
        {
            book = DiaryBook(xSize - 30, ySize, mc!!.fontRenderer)

            val player = clientPlayer

            if (player is IPhoenixPlayer)
            {
                val elements = ArrayList<ADiaryElement>()
                val chapters = player.getOpenedChapters()

                for ((id, date) in chapters)
                {
                    elements.addAll(makeParagraph(mc!!.fontRenderer, xSize / 2 - 5, Chapter.values()[id].getText()))
                    elements.add(RightAlignedTextElement(date.toString()))
                }
                book.add(elements)
            } else
            {
                error("Player mixin is broken. So diary wont work")
                return
            }
            error(book.toString())
        } catch (e: Exception)
        {
            error(e)
        }
    }

    companion object
    {
        private val backgroundTexture = TextureLocation(Phoenix.MOD_ID, "textures/gui/diary_2.png")
        const val textureWidth = 253
        const val textureHeight = 187
    }
}