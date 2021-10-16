package me.zahara.phoenix.biome

import net.minecraft.data.BuiltinRegistries
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.newbiome.context.Context
import net.minecraft.world.level.newbiome.layer.traits.AreaTransformer0

object InitLayer : AreaTransformer0
{
    override fun applyPixel(pContext: Context, pX: Int, pY: Int): Int
    {
        return if (pContext.nextRandom(2) == 0) 1 else 0
    }
}

fun Biome.getId(): Int = BuiltinRegistries.BIOME.getId(this)