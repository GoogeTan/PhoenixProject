package phoenix.world.builders;

import net.minecraft.block.Blocks;
import net.minecraft.world.gen.surfacebuilders.DefaultSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import phoenix.init.PhoenixBlocks;

public class Builders
{
    //Билдеры
    public static final SurfaceBuilder<AdvansedSurfaceBuilderConfig> UNDER = SurfaceBuilder.register("under", new UnderSurfaceBuilder(AdvansedSurfaceBuilderConfig::deserialize));
    public static final SurfaceBuilder<SurfaceBuilderConfig> HEARTVOID = SurfaceBuilder.register("heart", new HeartVoidSurfaceBuilder(SurfaceBuilderConfig::deserialize));

    //Конфиги для билдеров
    public static final AdvansedSurfaceBuilderConfig UNDER_CONFIG     = new AdvansedSurfaceBuilderConfig(Blocks.OBSIDIAN, Blocks.OBSIDIAN, Blocks.OBSIDIAN, PhoenixBlocks.FERTILE_END_STONE.get());
    public static final SurfaceBuilderConfig         HEARTVOID_CONFIG = new SurfaceBuilderConfig(Blocks.END_STONE.getDefaultState(), Blocks.END_STONE.getDefaultState(), Blocks.END_STONE.getDefaultState());

}
