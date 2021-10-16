package me.zahara.phoenix.biome

import me.zahara.phoenix.init.PhxBiomes
import net.minecraft.world.level.newbiome.context.Context
import net.minecraft.world.level.newbiome.layer.traits.C0Transformer

object SeaLayer : C0Transformer
{
    override fun apply(pContext: Context, pValue: Int): Int
    {
        if (pContext.nextRandom(2) == 0)
            return pValue
        else
            return PhxBiomes.sea.getId()
    }
}