package phoenix.init

import net.minecraft.util.ResourceLocation
import net.minecraft.world.gen.feature.structure.IStructurePieceType
import net.minecraft.world.storage.loot.LootTables
import phoenix.Phoenix
import phoenix.world.structures.remains.RemainsPieces.Piece

object PhoenixLootTables
{
    lateinit var REMAINS : ResourceLocation;
    lateinit var REMAINS_PIECES : IStructurePieceType
    fun init()
    {
        REMAINS = LootTables.register(ResourceLocation(Phoenix.MOD_ID, "remains_house"))
        REMAINS_PIECES = IStructurePieceType.register(::Piece, "RemainsHouse")
    }
}