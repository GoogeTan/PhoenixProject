package phoenix.world.biomes;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import phoenix.init.PhoenixBlocks;
import phoenix.init.PhoenixFeatures;
import phoenix.utils.GenerationUtils;
import phoenix.world.builders.Builders;

public class UnderBiome extends Biome
{
    public UnderBiome()
    {
        super(GenerationUtils.ADV.deafultSettingsForEnd(Builders.UNDER, Builders.UNDER_CONFIG));
        this.addStructure(Feature.END_CITY.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        DefaultBiomeFeatures.addEndCity(this);
        this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.CHORUS_PLANT.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG)
                .withPlacement(Placement.CHORUS_PLANT.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
        this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.ENDERMAN, 10, 4, 4));
    }

    @Override
    public void decorate(GenerationStage.Decoration stage, ChunkGenerator<? extends GenerationSettings> chunkGenerator, IWorld worldIn, long seed, SharedSeedRandom random, BlockPos pos)
    {
        super.decorate(stage, chunkGenerator, worldIn, seed, random, pos);
        System.out.println("UNDER GENERATED AT " + pos.getX() + " " + pos.getZ());
        BlockPos position = getDownHeight(worldIn, pos.getX(), pos.getX(), 0, 30);
        if (worldIn.getBlockState(position.up()).getBlock() == PhoenixBlocks.FERTILE_END_STONE.get())
        {
            worldIn.setBlockState(position, PhoenixBlocks.KIKIN_FRUIT.get().getDefaultState(), 2);
        }

    }

    public static BlockPos getDownHeight(IWorld worldIn, int x, int z, int min, int max)
    {
        int r  = max, l = min, m;
        while (r - l > 1)
        {
            m = r + l / 2;
            if(worldIn.isAirBlock(new BlockPos(x, m, z))) l = m;
            else r = m;
        }
        return new BlockPos(x, r, z);
    }
}
