package phoenix.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import phoenix.enity.KnifeEntity;
import phoenix.init.PhoenixItems;

public class KnifeRenderer<T extends KnifeEntity> extends EntityRenderer<T> {
    private final ItemRenderer itemRenderer;
    private final float scale = 1;

    public KnifeRenderer(EntityRendererManager renderManagerIn)
    {
        super(renderManagerIn);
        itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    protected int getBlockLight(T entityIn, float partialTicks) {
        return super.getBlockLight(entityIn, partialTicks);
    }

    @Override
    public void render(T entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();
        matrixStackIn.scale(scale, scale, scale);
        //matrixStackIn.rotate(renderManager.getCameraOrientation());
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90.0F));
        matrixStackIn.rotate(Vector3f.XN.rotationDegrees(90.0F));
        this.itemRenderer.renderItem(new ItemStack(PhoenixItems.INSTANCE.getZIRCONIUM_KNIFE().get()), ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
        matrixStackIn.pop();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    public ResourceLocation getEntityTexture(T entity) {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }
}
