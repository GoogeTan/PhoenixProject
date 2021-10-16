package me.zahara.phoenix.init

import me.zahara.phoenix.block.PhoenixBlock
import me.zahara.phoenix.block.PortalBlock
import me.zahara.phoenix.block.create
import me.zahara.phoenix.init.registery.AutoRegisterer
import me.zahara.phoenix.modId
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.material.Material
import net.minecraft.world.level.material.MaterialColor

object PhxBlocks : AutoRegisterer<Block>(Block::class.java, modId)
{
    val ash : PhoenixBlock by register("ash") {
        PhoenixBlock(create(
            material = Material.STONE,
            materialColor = MaterialColor.COLOR_LIGHT_GRAY,
            explosionResistance = 0.1f,
            destroyTime = 0.1f,
            soundType = SoundType.SAND
        ))
    }

    val dirt : PhoenixBlock by register("dirt") {
        PhoenixBlock(create(
            material = Material.DIRT,
            materialColor = MaterialColor.COLOR_BROWN,
            explosionResistance = 1f,
            destroyTime = 0.5f,
            soundType = SoundType.GRAVEL
        ))
    }

    val stone : PhoenixBlock by register("stone") {
        PhoenixBlock(create(
            material = Material.STONE,
            materialColor = MaterialColor.COLOR_LIGHT_GRAY,
            explosionResistance = 1.5f,
            destroyTime = 1.5f,
            requiresCorrectToolForDrops = true
        ))
    }

    val deepStone : PhoenixBlock by register("deep_stone") {
        PhoenixBlock(create(
            material = Material.STONE,
            materialColor = MaterialColor.COLOR_LIGHT_GRAY,
            explosionResistance = 4f,
            destroyTime = 2.5f,
            requiresCorrectToolForDrops = true
        ))
    }

    val portal : PhoenixBlock by register("portal") {
        PortalBlock(create(
            material = Material.PORTAL,
            materialColor = MaterialColor.COLOR_LIGHT_GRAY,
            explosionResistance = -1f,
            destroyTime = -1f,
            requiresCorrectToolForDrops = true
        ))
    }
}