package phoenix.world.biomes

import net.minecraft.entity.EntityClassification
import net.minecraft.entity.EntityType
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.util.Direction
import net.minecraft.util.SharedSeedRandom
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.GenerationSettings
import net.minecraft.world.gen.GenerationStage
import phoenix.init.PhxBlocks
import phoenix.other.defaultSettingsForEnd
import phoenix.other.getDownHeight
import phoenix.world.builders.Builders

object SmallIslandsUnderBiome : Biome(defaultSettingsForEnd(Builders.UNDER, Builders.UNDER_CONFIG))
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
            var position = worldIn.getDownHeight(pos.add(random.nextInt(15), 0, random.nextInt(15)), 70)
            if(position.y > 2)
            {
                worldIn.setBlockState(position, PhxBlocks.seta.defaultState.with(BlockStateProperties.AGE_0_3, random.nextInt(3)), 2)
                if (random.nextInt(3) == 0)
                {
                    position = worldIn.getDownHeight(position.add(random.nextInt(1) - 2, 0, random.nextInt(1) - 2), 70)
                    if(position.y > 2)
                    {
                        worldIn.setBlockState(
                            position,
                            PhxBlocks.seta.defaultState
                                .with(BlockStateProperties.AGE_0_3, random.nextInt(3))
                                .with(BlockStateProperties.HORIZONTAL_FACING, Direction.Plane.HORIZONTAL.random(random)), 2
                        )
                    }
                }
            }
        }
    }

    init
    {
        addSpawn(EntityClassification.MONSTER, SpawnListEntry(EntityType.ENDERMAN, 10, 4, 4))
    }
}
