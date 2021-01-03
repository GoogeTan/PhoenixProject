package phoenix.world.structures.remains;

import com.google.common.collect.ImmutableList;
import mcp.MethodsReturnNonnullByDefault;
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
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import phoenix.init.PhoenixLootTables;
import phoenix.utils.BlockPosUtils;
import phoenix.utils.SizedArrayList;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RemainsPieces
{
    private static final ResourceLocation house = new ResourceLocation("phoenix:remains/house");
    private static final ResourceLocation bed = new ResourceLocation("phoenix:remains/bed");
    private static final ResourceLocation barricade = new ResourceLocation("phoenix:remains/barricade");
    private static final ResourceLocation well = new ResourceLocation("phoenix:remains/well");

    private static final List<ResourceLocation> allPieces = ImmutableList.of(house, bed, barricade, well);

    private static final BlockPos center_offset = new BlockPos(3, 5, 5);
    private static final BlockPos offset = new BlockPos(0, -4, 0);

    public static void init(ChunkGenerator<?> generator, TemplateManager manager, BlockPos pos, Rotation rotation, List<StructurePiece> pieces, Random rand)
    {
        ArrayList<BlockPos> poses = new ArrayList<>();
        for (int j = 0; j < 3 + rand.nextInt(3); j++)
        {
            int x = pos.getX() + rand.nextInt(40) - 20, z = pos.getZ() + rand.nextInt(40) - 20;
            int i = 0;
            while (BlockPosUtils.isNear(new BlockPos(x, generator.getHeight(x, z, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES), z), poses, 10) && i < 20)
            {
                x = pos.getX() + rand.nextInt(50) - 25;
                z = pos.getZ() + rand.nextInt(50) - 25;
                i++;
            }
            BlockPos res = new BlockPos(x, generator.getHeight(x, z, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES), z);
            pieces.add(new RemainsPieces.Piece(manager, allPieces.get(rand.nextInt(allPieces.size())), res, rotation, 0));
            poses.add(res);
        }
    }

    public static class Piece extends TemplateStructurePiece
    {
        private final ResourceLocation current_piece;
        private final Rotation rotation;

        public Piece(TemplateManager manager, ResourceLocation location, BlockPos pos, Rotation rotation, int y_offset)
        {
            super(PhoenixLootTables.REMAINS_PIECES, 0);
            this.current_piece = location;
            this.templatePosition = pos.add(offset.getX(), offset.getY() - y_offset, offset.getZ());
            this.rotation = rotation;
            this.makeSetup(manager);
        }

        public Piece(TemplateManager manager, CompoundNBT nbt)
        {
            super(PhoenixLootTables.REMAINS_PIECES, nbt);
            this.current_piece = new ResourceLocation(nbt.getString("Template"));
            this.rotation = Rotation.valueOf(nbt.getString("Rot"));
            this.makeSetup(manager);
        }

        private void makeSetup(TemplateManager manager)
        {
            Template template = manager.getTemplateDefaulted(this.current_piece);
            PlacementSettings placementsettings = (new PlacementSettings()).setRotation(rotation).setMirror(Mirror.NONE).setCenterOffset(center_offset).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
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
                    ((ChestTileEntity) tileentity).setLootTable(PhoenixLootTables.REMAINS, rand.nextLong());
                }
            }
        }

        /**
         * Create Structure Piece
         */
        @Override
        public boolean create(IWorld worldIn,    ChunkGenerator<?> chunkGeneratorIn,    Random randomIn,
                                 MutableBoundingBox mutableBoundingBoxIn,    ChunkPos chunkPosIn)
        {
            PlacementSettings settings = new PlacementSettings()
                                                .setRotation(rotation)
                                                .setMirror(Mirror.NONE)
                                                .setCenterOffset(center_offset)
                                                .addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);

            BlockPos blockpos = offset;
            BlockPos blockpos1 = this.templatePosition.add(Template.transformedBlockPos(settings, new BlockPos(3 - blockpos.getX(), 0, -blockpos.getZ())));
            int height = worldIn.getHeight(Heightmap.Type.WORLD_SURFACE_WG, blockpos1.getX(), blockpos1.getZ());
            BlockPos blockpos2 = this.templatePosition;
            this.templatePosition = this.templatePosition.add(0, height - 90 - 1, 0);
            boolean flag = super.create(worldIn, chunkGeneratorIn, randomIn, mutableBoundingBoxIn, chunkPosIn);
            this.templatePosition = blockpos2;
            return flag;
        }
    }
}