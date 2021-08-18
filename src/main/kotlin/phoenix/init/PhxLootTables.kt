package phoenix.init

import net.minecraft.util.ResourceLocation
import net.minecraft.world.gen.feature.structure.IStructurePieceType
import net.minecraft.world.storage.loot.LootTables
import phoenix.MOD_ID
import phoenix.Phoenix
import phoenix.world.structures.remains.RemainsPieces.Piece

object PhxLootTables
{
    lateinit var REMAINS_LOOTTABLE : ResourceLocation
    lateinit var REMAINS_PIECES : IStructurePieceType

    fun init()
    {
        REMAINS_LOOTTABLE = LootTables.register(ResourceLocation(MOD_ID, "remains_house"))
        REMAINS_PIECES = IStructurePieceType.register(::Piece, "RemainsHouse")
    }
}