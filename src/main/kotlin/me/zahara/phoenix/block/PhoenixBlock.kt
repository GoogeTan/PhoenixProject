package me.zahara.phoenix.block

import net.minecraft.tags.Tag
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraftforge.common.Tags

open class PhoenixBlock(properties: Properties, vararg tagsIn: Tag<out Block>) : Block(
    properties.ifAbsent(
        explosionResistance = 3f,
        destroyTime = 1.0f
    )
)
{
}