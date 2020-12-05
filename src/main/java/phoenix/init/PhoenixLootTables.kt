package phoenix.init

import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.ResourceLocation
import net.minecraft.world.gen.feature.structure.IStructurePieceType
import net.minecraft.world.gen.feature.template.TemplateManager
import net.minecraft.world.storage.loot.LootTables
import phoenix.Phoenix
import phoenix.world.structures.remains.RemainsPieces

object PhoenixLootTables
{
    lateinit var REMAINS_HOUSE : ResourceLocation;
    lateinit var REMAINS_HOUSE_PIECES : IStructurePieceType
    fun init()
    {
        REMAINS_HOUSE = LootTables.register(ResourceLocation(Phoenix.MOD_ID, "remains/house"))
        REMAINS_HOUSE_PIECES = IStructurePieceType.register({ manager: TemplateManager, nbt: CompoundNBT -> RemainsPieces.Piece(manager, nbt) }, "RemainsHouse")
    }
}