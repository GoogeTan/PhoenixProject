package phoenix.init

import phoenix.Phoenix
import phoenix.utils.TextureLocation

interface ICaudaArmorMaterial
{
    val defend  : Float
    val texture : TextureLocation
    val durability : Int
    val speedModifier : Float
}

enum class PhxCaudaArmorMaterials(override val defend : Float, override val speedModifier : Float, override val durability : Int) : ICaudaArmorMaterial
{
    STEEL(0.7f, 0.6f, 800)
    {
        override val texture: TextureLocation = TextureLocation(Phoenix.MOD_ID, "textures/models/armor/steel_cauda_armor.png")
    },
    CERAMIC(0.8f, 0.9f, 1000)
    {
        override val texture: TextureLocation = TextureLocation(Phoenix.MOD_ID, "textures/models/armor/ceramic_cauda_armor.png")
    };
}