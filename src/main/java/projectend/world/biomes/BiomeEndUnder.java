package projectend.world.biomes;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeEndDecorator;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import projectend.BlocksRegister;

import java.util.Random;

public class BiomeEndUnder extends Biome
{
    private static final IBlockState AIR = Blocks.AIR.getDefaultState();
    private static final IBlockState END_STONE = Blocks.END_STONE.getDefaultState();
    public static BiomeProperties properties = new BiomeProperties("Under-wold");
    private Random randy;
    static {
        properties.setTemperature(Biomes.SKY.getDefaultTemperature());
        properties.setRainfall(Biomes.SKY.getRainfall());
        properties.setRainDisabled();
    }
    public BiomeEndUnder()
    {
        super(properties);
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.spawnableMonsterList.add(new SpawnListEntry(EntityEnderman.class, 10, 4, 4));
        this.topBlock = END_STONE;
        this.fillerBlock = END_STONE;
        this.decorator = new BiomeEndDecorator();
        randy = new Random();
    }

    @Override
    public BiomeDecorator createBiomeDecorator(){ return new BiomeDecoratorEndBiomes(); }

    public void decorate(World world, Random rand, BlockPos pos)
    {
        for (int x = 0; x < 16; x++)
        {
            for (int z = 0; z < 16; z++)
            {
                int height = getEndDownSurfaceHeight(world, pos.add(x, 0, z));
                if(height != 0)
                {
                    for (int i = height; i < height + 8; i++)
                    {
                        if (world.getBlockState(new BlockPos(pos.add(x, i, z))).getBlock().equals(Blocks.END_STONE))
                        {
                            world.setBlockState(new BlockPos(pos.add(x, i, z)), BlocksRegister.FERTILE_END_STONE.getDefaultState());
                        }
                    }
                }
            }
        }
        System.out.println("Under generated at " + pos.getX() + " " + pos.getZ());
        super.decorate(world, rand, pos);

    }

    public static int getEndDownSurfaceHeight(World world, BlockPos pos)
    {
        IBlockState state;
        for (int i = 10; i < 30; i++)
        {
            state = world.getBlockState(pos.add(0, i, 0));
            if(state != AIR)
                return i;
        }

        return 0;
    }
}
