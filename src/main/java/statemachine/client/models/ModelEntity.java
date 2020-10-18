package statemachine.client.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.function.Function;

public abstract class ModelEntity<Y extends PartList, T extends LivingEntity> extends EntityModel<T>
{
    private AnimationState<Y> currentAnimState;
    public ModelEntity(Function<ResourceLocation, RenderType> renderType)
    {
        super(renderType);
    }

    public abstract Y getParts();

    public abstract ArrayList<AnimationState<Y>> getAnimations();

    public abstract AnimationState<Y> currentAnimation(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch);

    @Override
    public void setRotationAngles(@Nonnull T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        AnimationState<Y> now = currentAnimation(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        if(!currentAnimState.equals(now))
        {
            currentAnimState = currentAnimState.blend(now, 0.5D);
        }

        currentAnimState.applyRotationsTo(getParts());
    }

    @ParametersAreNonnullByDefault
    @Override
    public final void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        for (PartRenderer renderer : getParts().getMasterParts())
        {
            renderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        }
    }
}