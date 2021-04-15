package phoenix.enity.boss

import net.minecraft.entity.EntityType
import net.minecraft.world.World
import phoenix.enity.boss.phase.PhaseType

class DragonAshStageEntity(type: EntityType<out DragonAshStageEntity>, worldIn: World) : AbstractEnderDragonEntity(type, worldIn)
{
    override val LANDING: PhaseType = PhaseType.ASH_LANDING
    override val TAKEOFF: PhaseType = PhaseType.ASH_TAKEOFF
    override val DYING: PhaseType   = PhaseType.ASH_DYING
}