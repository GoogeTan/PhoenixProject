package phoenix.tile

import net.minecraft.nbt.CompoundNBT
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import phoenix.init.PhoenixTiles

class TextTile : TileEntity(PhoenixTiles.TEXT.get())
{
    var text : ITextComponent = StringTextComponent("Just Like a Eva mark 06")

    override fun read(nbt: CompoundNBT)
    {
        if(nbt.contains("text"))
            text = StringTextComponent(nbt.getString("text"))
        super.read(nbt)
    }

    override fun write(nbt: CompoundNBT): CompoundNBT
    {
        nbt.putString("text", text.toString())
        return super.write(nbt)
    }
}