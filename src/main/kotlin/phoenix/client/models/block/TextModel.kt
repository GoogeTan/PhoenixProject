package phoenix.client.models.block

import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.model.Model
import net.minecraft.client.renderer.model.ModelRenderer
import net.minecraft.util.ResourceLocation
import java.util.function.Function

class TextModel : Model(Function { location: ResourceLocation -> RenderType.getEntitySolid(location) })
{
    private var base : ModelRenderer = ModelRenderer(this, 0, 0)

    init
    {
        base.addBox(0F, 0F, 0F, 16F, 16F, 16F)
    }

    fun render(matrixStackIn: MatrixStack, bufferIn: IVertexBuilder, packedLightIn: Int, packedOverlayIn: Int)
    {
        base.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn)
    }

    override fun render(matrixStackIn: MatrixStack, bufferIn: IVertexBuilder, packedLightIn: Int, packedOverlayIn: Int, red: Float, green: Float, blue: Float, alpha: Float)
    {
        base.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn)
    }
}