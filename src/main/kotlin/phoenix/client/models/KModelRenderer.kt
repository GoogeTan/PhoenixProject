package phoenix.client.models

import net.minecraft.client.renderer.model.Model
import net.minecraft.client.renderer.model.ModelRenderer

class KModelRenderer : ModelRenderer
{
    var xOffset : Float = 0.0f
        set(value)
        {
            rotationPointX -= field
            rotationPointX += value
            field = value
        }
    var yOffset : Float = 0.0f
        set(value)
        {
            rotationPointY -= field
            rotationPointY += value
            field = value
        }
    var zOffset : Float = 0.0f
        set(value)
        {
            rotationPointZ -= field
            rotationPointZ += value
            field = value
        }

    constructor(model: Model) : super(model)
    constructor(model: Model, texOffX: Int, texOffY: Int) : super(model, texOffX, texOffY)
    constructor(textureWidthIn: Int, textureHeightIn: Int, textureOffsetXIn: Int, textureOffsetYIn: Int) : super(textureWidthIn, textureHeightIn, textureOffsetXIn, textureOffsetYIn)
}