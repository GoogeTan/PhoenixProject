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
        if(random.nextBoolean())
        {
            BlockPos position = getDownHeight(worldIn, pos.add(random.nextInt(15), 0, random.nextInt(15)), 30);
            if (worldIn.getBlockState(position.up()).getBlock() == PhoenixBlocks.FERTILE_END_STONE.get())
            {
                worldIn.setBlockState(position, PhoenixBlocks.KIKIN_FRUIT.get().getDefaultState(), 2);
            } else if (worldIn.getBlockState(position).getBlock() == PhoenixBlocks.FERTILE_END_STONE.get())
            {
                worldIn.setBlockState(position.down(), PhoenixBlocks.KIKIN_FRUIT.get().getDefaultState(), 2);
            }
        }
    }

    public static BlockPos getDownHeight(IWorld worldIn, BlockPos pos, int max)
    {
        for (int i = 0; i < max; i++)
        {
            if(!worldIn.isAirBlock(pos.add(0, i, 0)))
                return pos.add(0, i, 0);
        }
        return pos.add(0, 0, 0);
    }
}
