package phoenix.world.builders

import com.mojang.datafixers.Dynamic
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilderConfig


/*
* Используется для генерации биомов, где нужен дополнительный блок.
* Пример UnderBiome
*/
class AdvancedSurfaceBuilderConfig : ISurfaceBuilderConfig
{
    private val top: BlockState
    private val under: BlockState
    val underWater: BlockState
    val advanced: BlockState

    /*
   * topMaterial - Верхний материал. Для примера тавка.
   * underMaterial - Подземный материал. К примеру камушек.
   * underWaterMaterial - Подводный материал. К примеру гравий или песок.
   * advanced - Доп материал для биомов с особыми генераторами. К примеру плодородный камень для Under.
    */
    constructor(
        topMaterial: BlockState,
        underMaterial: BlockState,
        underWaterMaterial: BlockState,
        advancedMaterial: BlockState
    )
    {
        top = topMaterial
        under = underMaterial
        underWater = underWaterMaterial
        advanced = advancedMaterial
    }

    constructor(topMaterial: Block, underMaterial: Block, underWaterMaterial: Block, advancedMaterial: Block)
    {
        top = topMaterial.defaultState
        under = underMaterial.defaultState
        underWater = underWaterMaterial.defaultState
        advanced = advancedMaterial.defaultState
    }

    override fun getTop(): BlockState = top

    override fun getUnder(): BlockState = under

    companion object
    {
        fun<T> deserialize(dynamic: Dynamic<T>): AdvancedSurfaceBuilderConfig
        {
            val top_material        = dynamic["top_material"].map(BlockState::deserialize).orElse(Blocks.AIR.defaultState)
            val under_material      = dynamic["under_material"].map(BlockState::deserialize).orElse(Blocks.AIR.defaultState)
            val underwater_material = dynamic["underwater_material"].map(BlockState::deserialize).orElse(Blocks.AIR.defaultState)
            val advanced            = dynamic["advanced"].map(BlockState::deserialize).orElse(Blocks.AIR.defaultState)
            return AdvancedSurfaceBuilderConfig(top_material, under_material, underwater_material, advanced)
        }
    }
}
