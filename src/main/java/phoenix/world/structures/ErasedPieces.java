package phoenix.world.structures;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockState;
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
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTables;
import phoenix.init.PhoenixFeatures;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class ErasedPieces
{
    private static final ResourceLocation top = new ResourceLocation("igloo/top");
    private static final ResourceLocation middle = new ResourceLocation("igloo/middle");
    private static final ResourceLocation bottom = new ResourceLocation("igloo/bottom");
    private static final Map<ResourceLocation, BlockPos> positionMap = ImmutableMap.of(top, new BlockPos(3, 5, 5), middle, new BlockPos(1, 3, 1), bottom, new BlockPos(3, 6, 7));
    private static final Map<ResourceLocation, BlockPos> positionMap2 = ImmutableMap.of(top, BlockPos.ZERO, middle, new BlockPos(2, -3, 4), bottom, new BlockPos(0, -3, -2));

    public static void initTemplate(TemplateManager manager, BlockPos pos, Rotation rotation, List<StructurePiece> pieces, Random rand, NoFeatureConfig config)
    {
        if (rand.nextDouble() < 0.5D)
        {
            int depth = rand.nextInt(8) + 4;
            pieces.add(new ErasedPieces.Piece(manager, bottom, pos, rotation, depth * 3));

            for (int j = 0; j < depth - 1; ++j)
            {
                pieces.add(new ErasedPieces.Piece(manager, middle, pos, rotation, j * 3));
            }
        }

        pieces.add(new ErasedPieces.Piece(manager, top, pos, rotation, 0));
    }

    public static class Piece extends TemplateStructurePiece
    {
        private final ResourceLocation location;
        private final Rotation rotation;

        public Piece(TemplateManager manager, ResourceLocation location, BlockPos pos, Rotation rotation, int p_i49313_5_)
        {
            super(PhoenixFeatures.ERASED_PIESES, 0);
            this.location = location;
            BlockPos blockpos = ErasedPieces.positionMap2.get(location);
            this.templatePosition = pos.add(blockpos.getX(), blockpos.getY() - p_i49313_5_, blockpos.getZ());
            this.rotation = rotation;
            this.func_207614_a(manager);
        }

        public Piece(TemplateManager manager, CompoundNBT nbt)
        {
            super(PhoenixFeatures.ERASED_PIESES, nbt);
            this.location = new ResourceLocation(nbt.getString("Template"));
            this.rotation = Rotation.valueOf(nbt.getString("Rot"));
            this.func_207614_a(manager);
        }

        private void func_207614_a(TemplateManager manager)
        {
            Template template = manager.getTemplateDefaulted(this.location);
            PlacementSettings placementsettings =
                    new PlacementSettings()
                    .setRotation(this.rotation)
                    .setMirror(Mirror.NONE)
                    .setCenterOffset(ErasedPieces.positionMap.get(this.location))
                    .addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);


            this.setup(template, this.templatePosition, placementsettings);
        }

        /**
         * (abstract) Helper method to read subclass data from NBT
         */
        @Override
        protected void readAdditional(CompoundNBT tagCompound)
        {
            super.readAdditional(tagCompound);
            tagCompound.putString("Template", this.location.toString());
            tagCompound.putString("Rot", this.rotation.name());
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
         *
         * @param worldIn              world
         * @param chunkGeneratorIn     chunkGenerator
         * @param randomIn             random
         * @param mutableBoundingBoxIn mutableBoundingBox
         * @param chunkPosIn           chunkPos
         */
        @Override
        public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, MutableBoundingBox mutableBoundingBoxIn, ChunkPos chunkPosIn)
        {
            PlacementSettings placementsettings = (new PlacementSettings())
                    .setRotation(this.rotation)
                    .setMirror(Mirror.NONE)
                    .setCenterOffset(ErasedPieces.positionMap.get(this.location))
                    .addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);

            BlockPos piecePos = ErasedPieces.positionMap2.get(this.location);
            BlockPos postRotationPosition = this.templatePosition.add(Template.transformedBlockPos(placementsettings, new BlockPos(3 - piecePos.getX(), 0, -piecePos.getZ())));

            int height = worldIn.getHeight(Heightmap.Type.WORLD_SURFACE_WG, postRotationPosition.getX(), postRotationPosition.getZ());

            BlockPos tmp = this.templatePosition;
            this.templatePosition = this.templatePosition.add(0, height - 90 - 1, 0);
            boolean flag = super.create(worldIn, chunkGeneratorIn, randomIn, mutableBoundingBoxIn, chunkPosIn);
            if (this.location.equals(ErasedPieces.top))
            {
                BlockPos blockpos3 = this.templatePosition.add(Template.transformedBlockPos(placementsettings, new BlockPos(3, 0, 5)));
                BlockState blockstate = worldIn.getBlockState(blockpos3.down());
                if (!blockstate.isAir() && blockstate.getBlock() != Blocks.LADDER)
                {
                    worldIn.setBlockState(blockpos3, Blocks.SNOW_BLOCK.getDefaultState(), 3);
                }
            }

            this.templatePosition = tmp;
            return flag;
        }
    }
}