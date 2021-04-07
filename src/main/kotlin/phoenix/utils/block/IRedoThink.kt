package phoenix.utils.block

import net.minecraft.item.ItemGroup
import phoenix.Phoenix

interface IRedoThink : ICustomGroup
{
    override val tab: ItemGroup
        get() = Phoenix.REDO
}