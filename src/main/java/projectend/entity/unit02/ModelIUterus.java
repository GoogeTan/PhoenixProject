package projectend.entity.unit02;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelIUterus extends ModelBase
{
    ModelRenderer block;
    public ModelIUterus()
    {
        block = new ModelRenderer(this, 0, 0);
        block.addBox(-2, 90, -2, 16, 16, 16);

    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);

        block.render(scale);
    }
}
