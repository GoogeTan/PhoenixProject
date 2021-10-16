package me.zahara.phoenix.biome

import me.zahara.phoenix.init.PhxBiomes
import net.minecraft.world.level.newbiome.context.Context
import net.minecraft.world.level.newbiome.layer.traits.C0Transformer

object MountainsLayer : C0Transformer
{
    override fun apply(context: Context, value: Int): Int
    {
         return if (value == 0) PhxBiomes.mountains.getId() else value
    }
}