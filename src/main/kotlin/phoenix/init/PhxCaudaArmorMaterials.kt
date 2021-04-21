package phoenix.init

import net.minecraft.client.renderer.RenderType
import phoenix.Phoenix
import phoenix.utils.TextureLocation

interface ICaudaArmorMaterial
{
    val defend  : Float
    val texture : RenderType
    val durability : Int
    val speedModifier : Float
}

enum class PhxCaudaArmorMaterials(override val defend : Float, override val speedModifier : Float, override val durability : Int, override val texture : RenderType) : ICaudaArmorMaterial
{
    STEEL  (0.7f, 0.6f, 800,  TextureLocation(Phoenix.MOD_ID, "textures/models/armor/steel_cauda_armor.png")),
    CERAMIC(0.8f, 0.9f, 1000, TextureLocation(Phoenix.MOD_ID, "textures/models/armor/ceramic_cauda_armor.png"));

    constructor(defend: Float, speedModifier: Float, durability: Int, texture: TextureLocation) : this(defend, speedModifier, durability, PhxRenderTypes.initTexture(texture, texture.path.split("/").last()))
}