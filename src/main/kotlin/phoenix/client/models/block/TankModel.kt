package phoenix.client.models.block

import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.model.Material
import net.minecraft.client.renderer.model.Model
import net.minecraft.client.renderer.model.ModelRenderer
import net.minecraft.client.renderer.texture.AtlasTexture
import net.minecraft.fluid.Fluids
import net.minecraft.util.ResourceLocation
import net.minecraft.world.dimension.DimensionType
import phoenix.init.PhoenixRenderTypes
import phoenix.tile.redo.TankTile
import phoenix.utils.RenderUtils.refreshDrawing
import java.util.function.Function

class TankModel(var tileTank : TankTile) : Model(Function { PhoenixRenderTypes.tankTexture })
{
    var TEXTURE_FLUID: ResourceLocation? = null
    var MATERIAL_FLUID: Material? = null
    var fluid = ModelRenderer(this, 0, 0)
    val block = ModelRenderer(this, 0, 0)

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
        block.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha)
        fluid.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha)
    }

    fun render(matrixStackIn: MatrixStack, buffer: IRenderTypeBuffer, packedLightIn: Int, packedOverlayIn: Int)
    {
        matrixStackIn.push()
        TEXTURE_FLUID = try
        {
            tileTank.tank.fluid.fluid.attributes.stillTexture
        } catch (e: Exception)
        {
            null
        }
        if (TEXTURE_FLUID != null)
        {
            MATERIAL_FLUID = Material(AtlasTexture.LOCATION_BLOCKS_TEXTURE, TEXTURE_FLUID)
            val fluidBuilder = MATERIAL_FLUID!!.getBuffer(buffer, { location: ResourceLocation -> RenderType.getEntitySolid(location) })
            if (tileTank.tank.fluid.fluid !== Fluids.WATER)
            {
                fluid.render(matrixStackIn, fluidBuilder, packedLightIn, packedOverlayIn, 1.0f, 1.0f, 1.0f, 1.0f)
            } else
            {
                if (tileTank.world!!.dimension.type === DimensionType.THE_END)
                {
                    fluid.render(
                        matrixStackIn,
                        fluidBuilder,
                        packedLightIn,
                        packedOverlayIn,
                        80 / 256f,
                        43 / 256f,
                        226 / 256f,
                        0.7f
                    )
                } else
                {
                    val watercolor = tileTank.world!!.getBiome(tileTank.pos).getWaterColor()
                    fluid.render(matrixStackIn, fluidBuilder, packedLightIn, packedOverlayIn, watercolor / 10000 / 100f, watercolor / 100 % 100 / 100f, 1.0f, 0.7f)
                }
            }
        }
        val builder = buffer.getBuffer(PhoenixRenderTypes.tankTexture)
        block.render(matrixStackIn, builder, packedLightIn, packedOverlayIn)
        refreshDrawing(builder, PhoenixRenderTypes.tankTexture)
        matrixStackIn.pop()
    }

    init
    {
        block.setRotationPoint(0f, 0f, 0f)
        block.addBox(0f, 0f, 0f, 16f, 16f, 16f)
        fluid.setRotationPoint(0f, 0f, 0f)
        fluid.addBox(2f, 2f, 2f, 12f, (12 * tileTank.tank.fluidAmount / tileTank.tank.capacity).toFloat(), 12f)
    }
}