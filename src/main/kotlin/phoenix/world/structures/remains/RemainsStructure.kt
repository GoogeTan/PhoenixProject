package phoenix.world.structures.remains

import com.mojang.datafixers.Dynamic
import net.minecraft.util.Rotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.NoFeatureConfig
import net.minecraft.world.gen.feature.structure.ScatteredStructure
import net.minecraft.world.gen.feature.structure.Structure
import net.minecraft.world.gen.feature.structure.Structure.IStartFactory
import net.minecraft.world.gen.feature.structure.StructureStart
import net.minecraft.world.gen.feature.template.TemplateManager
import phoenix.world.structures.remains.RemainsPieces.init
import java.util.function.Function

class RemainsStructure :
    ScatteredStructure<NoFeatureConfig>(Function { p_214639_0_: Dynamic<*> ->
        NoFeatureConfig.deserialize(
            p_214639_0_
        )
    })
{
    override fun getStructureName(): String = "Remains"

    override fun getSize(): Int = 3

    override fun getStartFactory(): IStartFactory
    {
        return IStartFactory{
                structure: Structure<*>, chunkPosX: Int, chunkPosZ: Int, mbb: MutableBoundingBox, references: Int, seed: Long ->
            Start(structure, chunkPosX, chunkPosZ, mbb, references, seed)
        }
    }

    override fun getSeedModifier(): Int = 14357618

    class Start(structure: Structure<*>, chunkPosX: Int, chunkPosZ: Int, mbb: MutableBoundingBox, references: Int, seed: Long) : StructureStart(structure, chunkPosX, chunkPosZ, mbb, references, seed)
    {
        override fun init(
            generator: ChunkGenerator<*>,
            templateManagerIn: TemplateManager,
            chunkX: Int,
            chunkZ: Int,
            biomeIn: Biome
        )
        {
            val i = chunkX * 16
            val j = chunkZ * 16
            val blockpos = BlockPos(i, 90, j)
            val rotation = Rotation.values()[rand.nextInt(Rotation.values().size)]
            init(generator, templateManagerIn, blockpos, rotation, components, rand)
            recalculateStructureSize()
        }
    }
}