package statemachine.client.models;

import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class PartRenderer extends ModelRenderer
{
    final boolean isMaster;
    public PartRenderer(Model model, boolean isMasterIn)
    {
        super(model);
        isMaster = isMasterIn;
    }

    public PartRenderer(Model model)
    {
        super(model);
        isMaster = false;
    }

    public PartRenderer(Model model, int texOffX, int texOffY, boolean isMasterIn)
    {
        super(model, texOffX, texOffY);
        isMaster = isMasterIn;
    }

    public PartRenderer(int textureWidthIn, int textureHeightIn, int textureOffsetXIn, int textureOffsetYIn, boolean isMasterIn)
    {
        super(textureWidthIn, textureHeightIn, textureOffsetXIn, textureOffsetYIn);
        isMaster = isMasterIn;
    }
}
