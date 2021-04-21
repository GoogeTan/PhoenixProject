package phoenix.enity.boss

import net.minecraft.entity.EntityType
import net.minecraft.world.World
import phoenix.enity.boss.phase.PhaseType

class DragonAshStageEntity(type: EntityType<out DragonAshStageEntity>, worldIn: World) : AbstractEnderDragonEntity(type, worldIn)
{
    override var LANDING: PhaseType = PhaseType.ASH_LANDING
    override var TAKEOFF: PhaseType = PhaseType.ASH_TAKEOFF
    override var DYING: PhaseType   = PhaseType.ASH_DYING
    override var HOVER: PhaseType   = PhaseType.ASH_HOVER
}