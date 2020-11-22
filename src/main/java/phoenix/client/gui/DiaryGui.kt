package phoenix.client.gui

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screen.inventory.ContainerScreen
import net.minecraft.client.gui.widget.Widget
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import phoenix.Phoenix
import phoenix.Phoenix.Companion.LOGGER
import phoenix.client.gui.diaryPages.DiaryBook
import phoenix.client.gui.diaryPages.elements.DiaryChapter
import phoenix.client.gui.diaryPages.elements.ImageElement
import phoenix.containers.DiaryContainer
import phoenix.utils.DiaryUtils
import phoenix.utils.RenderUtils
import phoenix.utils.TextureUtils


class DiaryGui(screenContainer: DiaryContainer, inv: PlayerInventory, titleIn: ITextComponent) : ContainerScreen<DiaryContainer>(screenContainer, inv, titleIn)
{
    private val DIARY_TEXTURE = ResourceLocation(Phoenix.MOD_ID, "textures/gui/diary_container.png")
    private val DIARY = ResourceLocation(Phoenix.MOD_ID, "textures/gui/diary_2.png")
    private lateinit var book: DiaryBook
    val container: DiaryContainer = screenContainer

    override fun init()
    {
        super.init()
        addButton(InvisibleButton(guiLeft - 40, guiTop, ySize, { book-- }, true))
        addButton(InvisibleButton(guiLeft + xSize - 10, guiTop, ySize, { book++ }, true))
        book = DiaryBook(xSize - 30, ySize, Minecraft.getInstance().fontRenderer)
        var par = ArrayList(DiaryUtils.toElements(Minecraft.getInstance().fontRenderer, (xSize - 40) / 2,
                "Друг мой, друг мой," +
                        "Я очень и очень болен.  " +
                        "Сам не знаю, откуда взялась эта боль." +
                        "То ли ветер свистит " +
                        "Над пустым и безлюдным полем, " +
                        "То ль, как рощу в сентябрь, " +
                        "Осыпает мозги алкоголь. " +
                        "Голова моя машет ушами, " +
                        "Как крыльями птица. " +
                        "Ей на шее ноги " +
                        "Маячить больше невмочь. " +
                        "Черный человек, " +
                        "Черный, черный, " +
                        "Черный человек " +
                        "На кровать ко мне садится, " +
                        "Черный человек " +
                        "Спать не дает мне всю ночь. " +
                        "Черный человек " +
                        "Водит пальцем по мерзкой книге " +
                        "И, гнусавя надо мной, " +
                        "Как над усопшим монах, " +
                        "Читает мне жизнь " +
                        "Какого-то прохвоста и забулдыги, " +
                        "Нагоняя на душу тоску и страх. " +
                        "Черный человек, " +
                        "Черный, черный! " +
                        "«Слушай, слушай, — " +
                        "Бормочет он мне, — " +
                        "В книге много прекраснейших " +
                        "Мыслей и планов. " +
                        "Этот человек " +
                        "Проживал в стране " +
                        "Самых отвратительных " +
                        "Громил и шарлатанов. " +
                        "В декабре в той стране " +
                        "Снег до дьявола чист, " +
                        "И метели заводят "))
        //par.add(ImageElement(DIARY_TEXTURE, xSize - 30 - 40, ySize - 30));
        book.add(DiaryChapter(xSize - 30, ySize - 50, par))
        par = ArrayList(DiaryUtils.toElements(Minecraft.getInstance().fontRenderer, (xSize - 40) / 2,
                "Веселые прялки. " +
                        "Был человек тот авантюрист, " +
                        "Но самой высокой " +
                        "И лучшей марки. " +
                        "Был он изящен, " +
                        "К тому ж поэт, " +
                        "Хоть с небольшой, " +
                        "Но ухватистой силою, " +
                        "И какую-то женщину, " +
                        "Сорока с лишним лет, " +
                        "Называл скверной девочкой " +
                        "И своею милою. " +
                        "Счастье, — говорил он, — " +
                        "Есть ловкость ума и рук. " +
                        "Все неловкие души " +
                        "За несчастных всегда известны. " +
                        "Это ничего, " +
                        "Что много мук " +
                        "Приносят изломанные " +
                        "И лживые жесты. " +
                        "В житейскую стынь, " +
                        "При тяжелых утратах "))
        //book.add(DiaryChapter(xSize - 30, ySize - 50, par))
        val d = TextureUtils.getTextureSize(DIARY_TEXTURE)
        LOGGER.error("${d.key}, ${d.value}")
        book.add(DiaryChapter(xSize - 30, ySize - 50, ImageElement(DIARY_TEXTURE, d.key, d.value)))
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