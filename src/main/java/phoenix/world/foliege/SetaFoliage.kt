package phoenix.world.foliege

import com.mojang.datafixers.Dynamic
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.GenerationSettings
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.NoFeatureConfig
import java.util.*
import java.util.function.Function

class SetaFoliage : Feature<NoFeatureConfig>(Function { d: Dynamic<*> -> NoFeatureConfig.deserialize(d) })
{
    override fun place(worldIn: IWorld, generator: ChunkGenerator<out GenerationSettings>, rand: Random, pos: BlockPos, config: NoFeatureConfig): Boolean
    {
        TODO("Not yet implemented")
    }
}