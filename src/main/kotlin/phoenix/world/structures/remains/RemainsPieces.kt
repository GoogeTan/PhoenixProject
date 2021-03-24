package phoenix.world.structures.remains

import com.google.common.collect.ImmutableList
import net.minecraft.block.Blocks
import net.minecraft.nbt.CompoundNBT
import net.minecraft.tileentity.ChestTileEntity
import net.minecraft.util.Mirror
import net.minecraft.util.ResourceLocation
import net.minecraft.util.Rotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.IWorld
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.Heightmap
import net.minecraft.world.gen.feature.structure.StructurePiece
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor
import net.minecraft.world.gen.feature.template.PlacementSettings
import net.minecraft.world.gen.feature.template.Template
import net.minecraft.world.gen.feature.template.TemplateManager
import phoenix.init.PhoenixLootTables
import phoenix.utils.BlockPosUtils.isNear
import java.util.*

object RemainsPieces
{
    private val house = ResourceLocation("phoenix:remains/house")
    private val bed = ResourceLocation("phoenix:remains/bed")
    private val barricade = ResourceLocation("phoenix:remains/barricade")
    private val well = ResourceLocation("phoenix:remains/well")
    private val allPieces: List<ResourceLocation> = ImmutableList.of(house, bed, barricade, well)
    private val center_offset = BlockPos(3, 5, 5)
    private val offset = BlockPos(0, -4, 0)
    fun init(
        generator: ChunkGenerator<*>,
        manager: TemplateManager,
        pos: BlockPos,
        rotation: Rotation,
        pieces: MutableList<StructurePiece?>,
        rand: Random
    )
    {
        val poses = ArrayList<BlockPos>()
        for (j in 0 until 3 + rand.nextInt(3))
        {
            var x = pos.x + rand.nextInt(40) - 20
            var z = pos.z + rand.nextInt(40) - 20
            var i = 0
            while (isNear(
                    BlockPos(x, generator.getHeight(x, z, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES), z),
                    poses,
                    10
                ) && i < 20
            )
            {
                x = pos.x + rand.nextInt(50) - 25
                z = pos.z + rand.nextInt(50) - 25
                i++
            }
            val res = BlockPos(x, generator.getHeight(x, z, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES), z)
            pieces.add(
                Piece(
                    manager,
                    allPieces[rand.nextInt(allPieces.size)], res, rotation, 0
                )
            )
            poses.add(res)
        }
    }

    class Piece : TemplateStructurePiece
    {
        private val current_piece: ResourceLocation
        private val rotationn: Rotation

        constructor(
            manager: TemplateManager,
            location: ResourceLocation,
            pos: BlockPos,
            rotation: Rotation,
            y_offset: Int
        ) : super(PhoenixLootTables.REMAINS_PIECES, 0)
        {
            current_piece = location
            templatePosition = pos.add(offset.x, offset.y - y_offset, offset.z)
            this.rotationn = rotation
            makeSetup(manager)
        }

        constructor(manager: TemplateManager, nbt: CompoundNBT) : super(PhoenixLootTables.REMAINS_PIECES, nbt)
        {
            current_piece = ResourceLocation(nbt.getString("Template"))
            this.rotationn = Rotation.valueOf(nbt.getString("Rot"))
            makeSetup(manager)
        }

        private fun makeSetup(manager: TemplateManager)
        {
            val template = manager.getTemplateDefaulted(current_piece)
            val placementsettings = PlacementSettings().setRotation(rotationn).setMirror(Mirror.NONE).setCenterOffset(
                center_offset
            ).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK)
            setup(template, templatePosition, placementsettings)
        }

        /**
         * (abstract) Helper method to read subclass data from NBT
         */
        override fun readAdditional(tagCompound: CompoundNBT)
        {
            super.readAdditional(tagCompound)
            tagCompound.putString("Template", current_piece.toString())
            tagCompound.putString("Rot", rotationn.name)
        }

        override fun handleDataMarker(function: String, pos: BlockPos, worldIn: IWorld, rand: Random, sbb: MutableBoundingBox)
        {
            if ("chest" == function)
            {
                worldIn.setBlockState(pos, Blocks.AIR.defaultState, 3)
                val tileentity = worldIn.getTileEntity(pos.down())
                if (tileentity is ChestTileEntity)
                {
                    tileentity.setLootTable(PhoenixLootTables.REMAINS, rand.nextLong())
                }
            }
        }

        /**
         * Create Structure Piece
         */
        override fun create(worldIn: IWorld, chunkGeneratorIn: ChunkGenerator<*>, randomIn: Random, mutableBoundingBoxIn: MutableBoundingBox, chunkPosIn: ChunkPos): Boolean
        {
            val settings = PlacementSettings()
                .setRotation(rotationn)
                .setMirror(Mirror.NONE)
                .setCenterOffset(center_offset)
                .addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK)
            val blockpos = offset
            val blockpos1 =
                templatePosition.add(Template.transformedBlockPos(settings, BlockPos(3 - blockpos.x, 0, -blockpos.z)))
            val height = worldIn.getHeight(Heightmap.Type.WORLD_SURFACE_WG, blockpos1.x, blockpos1.z)
            val blockpos2 = templatePosition
            templatePosition = templatePosition.add(0, height - 90 - 1, 0)
            val flag = super.create(worldIn, chunkGeneratorIn, randomIn, mutableBoundingBoxIn, chunkPosIn)
            templatePosition = blockpos2
            return flag
        }
    }
}