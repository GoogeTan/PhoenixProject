package phoenix.world;

import net.minecraft.world.IWorld;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.EndChunkGenerator;
import net.minecraft.world.gen.EndGenerationSettings;

public class HundredthLevelChunkGenerator extends EndChunkGenerator
{
    public HundredthLevelChunkGenerator(IWorld worldIn, BiomeProvider biomeProviderIn, EndGenerationSettings settingsIn)
    {
        super(worldIn, biomeProviderIn, settingsIn);
    }
}
