package phoenix.client.models.block

import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.block.BlockState
import net.minecraft.client.renderer.model.Model
import net.minecraft.client.renderer.model.ModelRenderer
import phoenix.blocks.redo.PipeBlock
import phoenix.init.PhoenixRenderTypes
import java.util.function.Function


class PipeModel(stateIn: BlockState) : Model(Function { PhoenixRenderTypes.PIPE })
{
    var base: ModelRenderer
    var n: ModelRenderer
    var s: ModelRenderer
    var w: ModelRenderer
    var e: ModelRenderer
    var u: ModelRenderer
    var d: ModelRenderer
    var state: BlockState

    override fun render(
        matrixStackIn: MatrixStack,
        bufferIn: IVertexBuilder,
        packedLightIn: Int,
        packedOverlayIn: Int,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    )
    {
        base.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha)
        if (state.get(PipeBlock.NORTH)) n.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha)
        if (state.get(PipeBlock.SOUTH)) s.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha)
        if (state.get(PipeBlock.WEST))  w.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha)
        if (state.get(PipeBlock.EAST))  e.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha)
        if (state.get(PipeBlock.UP))    u.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha)
        if (state.get(PipeBlock.DOWN))  d.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha)
    }

    init
    {
        textureHeight = 64
        textureWidth = 64
        state = stateIn
        base = ModelRenderer(this, 64, 64)
        n = ModelRenderer(this, 0, 0)
        s = ModelRenderer(this, 0, 0)
        w = ModelRenderer(this, 0, 0)
        e = ModelRenderer(this, 0, 0)
        u = ModelRenderer(this, 0, 0)
        d = ModelRenderer(this, 0, 0)
        base.setRotationPoint(6f, 6f, 6f)
        n.setRotationPoint(6f, 6f, 0f)
        s.setRotationPoint(6f, 6f, (6 + 4).toFloat())
        e.setRotationPoint((6 + 4).toFloat(), 6f, 6f)
        w.setRotationPoint(0f, 6f, 6f)
        u.setRotationPoint(6f, (6 + 4).toFloat(), 6f)
        d.setRotationPoint(6f, 0f, 6f)
        base.addBox(0f, 0f, 0f, 4f, 4f, 4f)
        n.addBox(0f, 0f, 0f, 4f, 4f, 6f)
        s.addBox(0f, 0f, 0f, 4f, 4f, 6f)
        e.addBox(0f, 0f, 0f, 6f, 4f, 4f)
        w.addBox(0f, 0f, 0f, 6f, 4f, 4f)
        u.addBox(0f, 0f, 0f, 4f, 6f, 4f)
        d.addBox(0f, 0f, 0f, 4f, 6f, 4f)
    }
}
