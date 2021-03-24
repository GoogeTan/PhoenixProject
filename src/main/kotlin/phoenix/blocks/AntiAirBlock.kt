package phoenix.blocks

import net.minecraft.block.AirBlock
import net.minecraft.block.material.Material
import phoenix.utils.block.INonItem

class AntiAirBlock : AirBlock(Properties.create(Material.AIR).doesNotBlockMovement().noDrops().notSolid()), INonItem
