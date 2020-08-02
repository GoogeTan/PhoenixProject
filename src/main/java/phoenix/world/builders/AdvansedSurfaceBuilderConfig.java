package phoenix.world.builders;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilderConfig;

import javax.annotation.Nonnull;

/*
* Используется для генерации биомов, где нужен дополнительный блок.
* Пример UnderBiome
 */
public class AdvansedSurfaceBuilderConfig implements ISurfaceBuilderConfig
{
    private final BlockState top;
    private final BlockState under;
    private final BlockState underWater;
    private final BlockState advanced;
    /*
    * topMaterial - Верхний материал. Для примера тавка.
    * underMaterial - Подземный материал. К примеру камушек.
    * underWaterMaterial - Подводный материал. К примеру гравий или песок.
    * advanced - Доп материал для биомов с особыми генераторами. К примеру плодородный камень для Under.
     */
    public AdvansedSurfaceBuilderConfig(BlockState topMaterial, BlockState underMaterial, BlockState underWaterMaterial, BlockState advancedMaterial)
    {
        this.top        = topMaterial;
        this.under      = underMaterial;
        this.underWater = underWaterMaterial;
        this.advanced   = advancedMaterial;
    }

    public AdvansedSurfaceBuilderConfig(Block topMaterial, Block underMaterial, Block underWaterMaterial, Block advancedMaterial)
    {
        this.top        = topMaterial.getDefaultState();
        this.under      = underMaterial.getDefaultState();
        this.underWater = underWaterMaterial.getDefaultState();
        this.advanced   = advancedMaterial.getDefaultState();
    }

    @Nonnull
    @Override
    public BlockState getTop()
    {
        return top;
    }

    @Nonnull
    @Override
    public BlockState getUnder()
    {
        return under;
    }

    public BlockState getUnderWater()
    {
        return underWater;
    }

    public BlockState getAdvanced()
    {
        return advanced;
    }

    public static AdvansedSurfaceBuilderConfig deserialize(Dynamic<?> p_215455_0_) {
        BlockState top_material        = p_215455_0_.get("top_material")       .map(BlockState::deserialize).orElse(Blocks.AIR.getDefaultState());
        BlockState under_material      = p_215455_0_.get("under_material")     .map(BlockState::deserialize).orElse(Blocks.AIR.getDefaultState());
        BlockState underwater_material = p_215455_0_.get("underwater_material").map(BlockState::deserialize).orElse(Blocks.AIR.getDefaultState());
        BlockState advanced            = p_215455_0_.get("advanced")           .map(BlockState::deserialize).orElse(Blocks.AIR.getDefaultState());
        return new AdvansedSurfaceBuilderConfig(top_material, under_material, underwater_material, advanced);
    }
}
