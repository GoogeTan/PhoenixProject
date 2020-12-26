package phoenix.world.biomes

import net.minecraft.entity.EntityClassification
import net.minecraft.entity.EntityType
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.state.properties.BlockStateProperties.AGE_0_3
import net.minecraft.util.Direction
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
import phoenix.utils.LogManager
import phoenix.utils.getDownHeight
import phoenix.world.builders.Builders

class UnderBiome : Biome(GenerationUtils.defaultSettingsForEnd(Builders.UNDER, Builders.UNDER_CONFIG))
{
    override fun decorate   (
        stage           : GenerationStage.Decoration,
        chunkGenerator  : ChunkGenerator<out GenerationSettings>,
        worldIn         : IWorld,
        seed            : Long,
        random          : SharedSeedRandom,
        pos             : BlockPos
                            )
    {
        super.decorate(stage, chunkGenerator, worldIn, seed, random, pos)
        if (random.nextInt(3) == 0)
        {
            var position = worldIn.getDownHeight(pos.add(random.nextInt(15), 0, random.nextInt(15)), 50)
            LogManager.log(this, position.toString())
            if(position.y > 2)
            {
                worldIn.setBlockState(position, PhoenixBlocks.SETA.get().defaultState.with(AGE_0_3, random.nextInt(3)), 2)
                if (random.nextInt(3) == 0)
                {
                    position = worldIn.getDownHeight(position.add(random.nextInt(1) - 2, 0, random.nextInt(1) - 2), 30)
                    if(position.y > 2)
                        worldIn.setBlockState   (
                            position,
                            PhoenixBlocks.SETA.get().defaultState
                            .with(AGE_0_3, random.nextInt(3))
                            .with(BlockStateProperties.HORIZONTAL_FACING, Direction.Plane.HORIZONTAL.random(random)), 2)
                }
            }
        }
    }

    init
    {
        addStructure(Feature.END_CITY.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG))
        DefaultBiomeFeatures.addEndCity(this)
        addFeature(
            GenerationStage.Decoration.VEGETAL_DECORATION,
            Feature.CHORUS_PLANT.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG)
                .withPlacement(Placement.CHORUS_PLANT.configure(IPlacementConfig.NO_PLACEMENT_CONFIG))
        )
        addSpawn(EntityClassification.MONSTER, SpawnListEntry(EntityType.ENDERMAN, 10, 4, 4))
    }
}
