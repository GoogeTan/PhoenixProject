package projectend.blocks.title;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class GostBlockTile extends TileEntity
{
    private int count;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setInteger("count", this.count);
        return super.writeToNBT(tagCompound);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        this.count = tagCompound.getInteger("count");
        super.readFromNBT(tagCompound);
    }

    public int getCount() {  return this.count;  }

    public void incrementCount()
    {
        this.count++;
        this.markDirty();
    }

    public void decrementCount()
    {
        this.count--;
        this.markDirty();
    }

}
