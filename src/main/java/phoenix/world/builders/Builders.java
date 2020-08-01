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

    //Конфиги для билдеров
    public static final AdvansedSurfaceBuilderConfig UNDER_CONFIG = new AdvansedSurfaceBuilderConfig(Blocks.OBSIDIAN, Blocks.OBSIDIAN, Blocks.OBSIDIAN, PhoenixBlocks.FERTILE_END_STONE.get());

}
