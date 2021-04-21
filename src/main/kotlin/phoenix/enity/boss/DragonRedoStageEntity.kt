package phoenix.enity.boss

import net.minecraft.entity.EntityType
import net.minecraft.world.World
import phoenix.enity.boss.phase.PhaseType

class DragonRedoStageEntity(type: EntityType<out DragonRedoStageEntity>, worldIn: World) : AbstractEnderDragonEntity(type, worldIn)
{
    override var LANDING: PhaseType = PhaseType.REDO_LANDING
    override var TAKEOFF: PhaseType = PhaseType.REDO_TAKEOFF
    override var DYING: PhaseType   = PhaseType.REDO_DYING
    override var HOVER: PhaseType   = PhaseType.REDO_HOVER
}
