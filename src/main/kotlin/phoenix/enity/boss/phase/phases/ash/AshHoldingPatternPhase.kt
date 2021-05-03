package phoenix.enity.boss.phase.phases.ash

import net.minecraft.entity.EntityPredicate
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.pathfinding.Path
import net.minecraft.pathfinding.PathPoint
import net.minecraft.util.DamageSource
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.gen.Heightmap
import net.minecraft.world.gen.feature.EndPodiumFeature
import phoenix.enity.EnderCrystalEntity
import phoenix.enity.boss.AbstractEnderDragonEntity
import phoenix.enity.boss.phase.PhaseType
import phoenix.enity.boss.phase.phases.Phase

open class AshHoldingPatternPhase(dragonIn: AbstractEnderDragonEntity) : Phase(dragonIn)
{
    protected var currentPath: Path? = null
    override var targetLocation: Vec3d? = null
    protected var clockwise = false
    override val type: PhaseType
        get() = PhaseType.ASH_HOLDING_PATTERN


    /**
     * Gives the phase a chance to update its status.
     * Called by dragon's onLivingUpdate. Only used when !worldObj.isRemote.
     */
    override fun serverTick()
    {
        val d0 = if (targetLocation == null) 0.0 else targetLocation!!.squareDistanceTo(
            dragon.posX,
            dragon.posY,
            dragon.posZ
        )
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

    protected open fun findNewTarget()
    {
        if (currentPath != null && currentPath!!.isFinished)
        {
            val blockpos = dragon.world.getHeight(
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                BlockPos(EndPodiumFeature.END_PODIUM_LOCATION)
            )
            val i = if (dragon.fightManager == null) 0 else dragon.fightManager!!.numAliveCrystals
            if (dragon.rng.nextInt(i + 3) == 0)
            {
                dragon.phaseManager.setPhase(PhaseType.ASH_LANDING_APPROACH)
                return
            }
            var d0 = 64.0
            val playerentity = dragon.world.getClosestPlayer(
                NEW_TARGET_TARGETING,
                blockpos.x.toDouble(), blockpos.y.toDouble(), blockpos.z.toDouble()
            )
            if (playerentity != null)
            {
                d0 = blockpos.distanceSq(playerentity.positionVec, true) / 512.0
            }
            if (playerentity != null && !playerentity.abilities.disableDamage && (dragon.rng.nextInt(
                    MathHelper.abs(
                        d0.toInt()
                    ) + 2
                ) == 0 || dragon.rng.nextInt(i + 2) == 0)
            )
            {
                strafePlayer(playerentity)
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
                k = k % 12
                if (k < 0)
                {
                    k += 12
                }
            } else
            {
                k = k - 12
                k = k and 7
                k = k + 12
            }
            currentPath = dragon.findPath(j, k, null as PathPoint?)
            if (currentPath != null)
            {
                currentPath!!.incrementPathIndex()
            }
        }
        navigateToNextPathNode()
    }

    protected open fun strafePlayer(player: PlayerEntity)
    {
        dragon.phaseManager.setPhase(PhaseType.ASH_STRAFE_PLAYER)
        dragon.phaseManager.getPhase<AshStrafePlayerPhase>(PhaseType.ASH_STRAFE_PLAYER)?.setTarget(player)
    }

    protected open fun navigateToNextPathNode()
    {
        if (currentPath != null && !currentPath!!.isFinished)
        {
            val vec3d = currentPath!!.currentPos
            currentPath!!.incrementPathIndex()
            val d0 = vec3d.x
            val d1 = vec3d.z
            var d2: Double
            while (true)
            {
                d2 = vec3d.y + (dragon.rng.nextFloat() * 20.0f).toDouble()
                if (d2 >= vec3d.y)
                {
                    break
                }
            }
            targetLocation = Vec3d(d0, d2, d1)
        }
    }

    override fun onCrystalDestroyed(
        crystal: EnderCrystalEntity,
        pos: BlockPos,
        dmgSrc: DamageSource,
        plyr: PlayerEntity?
    )
    {
        if (plyr != null && !plyr.abilities.disableDamage)
        {
            strafePlayer(plyr)
        }
    }

    companion object
    {
        val NEW_TARGET_TARGETING = EntityPredicate().setDistance(64.0)
    }
}
