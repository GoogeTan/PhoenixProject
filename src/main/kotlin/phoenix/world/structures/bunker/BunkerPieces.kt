package phoenix.world.structures.bunker

import com.mojang.datafixers.util.Pair
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.ResourceLocation
import net.minecraft.util.Rotation
import net.minecraft.util.SharedSeedRandom
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.jigsaw.JigsawManager
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece
import net.minecraft.world.gen.feature.jigsaw.SingleJigsawPiece
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece
import net.minecraft.world.gen.feature.structure.IStructurePieceType
import net.minecraft.world.gen.feature.structure.StructurePiece
import net.minecraft.world.gen.feature.template.TemplateManager
import phoenix.Phoenix
import phoenix.utils.SizedArrayList

object BunkerPieces
{
    fun func_215139_a(
        chunkGeneratorIn: ChunkGenerator<*>,
        templateManagerIn: TemplateManager,
        posIn: BlockPos,
        structurePieces: List<StructurePiece>,
        rand: SharedSeedRandom
    )
    {
        JigsawManager.addPieces(
            ResourceLocation("pillager_outpost/base_plates"),
            7,
            { manager: TemplateManager, jigsawPieceIn: JigsawPiece, pos: BlockPos, seed: Int, rotationIn: Rotation, boundsIn: MutableBoundingBox -> BunkerPiece(manager, jigsawPieceIn, pos, seed, rotationIn, boundsIn) },
            chunkGeneratorIn,
            templateManagerIn,
            posIn,
            structurePieces,
            rand
        )
    }

    init
    {
        JigsawManager.REGISTRY.register(
            JigsawPattern
            (
                ResourceLocation(Phoenix.MOD_ID, "pillager_outpost/base_plates"),
                ResourceLocation(Phoenix.MOD_ID, "empty"),
                SizedArrayList.of(Pair.of(SingleJigsawPiece("pillager_outpost/base_plate"), 1)),
                JigsawPattern.PlacementBehaviour.RIGID
            )
        )
    }


    class BunkerPiece : AbstractVillagePiece
    {
        constructor(
            templateManagerIn: TemplateManager,
            jigsawPieceIn: JigsawPiece,
            posIn: BlockPos,
            groundLevelDelta: Int,
            rotation: Rotation,
            boundingBox: MutableBoundingBox
        ) : super(
            IStructurePieceType.PCP,
            templateManagerIn,
            jigsawPieceIn,
            posIn,
            groundLevelDelta,
            rotation,
            boundingBox
        )

        constructor(
            templateManagerIn: TemplateManager,
            tag: CompoundNBT,
            structurePieceTypeIn: IStructurePieceType
        ) : super(templateManagerIn, tag, structurePieceTypeIn)
    }

}