package phoenix.enity.boss

import net.minecraft.entity.EntityType
import net.minecraft.world.World
import phoenix.enity.boss.phase.PhaseType

class DragonRedoStageEntity(type: EntityType<out DragonRedoStageEntity>, worldIn: World) : AbstractEnderDragonEntity(type, worldIn)
{
    override val LANDING: PhaseType = PhaseType.REDO_LANDING
    override val TAKEOFF: PhaseType = PhaseType.REDO_TAKEOFF
    override val DYING: PhaseType   = PhaseType.REDO_DYING
    override val HOVER: PhaseType   = PhaseType.REDO_HOVER
}
