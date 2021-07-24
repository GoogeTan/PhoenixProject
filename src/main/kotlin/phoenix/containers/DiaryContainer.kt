package phoenix.containers

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.Container
import net.minecraft.inventory.container.INamedContainerProvider
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import phoenix.init.PhxContainers

class DiaryContainer(id: Int) : Container(PhxContainers.CAUDA, id), INamedContainerProvider
{
    constructor(id: Int, inventory: PlayerInventory) : this(id)
    var name: ITextComponent = StringTextComponent("Zahara")

    fun setName(nameIn: ITextComponent): DiaryContainer
    {
        name = nameIn
        return this
    }

    override fun canInteractWith(playerIn: PlayerEntity): Boolean = true

    override fun getDisplayName(): ITextComponent = StringTextComponent("${name.formattedText}'s Diary")

    override fun createMenu(id: Int, inventory: PlayerInventory, entity: PlayerEntity): Container = this
}
