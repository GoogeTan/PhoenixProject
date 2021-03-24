package phoenix.containers

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.Container
import net.minecraft.inventory.container.ContainerType
import net.minecraft.inventory.container.INamedContainerProvider
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import phoenix.init.PhoenixContainers

class DiaryContainer(id: Int) : Container(PhoenixContainers.GUIDE, id), INamedContainerProvider
{
    var name: ITextComponent = StringTextComponent("Zahara")

    fun setName(nameIn: ITextComponent): DiaryContainer
    {
        name = nameIn
        return this
    }

    override fun canInteractWith(playerIn: PlayerEntity): Boolean = true

    override fun getDisplayName(): ITextComponent = StringTextComponent("${name.formattedText}'s Diary")

    override fun createMenu(id: Int, inventory: PlayerInventory, entity: PlayerEntity): Container = this

    companion object
    {
        fun fromNetwork(): ContainerType<DiaryContainer> = ContainerType { id: Int, _: PlayerInventory? -> DiaryContainer(id) }
    }
}
