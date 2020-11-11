package phoenix.world.structures.corn;

import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTables;

import java.util.List;
import java.util.Random;

public class CornPieces
{
    private static final ResourceLocation top_location = new ResourceLocation("phoenix:corn/corn");

    private static final BlockPos pieces_to_offset = new BlockPos(3, 5, 5);

    private static final BlockPos offset = BlockPos.ZERO;


    public static void init(TemplateManager manager, BlockPos pos, Rotation rotation, List<StructurePiece> pieces, Random rand)
    {
        pieces.add(new Piece(manager, top_location, pos, rotation, 0));
    }

    public static class Piece extends TemplateStructurePiece
    {
        private final ResourceLocation current_piece;
        private final Rotation rotation;

        public Piece(TemplateManager manager, ResourceLocation location, BlockPos pos, Rotation rotation, int y_offset)
        {
            super(IStructurePieceType.IGLU, 0);
            this.current_piece = location;
            BlockPos blockpos = offset;
            this.templatePosition = pos.add(blockpos.getX(), blockpos.getY() - y_offset, blockpos.getZ());
            this.rotation = rotation;
            this.makeSetup(manager);
        }

        public Piece(TemplateManager manager, CompoundNBT nbt)
        {
            super(IStructurePieceType.IGLU, nbt);
            this.current_piece = new ResourceLocation(nbt.getString("Template"));
            this.rotation = Rotation.valueOf(nbt.getString("Rot"));
            this.makeSetup(manager);
        }

        private void makeSetup(TemplateManager manager)
        {
            Template template = manager.getTemplateDefaulted(this.current_piece);
            PlacementSettings placementsettings = (new PlacementSettings()).setRotation(rotation).setMirror(Mirror.NONE).setCenterOffset(pieces_to_offset).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
            this.setup(template, this.templatePosition, placementsettings);
        }

        /**
         * (abstract) Helper method to read subclass data from NBT
         */
        @Override
        protected void readAdditional(CompoundNBT tagCompound)
        {
            super.readAdditional(tagCompound);
            tagCompound.putString("Template", this.current_piece.toString());
            tagCompound.putString("Rot", rotation.name());
        }

        @Override
        protected void handleDataMarker(String function, BlockPos pos, IWorld worldIn, Random rand, MutableBoundingBox sbb)
        {
            if ("chest".equals(function))
            {
                worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                TileEntity tileentity = worldIn.getTileEntity(pos.down());
                if (tileentity instanceof ChestTileEntity)
                {
                    ((ChestTileEntity) tileentity).setLootTable(LootTables.CHESTS_IGLOO_CHEST, rand.nextLong());
                }
            }
        }

        /**
         * Create Structure Piece
         */
        @Override
        public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn,
                              MutableBoundingBox mutableBoundingBoxIn, ChunkPos chunkPosIn)
        {
            PlacementSettings settings = (new PlacementSettings())
                    .setRotation(rotation)
                    .setMirror(Mirror.NONE)
                    .setCenterOffset(pieces_to_offset)
                    .addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);

            BlockPos blockpos = this.templatePosition.add(Template.transformedBlockPos(settings, new BlockPos(3 - offset.getX(), 0, -offset.getZ())));
            int height = worldIn.getHeight(Heightmap.Type.WORLD_SURFACE_WG, blockpos.getX(), blockpos.getZ());
            BlockPos blockpos2 = this.templatePosition;
            //this.templatePosition = this.templatePosition.add(0, height - 90 - 1, 0);
            boolean flag = super.create(worldIn, chunkGeneratorIn, randomIn, mutableBoundingBoxIn, chunkPosIn);
            this.templatePosition = blockpos2;
            return flag;
        }
    }
}
