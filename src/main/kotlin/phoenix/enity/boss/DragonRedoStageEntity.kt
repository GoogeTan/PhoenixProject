package phoenix.enity.boss

import net.minecraft.entity.EntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.DamageSource
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraft.world.gen.Heightmap
import net.minecraft.world.gen.feature.EndPodiumFeature
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import phoenix.enity.boss.phase.PhaseType
import kotlin.math.max
import kotlin.math.min

class DragonRedoStageEntity(type: EntityType<out DragonRedoStageEntity>, worldIn: World) : AbstractEnderDragonEntity(type, worldIn)
{
    override val LANDING: PhaseType = PhaseType.REDO_LANDING
    override val TAKEOFF: PhaseType = PhaseType.REDO_TAKEOFF
    override val DYING: PhaseType   = PhaseType.REDO_DYING
}
