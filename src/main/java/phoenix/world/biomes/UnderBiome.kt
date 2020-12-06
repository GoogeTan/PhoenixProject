package phoenix.world.biomes

import net.minecraft.entity.EntityClassification
import net.minecraft.entity.EntityType
import net.minecraft.util.SharedSeedRandom
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.DefaultBiomeFeatures
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.GenerationSettings
import net.minecraft.world.gen.GenerationStage
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.IFeatureConfig
import net.minecraft.world.gen.placement.IPlacementConfig
import net.minecraft.world.gen.placement.Placement
import phoenix.init.PhoenixBlocks
import phoenix.utils.GenerationUtils
import phoenix.world.builders.Builders


class UnderBiome : Biome(GenerationUtils.defaultSettingsForEnd(Builders.UNDER, Builders.UNDER_CONFIG))
{
    override fun decorate(stage: GenerationStage.Decoration, chunkGenerator: ChunkGenerator<out GenerationSettings?>, worldIn: IWorld, seed: Long, random: SharedSeedRandom, pos: BlockPos)
    {
        super.decorate(stage, chunkGenerator, worldIn, seed, random, pos)
        if (random.nextBoolean())
        {
            val position = getDownHeight(worldIn, pos.add(random.nextInt(15), 0, random.nextInt(15)), 30)
            if (worldIn.getBlockState(position.up()).block == PhoenixBlocks.FERTILE_END_STONE.get())
            {
                worldIn.setBlockState(position, PhoenixBlocks.KIKIN_FRUIT.get().defaultState, 2)
            }
            else if (worldIn.getBlockState(position).block == PhoenixBlocks.FERTILE_END_STONE.get())
            {
                worldIn.setBlockState(position.down(), PhoenixBlocks.KIKIN_FRUIT.get().defaultState, 2)
            }
        }
    }

    companion object
    {
        fun getDownHeight(worldIn: IWorld, pos: BlockPos, max: Int): BlockPos
        {
            for (i in 0..max - 1)
            {
                if (!worldIn.isAirBlock(pos.add(0, i, 0))) return pos.add(0, i, 0)
            }
            return pos.add(0, 0, 0)
        }
    }

    init
    {
        addStructure(Feature.END_CITY.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG))
        DefaultBiomeFeatures.addEndCity(this)
        addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.CHORUS_PLANT.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG)
                .withPlacement(Placement.CHORUS_PLANT.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)))
        addSpawn(EntityClassification.MONSTER, SpawnListEntry(EntityType.ENDERMAN, 10, 4, 4))
    }
}
