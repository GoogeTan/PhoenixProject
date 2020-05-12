package projectend.world.gen.unit02;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.*;
import projectend.world.BiomeRegistrar;
import projectend.world.biomes.BiomeEndUnder;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class MapGenRemains extends MapGenStructure
{
    private int distance;

    public MapGenRemains()
    {
        this.distance = 32;
    }

    @Override
    public String getStructureName()
    {
        return "remains";
    }

    @Nullable
    @Override
    public BlockPos getNearestStructurePos(World worldIn, BlockPos pos, boolean findUnexplored)
    {
        this.world = worldIn;
        return findNearestStructurePosBySpacing(worldIn, this, pos, this.distance, 8, 10387312, false, 100, findUnexplored);
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ)
    {
        return world.getBiome(new BlockPos(chunkX * 16, 0 ,chunkZ * 16)) == BiomeRegistrar.END_UNDER &&
                BiomeEndUnder.getEndDownSurfaceHeight(world, new BlockPos(new BlockPos(chunkX * 16, 0 ,chunkZ * 16))) >= 14;
    }

    @Override
    protected StructureStart getStructureStart(int chunkX, int chunkZ)
    {
        return new Start();
    }

    public static class Start extends StructureStart
    {

        public Start()
        {
        }

        public Start(World worldIn, Random rand, int x, int z, int size)
        {

        }

        @Override
        public void generateStructure(World worldIn, Random rand, StructureBoundingBox structurebb)
        {
            super.generateStructure(worldIn, rand, structurebb);
        }
    }
}
