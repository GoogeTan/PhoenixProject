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
    var base = ModelRenderer(this, 0, 0)

    fun render(matrixStackIn: MatrixStack, bufferIn: IVertexBuilder, packedLightIn: Int, packedOverlayIn: Int)
    {
        base.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn)
    }

    override fun render(p0: MatrixStack, p1: IVertexBuilder, p2: Int, p3: Int, p4: Float, p5: Float, p6: Float, p7: Float)
    {
        base.render(p0, p1, p2, p3)
    }
}