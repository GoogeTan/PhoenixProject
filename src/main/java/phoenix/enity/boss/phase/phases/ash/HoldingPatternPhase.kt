package phoenix.enity.boss.phase.phases.ash

import net.minecraft.entity.EntityPredicate
import net.minecraft.entity.item.EnderCrystalEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.pathfinding.Path
import net.minecraft.pathfinding.PathPoint
import net.minecraft.util.DamageSource
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.gen.Heightmap
import net.minecraft.world.gen.feature.EndPodiumFeature
import phoenix.enity.boss.AbstractEnderDragonEntity
import phoenix.enity.boss.phase.PhaseType
import phoenix.enity.boss.phase.phases.Phase


class HoldingPatternPhase(dragonIn: AbstractEnderDragonEntity) : Phase(dragonIn)
{
    var currentPath : Path? = null

    private var clockwise = false

    override val type = PhaseType.HOLDING_PATTERN

    /**
     * Gives the phase a chance to update its status.
     * Called by dragon's onLivingUpdate. Only used when !worldObj.isRemote.
     */
    override fun serverTick()
    {
        val d0 = if (targetLocation == null) 0.0 else targetLocation!!.squareDistanceTo(dragon.posX, dragon.posY, dragon.posZ)
        if (d0 < 100.0 || d0 > 22500.0 || dragon.collidedHorizontally || dragon.collidedVertically)
        {
            findNewTarget()
        }
    }

    /**
     * Called when this phase is set to active
     */
    override fun initPhase()
    {
        currentPath = null
        targetLocation = null
    }

    /**
     * Returns the location the dragon is flying toward
     */
    override var targetLocation : Vec3d? = null

    private fun findNewTarget()
    {
        if (currentPath != null && currentPath!!.isFinished)
        {
            val topPos = dragon.world!!.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, BlockPos(EndPodiumFeature.END_PODIUM_LOCATION))
            val i = if (dragon.fightManager == null) 0 else dragon.fightManager!!.numAliveCrystals
            if (dragon.rng.nextInt(i + 3) == 0)
            {
                dragon.phaseManager.setPhase(PhaseType.LANDING_APPROACH)
                return
            }
            var dist = 64.0
            val player = dragon.world.getClosestPlayer(field_221117_b, topPos.x.toDouble(), topPos.y.toDouble(), topPos.z.toDouble())
            if (player != null)
            {
                dist = topPos.distanceSq(player.positionVec, true) / 512.0
            }
            if (player != null && !player.abilities.disableDamage && (dragon.rng.nextInt(MathHelper.abs(dist.toInt()) + 2) == 0 || dragon.rng.nextInt(i + 2) == 0))
            {
                strafePlayer(player)
                return
            }
        }
        if (currentPath == null || currentPath!!.isFinished)
        {
            val j = dragon.initPathPoints()
            var k = j
            if (dragon.rng.nextInt(8) == 0)
            {
                clockwise = !clockwise
                k = j + 6
            }
            if (clockwise)
            {
                ++k
            } else
            {
                --k
            }
            if (dragon.fightManager != null && dragon.fightManager!!.numAliveCrystals >= 0)
            {
                k %= 12
                if (k < 0)
                {
                    k += 12
                }
            } else
            {
                k -= 12
                k = k and 7
                k += 12
            }
            currentPath = dragon.findPath(j, k, null as PathPoint?)
            if (currentPath != null)
            {
                currentPath!!.incrementPathIndex()
            }
        }
        navigateToNextPathNode()
    }

    private fun strafePlayer(player: PlayerEntity)
    {
        dragon.phaseManager.setPhase(PhaseType.STRAFE_PLAYER)
        dragon.phaseManager.getPhase<StrafePlayerPhase>(PhaseType.STRAFE_PLAYER)?.setTarget(player)
    }

    private fun navigateToNextPathNode()
    {
        if (currentPath != null && !currentPath!!.isFinished)
        {
            val vec3d = currentPath!!.currentPos
            currentPath!!.incrementPathIndex()
            val d0 = vec3d.x
            val d1 = vec3d.z
            var d2: Double
            do
                d2 = vec3d.y + (dragon.rng.nextFloat() * 20.0f).toDouble()
            while (d2 < vec3d.y)
            targetLocation = Vec3d(d0, d2, d1)
        }
    }

    override fun onCrystalDestroyed(
        crystal: EnderCrystalEntity,
        pos: BlockPos,
        dmgSrc: DamageSource,
        player: PlayerEntity?
    )
    {
        if (player != null && !player.abilities.disableDamage)
        {
            strafePlayer(player)
        }
    }

    companion object
    {
        private val field_221117_b = EntityPredicate().setDistance(64.0)
    }
}
