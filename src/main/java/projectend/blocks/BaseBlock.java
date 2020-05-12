package projectend.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BaseBlock extends Block
{
	public BaseBlock(String name, Material material, float hardness, float resistanse, SoundType soundType)
	{
		super(material);
		this.setRegistryName(name);
		this.setHardness(hardness);
		this.setResistance(resistanse);
		this.setSoundType(soundType);
	}
}