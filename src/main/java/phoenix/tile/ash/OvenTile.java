package phoenix.tile.ash;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import phoenix.containers.OvenContainer;
import phoenix.init.PhoenixTiles;

import javax.annotation.Nullable;

public class OvenTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider
{
    public OvenTile()
    {
        super(PhoenixTiles.OVEN.get());
    }

    @Override
    public void tick()
    {

    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new StringTextComponent("Oven");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity)
    {
        return new OvenContainer(id, playerInventory);
    }
}
