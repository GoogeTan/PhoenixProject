package me.zahara.phoenix.biome

import me.zahara.phoenix.init.PhxBiomes
import net.minecraft.world.level.newbiome.context.Context
import net.minecraft.world.level.newbiome.layer.traits.C0Transformer

object ValleyLayer : C0Transformer
{
    override fun apply(pContext: Context, pValue: Int): Int
    {
        return if (pValue == 1) PhxBiomes.valley.getId() else pValue
    }
}