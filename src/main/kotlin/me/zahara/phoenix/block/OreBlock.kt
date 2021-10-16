package me.zahara.phoenix.block

import net.minecraft.world.level.block.Block

class OreBlock(properties: Properties) : Block(properties.ifAbsent(
    requiresCorrectToolForDrops = true
))
{
}