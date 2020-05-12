package projectend.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

@SideOnly(Side.CLIENT)
public class ModelMindOre extends ModelBase
{
    public ModelRenderer base;

    public ModelMindOre()
    {
        this.textureWidth = 64;
        this.textureHeight = 64;
        base = new ModelRenderer(this, 0, 0);
        base.addBox(0,0,0, 16,16,16);
    }


}
