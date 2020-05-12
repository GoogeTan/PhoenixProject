package projectend.world.structures.Unit01;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.fml.common.IWorldGenerator;
import projectend.Projectend;

import java.util.Random;

public class WorldGenCorn implements IWorldGenerator
{
    private static final ResourceLocation CORN = new ResourceLocation(Projectend.MOD_ID + ":corn");
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        final PlacementSettings settings = new PlacementSettings().setRotation(Rotation.NONE);
        final Template template = world.getSaveHandler().getStructureTemplateManager().getTemplate(world.getMinecraftServer(), CORN);
        if((chunkX == 64 || chunkX == -64) && (chunkZ == 64 || chunkZ == -64))
        template.addBlocksToWorld(world, new BlockPos(
                chunkX * 16 + random.nextInt(200) - random.nextInt(200),
                100 + random.nextInt(20),
                chunkZ * 16 + random.nextInt(200) - random.nextInt(200)),
                settings);
    }
}
