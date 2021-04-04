package phoenix.init

import net.minecraft.client.renderer.RenderType
import phoenix.Phoenix
import phoenix.utils.TextureLocation

interface ICaudaArmorMaterial
{
    val defend  : Double
    val texture : RenderType
    val durability : Int
    val speedModifier : Double
}

enum class PhoenixCaudaArmorMaterials(override val defend : Double, override val speedModifier : Double, override val durability : Int, override val texture : RenderType) : ICaudaArmorMaterial
{
    STEEL  (0.7, 0.6, 800,  TextureLocation(Phoenix.MOD_ID, "textures/models/armor/steel_cauda_armor.png")),
    CERAMIC(0.8, 0.9, 1000, TextureLocation(Phoenix.MOD_ID, "textures/models/armor/ceramic_cauda_armor.png"));

    constructor(defend: Double, speedModifier: Double, durability: Int, texture: TextureLocation) : this(defend, speedModifier, durability, PhoenixRenderTypes.initTexture(texture, texture.path.split("/").last()))
}