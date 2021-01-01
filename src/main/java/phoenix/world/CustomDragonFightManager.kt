package phoenix.world

import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.Unit
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.end.DragonFightManager
import net.minecraft.world.end.DragonSpawnState
import net.minecraft.world.server.ServerWorld
import net.minecraft.world.server.TicketType

class CustomDragonFightManager(world: ServerWorld, compound: CompoundNBT, dim: EndDimension) : DragonFightManager(world, compound, dim)
{
    override fun tick()
    {
        bossInfo.isVisible = !dragonKilled
        if (++ticksSinceLastPlayerScan >= 20)
        {
            updatePlayers()
            ticksSinceLastPlayerScan = 0
        }
        if (!bossInfo.players.isEmpty())
        {
            this.world.chunkProvider.registerTicket(TicketType.DRAGON, ChunkPos(0, 0), 9, Unit.INSTANCE)
            val flag = this.isFightAreaLoaded
            if (scanForLegacyFight && flag)
            {
                scanForLegacyFight()
                scanForLegacyFight = false
            }
            if (respawnState != null)
            {
                if (crystals == null && flag)
                {
                    respawnState = null
                    tryRespawnDragon()
                }
                val state2 = when(respawnState)
                {
                    DragonSpawnState.END -> CustomDragonSpawnState.END;
                    DragonSpawnState.START -> CustomDragonSpawnState.START;
                    DragonSpawnState.PREPARING_TO_SUMMON_PILLARS -> CustomDragonSpawnState.PREPARING_TO_SUMMON_PILLARS;
                    DragonSpawnState.SUMMONING_PILLARS -> CustomDragonSpawnState.SUMMONING_PILLARS;
                    DragonSpawnState.SUMMONING_DRAGON -> CustomDragonSpawnState.SUMMONING_DRAGON;
                }
                state2.process(this.world, this, crystals, respawnStateTicks++, exitPortalLocation)
            }
            if (!dragonKilled)
            {
                if ((dragonUniqueId == null || ++ticksSinceDragonSeen >= 1200) && flag)
                {
                    findOrCreateDragon()
                    ticksSinceDragonSeen = 0
                }
                if (++ticksSinceCrystalsScanned >= 100 && flag)
                {
                    findAliveCrystals()
                    ticksSinceCrystalsScanned = 0
                }
            }
        } else
        {
            this.world.chunkProvider.releaseTicket(TicketType.DRAGON, ChunkPos(0, 0), 9, Unit.INSTANCE)
        }
    }
}
