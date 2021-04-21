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
import phoenix.utils.LogManager

open class AshHoldingPatternPhase(dragonIn: AbstractEnderDragonEntity) : Phase(dragonIn)
{
    var currentPath : Path? = null

    var clockwise = false

    override val type = PhaseType.ASH_HOLDING_PATTERN

    /**
     * Gives the phase a chance to update its status.
     * Called by dragon's onLivingUpdate. Only used when !worldObj.isRemote.
     */
    override fun serverTick()
    {
        val dist = targetLocation?.squareDistanceTo(dragon.posX, dragon.posY, dragon.posZ)?:0.0
        if (dist < 100.0 || dist > 22500.0 || dragon.collidedHorizontally || dragon.collidedVertically)
        {
            findNewTarget()
            LogManager.errorObjects(this, targetLocation, dist, currentPath)
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

    protected open fun findNewTarget()
    {
        if (currentPath != null && currentPath!!.isFinished)
        {
            val topPos = dragon.world!!.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, BlockPos(EndPodiumFeature.END_PODIUM_LOCATION))
            val aliveCrystals = if (dragon.fightManager == null) 0 else dragon.fightManager!!.numAliveCrystals
            if (dragon.rng.nextInt(aliveCrystals + 3) == 0)
            {
                dragon.phaseManager.setPhase(PhaseType.ASH_LANDING_APPROACH)
                return
            }
            var dist = 64.0
            val player = dragon.world.getClosestPlayer(NEW_TARGET_TARGETING, topPos.x.toDouble(), topPos.y.toDouble(), topPos.z.toDouble())
            if (player != null)
            {
                dist = topPos.distanceSq(player.positionVec, true) / 512.0
            }
            if (player != null && !player.abilities.disableDamage && (dragon.rng.nextInt(MathHelper.abs(dist.toInt()) + 2) == 0 || dragon.rng.nextInt(aliveCrystals + 2) == 0))
            {
                strafePlayer(player)
                return
            }
        }
        if (currentPath == null || currentPath!!.isFinished)
        {
            val j = dragon.findClosestNode()
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
            currentPath = dragon.findPath(j, k, null)
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

    protected fun navigateToNextPathNode()
    {
        if (currentPath != null && !currentPath!!.isFinished)
        {
            val pos = currentPath!!.currentPos
            currentPath!!.incrementPathIndex()
            val x = pos.x
            val z = pos.z
            var y: Double
            do
                y = pos.y + (dragon.rng.nextFloat() * 20.0f).toDouble()
            while (y < pos.y)
            targetLocation = Vec3d(x, y, z)
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
        val NEW_TARGET_TARGETING = EntityPredicate().setDistance(64.0)
    }
}
