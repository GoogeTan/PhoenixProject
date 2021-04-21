package phoenix.enity.boss

import com.google.common.collect.Lists
import net.minecraft.block.material.Material
import net.minecraft.entity.*
import net.minecraft.entity.item.ExperienceOrbEntity
import net.minecraft.entity.monster.IMob
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.datasync.DataParameter
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.particles.ParticleTypes
import net.minecraft.pathfinding.Path
import net.minecraft.pathfinding.PathHeap
import net.minecraft.pathfinding.PathPoint
import net.minecraft.potion.EffectInstance
import net.minecraft.tags.BlockTags
import net.minecraft.util.*
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.GameRules
import net.minecraft.world.World
import net.minecraft.world.gen.Heightmap
import net.minecraft.world.gen.feature.EndPodiumFeature
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.common.ForgeHooks
import org.apache.logging.log4j.LogManager
import phoenix.enity.EnderCrystalEntity
import phoenix.enity.boss.phase.AbstractPhaseManager
import phoenix.enity.boss.phase.PhaseType
import phoenix.world.CustomDragonFightManager
import phoenix.world.EndDimension
import phoenix.world.StageManager
import kotlin.math.max
import kotlin.math.min

abstract class AbstractEnderDragonEntity(type: EntityType<out AbstractEnderDragonEntity>, worldIn: World) : MobEntity(type, worldIn), IMob
{
    val positions = Array(64) { DoubleArray(3) }
    var posPointer = -1
    val dragonParts: Array<AbstractDragonPartEntity>
    val dragonPartHead: AbstractDragonPartEntity = AbstractDragonPartEntity(this, "head", 1.0f, 1.0f)
    protected val dragonPartNeck: AbstractDragonPartEntity = AbstractDragonPartEntity(this, "neck", 3.0f, 3.0f)
    protected val dragonPartBody: AbstractDragonPartEntity = AbstractDragonPartEntity(this, "body", 5.0f, 3.0f)
    protected val dragonPartTail1: AbstractDragonPartEntity = AbstractDragonPartEntity(this, "tail", 2.0f, 2.0f)
    protected val dragonPartTail2: AbstractDragonPartEntity = AbstractDragonPartEntity(this, "tail", 2.0f, 2.0f)
    protected val dragonPartTail3: AbstractDragonPartEntity = AbstractDragonPartEntity(this, "tail", 2.0f, 2.0f)
    protected val dragonPartRightWing: AbstractDragonPartEntity = AbstractDragonPartEntity(this, "wing", 4.0f, 2.0f)
    protected val dragonPartLeftWing: AbstractDragonPartEntity = AbstractDragonPartEntity(this, "wing", 4.0f, 2.0f)
    var prevAnimTime = 0f
    var animTime = 0f
    var slowed = false
    var deathTicks = 0
    var yRotA = 0f
    var closestEnderCrystal: EnderCrystalEntity? = null
    var fightManager: CustomDragonFightManager? = null
    val phaseManager: AbstractPhaseManager
    protected var growlTime = 100
    protected var sittingDamageReceived = 0
    protected val pathPoints = arrayOfNulls<PathPoint>(24)
    protected val neighbors = IntArray(24)
    protected val pathFindQueue = PathHeap()

    abstract var LANDING: PhaseType
    abstract var TAKEOFF: PhaseType
    abstract var DYING  : PhaseType
    abstract var HOVER  : PhaseType

    override fun registerAttributes()
    {
        super.registerAttributes()
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).baseValue = 200.0
    }

    override fun registerData()
    {
        super.registerData()
        if(HOVER == null) HOVER = StageManager.stageEnum.hoverPhase;
        getDataManager().register(PHASE, HOVER.getId())
    }

    /**
     * Returns a double[3] array with movement offsets, used to calculate trailing tail/neck positions. [0] = yaw offset,
     * [1] = y offset, [2] = unused, always 0. Parameters: buffer index offset, partial ticks.
     */
    fun getMovementOffsets(p_70974_1_: Int, p_70974_2_: Float): DoubleArray
    {
        var p_70974_2_ = p_70974_2_
        if (this.health <= 0.0f)
        {
            p_70974_2_ = 0.0f
        }
        p_70974_2_ = 1.0f - p_70974_2_
        val i = posPointer - p_70974_1_ and 63
        val j = posPointer - p_70974_1_ - 1 and 63
        val adouble = DoubleArray(3)
        var d0 = positions[i][0]
        var d1 = MathHelper.wrapDegrees(positions[j][0] - d0)
        adouble[0] = d0 + d1 * p_70974_2_.toDouble()
        d0 = positions[i][1]
        d1 = positions[j][1] - d0
        adouble[1] = d0 + d1 * p_70974_2_.toDouble()
        adouble[2] = MathHelper.lerp(p_70974_2_.toDouble(), positions[i][2], positions[j][2])
        return adouble
    }

    override fun livingTick()
    {
        if (world.isRemote)
        {
            this.health = this.health
            if (!this.isSilent)
            {
                val f = MathHelper.cos(animTime * (Math.PI.toFloat() * 2f))
                val f1 = MathHelper.cos(prevAnimTime * (Math.PI.toFloat() * 2f))
                if (-0.3f in f1..f)
                {
                    world.playSound(
                        posX,
                        posY,
                        posZ,
                        SoundEvents.ENTITY_ENDER_DRAGON_FLAP,
                        this.soundCategory,
                        5.0f,
                        0.8f + rand.nextFloat() * 0.3f,
                        false
                    )
                }
                if (!phaseManager.currentPhase!!.isStationary && --growlTime < 0)
                {
                    world.playSound(
                        posX,
                        posY,
                        posZ,
                        SoundEvents.ENTITY_ENDER_DRAGON_GROWL,
                        this.soundCategory,
                        2.5f,
                        0.8f + rand.nextFloat() * 0.3f,
                        false
                    )
                    growlTime = 200 + rand.nextInt(200)
                }
            }
        }
        prevAnimTime = animTime
        if (this.health <= 0.0f)
        {
            val f11 = (rand.nextFloat() - 0.5f) * 8.0f
            val f13 = (rand.nextFloat() - 0.5f) * 4.0f
            val f14 = (rand.nextFloat() - 0.5f) * 8.0f
            world.addParticle(
                ParticleTypes.EXPLOSION,
                posX + f11.toDouble(),
                posY + 2.0 + f13.toDouble(),
                posZ + f14.toDouble(),
                0.0,
                0.0,
                0.0
            )
        } else
        {
            updateDragonEnderCrystal()
            val vec3d4 = motion
            var f12 = 0.2f / (MathHelper.sqrt(horizontalMag(vec3d4)) * 10.0f + 1.0f)
            f12 *= Math.pow(2.0, vec3d4.y).toFloat()
            animTime += when
            {
                phaseManager.currentPhase!!.isStationary ->
                {
                    0.1f
                }
                slowed                                   ->
                {
                    f12 * 0.5f
                }
                else                                     ->
                {
                    f12
                }
            }
            rotationYaw = MathHelper.wrapDegrees(rotationYaw)
            if (this.isAIDisabled)
            {
                animTime = 0.5f
            } else
            {
                if (posPointer < 0)
                {
                    for (i in positions.indices)
                    {
                        positions[i][0] = rotationYaw.toDouble()
                        positions[i][1] = posY
                    }
                }
                if (++posPointer == positions.size)
                {
                    posPointer = 0
                }
                positions[posPointer][0] = rotationYaw.toDouble()
                positions[posPointer][1] = posY
                if (world.isRemote)
                {
                    if (newPosRotationIncrements > 0)
                    {
                        val d7 = posX + (interpTargetX - posX) / newPosRotationIncrements.toDouble()
                        val d0 = posY + (interpTargetY - posY) / newPosRotationIncrements.toDouble()
                        val d1 = posZ + (interpTargetZ - posZ) / newPosRotationIncrements.toDouble()
                        val d2 = MathHelper.wrapDegrees(interpTargetYaw - rotationYaw.toDouble())
                        rotationYaw = (rotationYaw.toDouble() + d2 / newPosRotationIncrements.toDouble()).toFloat()
                        rotationPitch =
                            (rotationPitch.toDouble() + (interpTargetPitch - rotationPitch.toDouble()) / newPosRotationIncrements.toDouble()).toFloat()
                        --newPosRotationIncrements
                        setPosition(d7, d0, d1)
                        setRotation(rotationYaw, rotationPitch)
                    }
                    phaseManager.currentPhase?.clientTick()
                } else
                {
                    var iphase = phaseManager.currentPhase!!
                    iphase.serverTick()
                    if (phaseManager.currentPhase !== iphase)
                    {
                        iphase = phaseManager.currentPhase!!
                        iphase.serverTick()
                    }
                    val vec3d = iphase.targetLocation
                    if (vec3d != null)
                    {
                        val d8 = vec3d.x - posX
                        var d9 = vec3d.y - posY
                        val d10 = vec3d.z - posZ
                        val d3 = d8 * d8 + d9 * d9 + d10 * d10
                        val f6 = iphase.maxRiseOrFall
                        val d4 = MathHelper.sqrt(d8 * d8 + d10 * d10).toDouble()
                        if (d4 > 0.0)
                        {
                            d9 = MathHelper.clamp(d9 / d4, (-f6).toDouble(), f6.toDouble())
                        }
                        motion = motion.add(0.0, d9 * 0.01, 0.0)
                        rotationYaw = MathHelper.wrapDegrees(rotationYaw)
                        val d5 = MathHelper.clamp(
                            MathHelper.wrapDegrees(
                                180.0 - MathHelper.atan2(
                                    d8,
                                    d10
                                ) * (180f / Math.PI.toFloat()).toDouble() - rotationYaw.toDouble()
                            ), -50.0, 50.0
                        )
                        val vec3d1 = vec3d.subtract(posX, posY, posZ).normalize()
                        val vec3d2 = Vec3d(
                            MathHelper.sin(rotationYaw * (Math.PI.toFloat() / 180f))
                                .toDouble(),
                            motion.y,
                            (-MathHelper.cos(rotationYaw * (Math.PI.toFloat() / 180f))).toDouble()
                        ).normalize()
                        val f8 = Math.max((vec3d2.dotProduct(vec3d1).toFloat() + 0.5f) / 1.5f, 0.0f)
                        yRotA *= 0.8f
                        yRotA = (yRotA.toDouble() + d5 * iphase.yawFactor.toDouble()).toFloat()
                        rotationYaw += yRotA * 0.1f
                        val f9 = (2.0 / (d3 + 1.0)).toFloat()
                        val f10 = 0.06f
                        moveRelative(0.06f * (f8 * f9 + (1.0f - f9)), Vec3d(0.0, 0.0, -1.0))
                        if (slowed)
                        {
                            move(MoverType.SELF, motion.scale(0.8f.toDouble()))
                        } else
                        {
                            move(MoverType.SELF, motion)
                        }
                        val vec3d3 = motion.normalize()
                        val d6 = 0.8 + 0.15 * (vec3d3.dotProduct(vec3d2) + 1.0) / 2.0
                        motion = motion.mul(d6, 0.91f.toDouble(), d6)
                    }
                }
                renderYawOffset = rotationYaw
                val avec3d = arrayOfNulls<Vec3d>(dragonParts.size)
                for (j in dragonParts.indices)
                {
                    avec3d[j] = Vec3d(dragonParts[j].posX, dragonParts[j].posY, dragonParts[j].posZ)
                }
                val f15 = (getMovementOffsets(5, 1.0f)[1] - getMovementOffsets(
                    10,
                    1.0f
                )[1]).toFloat() * 10.0f * (Math.PI.toFloat() / 180f)
                val f16 = MathHelper.cos(f15)
                val f2 = MathHelper.sin(f15)
                val f17 = rotationYaw * (Math.PI.toFloat() / 180f)
                val f3 = MathHelper.sin(f17)
                val f18 = MathHelper.cos(f17)
                tickPart(dragonPartBody, (f3 * 0.5f).toDouble(), 0.0, (-f18 * 0.5f).toDouble())
                tickPart(dragonPartRightWing, (f18 * 4.5f).toDouble(), 2.0, (f3 * 4.5f).toDouble())
                tickPart(dragonPartLeftWing, (f18 * -4.5f).toDouble(), 2.0, (f3 * -4.5f).toDouble())
                if (!world.isRemote && hurtTime == 0)
                {
                    collideWithEntities(
                        world.getEntitiesInAABBexcluding(
                            this,
                            dragonPartRightWing.boundingBox.grow(4.0, 2.0, 4.0).offset(0.0, -2.0, 0.0),
                            EntityPredicates.CAN_AI_TARGET
                        )
                    )
                    collideWithEntities(
                        world.getEntitiesInAABBexcluding(
                            this,
                            dragonPartLeftWing.boundingBox.grow(4.0, 2.0, 4.0).offset(0.0, -2.0, 0.0),
                            EntityPredicates.CAN_AI_TARGET
                        )
                    )
                    attackEntitiesInList(
                        world.getEntitiesInAABBexcluding(
                            this,
                            dragonPartHead.boundingBox.grow(1.0),
                            EntityPredicates.CAN_AI_TARGET
                        )
                    )
                    attackEntitiesInList(
                        world.getEntitiesInAABBexcluding(
                            this,
                            dragonPartNeck.boundingBox.grow(1.0),
                            EntityPredicates.CAN_AI_TARGET
                        )
                    )
                }
                val f4 = MathHelper.sin(rotationYaw * (Math.PI.toFloat() / 180f) - yRotA * 0.01f)
                val f19 = MathHelper.cos(rotationYaw * (Math.PI.toFloat() / 180f) - yRotA * 0.01f)
                val f5 = getHeadYOffset()
                tickPart(
                    dragonPartHead,
                    (f4 * 6.5f * f16).toDouble(), (f5 + f2 * 6.5f).toDouble(), (-f19 * 6.5f * f16).toDouble()
                )
                tickPart(
                    dragonPartNeck,
                    (f4 * 5.5f * f16).toDouble(), (f5 + f2 * 5.5f).toDouble(), (-f19 * 5.5f * f16).toDouble()
                )
                val adouble = getMovementOffsets(5, 1.0f)
                for (k in 0..2)
                {
                    var enderdragonpartentity: AbstractDragonPartEntity? = null
                    when (k)
                    {
                        0 ->
                        {
                            enderdragonpartentity = dragonPartTail1
                        }
                        1 ->
                        {
                            enderdragonpartentity = dragonPartTail2
                        }
                        2 ->
                        {
                            enderdragonpartentity = dragonPartTail3
                        }
                    }
                    val adouble1 = getMovementOffsets(12 + k * 2, 1.0f)
                    val f7 =
                        rotationYaw * (Math.PI.toFloat() / 180f) + simplifyAngle(adouble1[0] - adouble[0]) * (Math.PI.toFloat() / 180f)
                    val f20 = MathHelper.sin(f7)
                    val f21 = MathHelper.cos(f7)
                    val f22 = 1.5f
                    val f23 = (k + 1).toFloat() * 2.0f
                    tickPart(
                        enderdragonpartentity,
                        (-(f3 * 1.5f + f20 * f23) * f16).toDouble(),
                        adouble1[1] - adouble[1] - ((f23 + 1.5f) * f2).toDouble() + 1.5,
                        ((f18 * 1.5f + f21 * f23) * f16).toDouble()
                    )
                }
                if (!world.isRemote)
                {
                    slowed =
                        destroyBlocksInAABB(dragonPartHead.boundingBox) or destroyBlocksInAABB(dragonPartNeck.boundingBox) or destroyBlocksInAABB(
                            dragonPartBody.boundingBox
                        )
                    if (fightManager != null)
                    {
                        fightManager?.dragonUpdate(this)
                    }
                }
                for (l in dragonParts.indices)
                {
                    dragonParts[l].prevPosX = avec3d[l]!!.x
                    dragonParts[l].prevPosY = avec3d[l]!!.y
                    dragonParts[l].prevPosZ = avec3d[l]!!.z
                    dragonParts[l].lastTickPosX = avec3d[l]!!.x
                    dragonParts[l].lastTickPosY = avec3d[l]!!.y
                    dragonParts[l].lastTickPosZ = avec3d[l]!!.z
                }
            }
        }
    }

    private fun tickPart(part: AbstractDragonPartEntity?, x: Double, y: Double, z: Double)
    {
        part?.setPosition(posX + x, posY + y, posZ + z)
    }

    private fun getHeadYOffset(): Float
    {
        return if (phaseManager.currentPhase?.isStationary == true)
        {
            -1.0f
        } else
        {
            val adouble = getMovementOffsets(5, 1.0f)
            val adouble1 = getMovementOffsets(0, 1.0f)
            (adouble[1] - adouble1[1]).toFloat()
        }
    }

    /**
     * Updates the state of the enderdragon's current endercrystal.
     */
    private fun updateDragonEnderCrystal()
    {
        if (closestEnderCrystal != null)
        {
            if (closestEnderCrystal!!.removed)
            {
                closestEnderCrystal = null
            } else if (ticksExisted % 10 == 0 && this.health < this.maxHealth)
            {
                this.health = this.health + 1.0f
            }
        }
        if (rand.nextInt(10) == 0)
        {
            val list = world.getEntitiesWithinAABB(
                EnderCrystalEntity::class.java, boundingBox.grow(32.0)
            )
            var endercrystalentity: EnderCrystalEntity? = null
            var d0 = Double.MAX_VALUE
            for (endercrystalentity1 in list)
            {
                val d1 = endercrystalentity1.getDistanceSq(this)
                if (d1 < d0)
                {
                    d0 = d1
                    endercrystalentity = endercrystalentity1
                }
            }
            closestEnderCrystal = endercrystalentity
        }
    }

    /**
     * Pushes all entities inside the list away from the enderdragon.
     */
    private fun collideWithEntities(p_70970_1_: List<Entity>)
    {
        val d0 = (dragonPartBody.boundingBox.minX + dragonPartBody.boundingBox.maxX) / 2.0
        val d1 = (dragonPartBody.boundingBox.minZ + dragonPartBody.boundingBox.maxZ) / 2.0
        for (entity in p_70970_1_)
        {
            if (entity is LivingEntity)
            {
                val d2 = entity.getPosX() - d0
                val d3 = entity.getPosZ() - d1
                val d4 = d2 * d2 + d3 * d3
                entity.addVelocity(d2 / d4 * 4.0, 0.2f.toDouble(), d3 / d4 * 4.0)
                if (phaseManager.currentPhase?.isStationary == false && entity.revengeTimer < entity.ticksExisted - 2)
                {
                    entity.attackEntityFrom(DamageSource.causeMobDamage(this), 5.0f)
                    applyEnchantments(this, entity)
                }
            }
        }
    }

    /**
     * Attacks all entities inside this list, dealing 5 hearts of damage.
     */
    private fun attackEntitiesInList(entities: List<Entity>)
    {
        for (entity in entities)
        {
            if (entity is LivingEntity)
            {
                entity.attackEntityFrom(DamageSource.causeMobDamage(this), 10.0f)
                applyEnchantments(this, entity)
            }
        }
    }

    /**
     * Simplifies the value of a number by adding/subtracting 180 to the point that the number is between -180 and 180.
     */
    private fun simplifyAngle(angle : Double): Float = MathHelper.wrapDegrees(angle).toFloat()

    /**
     * Destroys all blocks that aren't associated with 'The End' inside the given bounding box.
     */
    private fun destroyBlocksInAABB(p_70972_1_: AxisAlignedBB): Boolean
    {
        val i = MathHelper.floor(p_70972_1_.minX)
        val j = MathHelper.floor(p_70972_1_.minY)
        val k = MathHelper.floor(p_70972_1_.minZ)
        val l = MathHelper.floor(p_70972_1_.maxX)
        val i1 = MathHelper.floor(p_70972_1_.maxY)
        val j1 = MathHelper.floor(p_70972_1_.maxZ)
        var flag = false
        var flag1 = false
        for (k1 in i..l)
        {
            for (l1 in j..i1)
            {
                for (i2 in k..j1)
                {
                    val blockpos = BlockPos(k1, l1, i2)
                    val blockstate = world.getBlockState(blockpos)
                    val block = blockstate.block
                    if (!blockstate.isAir(world, blockpos) && blockstate.material != Material.FIRE)
                    {
                        if (ForgeHooks.canEntityDestroy(world, blockpos, this) && !BlockTags.DRAGON_IMMUNE.contains(
                                block
                            )
                        )
                        {
                            flag1 = world.removeBlock(blockpos, false) || flag1
                        } else
                        {
                            flag = true
                        }
                    }
                }
            }
        }
        if (flag1)
        {
            val blockpos1 =
                BlockPos(i + rand.nextInt(l - i + 1), j + rand.nextInt(i1 - j + 1), k + rand.nextInt(j1 - k + 1))
            world.playEvent(2008, blockpos1, 0)
        }
        return flag
    }

    fun hurt(part: AbstractDragonPartEntity, source: DamageSource, amount: Float): Boolean
    {
        var p_213403_3_ = amount
        return if (phaseManager.currentPhase?.type === DYING)
        {
            false
        } else
        {
            p_213403_3_ = phaseManager.currentPhase?.onHurt(source, p_213403_3_)!!
            if (part !== dragonPartHead)
            {
                p_213403_3_ = p_213403_3_ / 4.0f + min(p_213403_3_, 1.0f)
            }
            if (p_213403_3_ < 0.01f)
            {
                false
            } else
            {
                if (source.trueSource is PlayerEntity || source.isExplosion)
                {
                    val f = this.health
                    attackDragonFrom(source, p_213403_3_)
                    if (this.health <= 0.0f && !phaseManager.currentPhase!!.isStationary)
                    {
                        this.health = 1.0f
                        phaseManager.setPhase(DYING)
                    }
                    if (phaseManager.currentPhase!!.isStationary)
                    {
                        sittingDamageReceived = (sittingDamageReceived.toFloat() + (f - this.health)).toInt()
                        if (sittingDamageReceived.toFloat() > 0.25f * this.maxHealth)
                        {
                            sittingDamageReceived = 0
                            phaseManager.setPhase(TAKEOFF)
                        }
                    }
                }
                true
            }
        }
    }

    /**
     * Called when the entity is attacked.
     */
    override fun attackEntityFrom(source: DamageSource, amount: Float): Boolean
    {
        if (source is EntityDamageSource && source.isThornsDamage)
        {
            hurt(dragonPartBody, source, amount)
        }
        return false
    }

    /**
     * Provides a way to cause damage to an ender dragon.
     */
    fun attackDragonFrom(source: DamageSource?, amount: Float): Boolean = super.attackEntityFrom(source, amount)

    /**
     * Called by the /kill command.
     */
    override fun onKillCommand()
    {
        this.remove()
        if (fightManager != null)
        {
            fightManager?.dragonUpdate(this)
            fightManager?.processDragonDeath(this)
        }
    }

    /**
     * handles entity death timer, experience orb and particle creation
     */
    override fun onDeathUpdate()
    {
        if (fightManager != null)
        {
            fightManager?.dragonUpdate(this)
        }
        ++deathTicks
        if (deathTicks in 180..200)
        {
            val f = (rand.nextFloat() - 0.5f) * 8.0f
            val f1 = (rand.nextFloat() - 0.5f) * 4.0f
            val f2 = (rand.nextFloat() - 0.5f) * 8.0f
            world.addParticle(
                ParticleTypes.EXPLOSION_EMITTER,
                posX + f.toDouble(),
                posY + 2.0 + f1.toDouble(),
                posZ + f2.toDouble(),
                0.0,
                0.0,
                0.0
            )
        }
        val flag = world.gameRules.getBoolean(GameRules.DO_MOB_LOOT)
        var i = 500
        if (fightManager != null && fightManager?.hasPreviouslyKilledDragon() != true)
        {
            i = 12000
        }
        if (!world.isRemote)
        {
            if (deathTicks > 150 && deathTicks % 5 == 0 && flag)
            {
                this.dropExperience(MathHelper.floor(i.toFloat() * 0.08f))
            }
            if (deathTicks == 1)
            {
                world.playBroadcastSound(1028, BlockPos(this), 0)
            }
        }
        move(MoverType.SELF, Vec3d(0.0, 0.1f.toDouble(), 0.0))
        rotationYaw += 20.0f
        renderYawOffset = rotationYaw
        if (deathTicks == 200 && !world.isRemote)
        {
            if (flag)
            {
                this.dropExperience(MathHelper.floor(i.toFloat() * 0.2f))
            }
            if (fightManager != null)
            {
                fightManager?.processDragonDeath(this)
            }
            this.remove()
        }
    }

    private fun dropExperience(p_184668_1_: Int)
    {
        var p_184668_1_ = p_184668_1_
        while (p_184668_1_ > 0)
        {
            val i = ExperienceOrbEntity.getXPSplit(p_184668_1_)
            p_184668_1_ -= i
            world.addEntity(ExperienceOrbEntity(world, posX, posY, posZ, i))
        }
    }

    /**
     * Generates values for the fields pathPoints, and neighbors, and then returns the nearest pathPoint to the specified
     * position.
     */
    fun findClosestNode(): Int
    {
        if (pathPoints[0] == null)
        {
            for (i in 0..23)
            {
                var j = 5
                var l: Int
                var i1: Int
                when
                {
                    i < 12 ->
                    {
                        l =
                            MathHelper.floor(60.0f * MathHelper.cos(2.0f * ((-Math.PI).toFloat() + 0.2617994f * i.toFloat())))
                        i1 =
                            MathHelper.floor(60.0f * MathHelper.sin(2.0f * ((-Math.PI).toFloat() + 0.2617994f * i.toFloat())))
                    }
                    i < 20 ->
                    {
                        val lvt_3_1_ = i - 12
                        l =
                            MathHelper.floor(40.0f * MathHelper.cos(2.0f * ((-Math.PI).toFloat() + Math.PI.toFloat() / 8f * lvt_3_1_.toFloat())))
                        i1 =
                            MathHelper.floor(40.0f * MathHelper.sin(2.0f * ((-Math.PI).toFloat() + Math.PI.toFloat() / 8f * lvt_3_1_.toFloat())))
                        j += 10
                    }
                    else   ->
                    {
                        val k1 = i - 20
                        l =
                            MathHelper.floor(20.0f * MathHelper.cos(2.0f * ((-Math.PI).toFloat() + Math.PI.toFloat() / 4f * k1.toFloat())))
                        i1 =
                            MathHelper.floor(20.0f * MathHelper.sin(2.0f * ((-Math.PI).toFloat() + Math.PI.toFloat() / 4f * k1.toFloat())))
                    }
                }
                val j1 = max(world.seaLevel + 10, world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, BlockPos(l, 0, i1)).y + j)
                pathPoints[i] = PathPoint(l, j1, i1)
            }
            neighbors[0] = 6146
            neighbors[1] = 8197
            neighbors[2] = 8202
            neighbors[3] = 16404
            neighbors[4] = 32808
            neighbors[5] = 32848
            neighbors[6] = 65696
            neighbors[7] = 131392
            neighbors[8] = 131712
            neighbors[9] = 263424
            neighbors[10] = 526848
            neighbors[11] = 525313
            neighbors[12] = 1581057
            neighbors[13] = 3166214
            neighbors[14] = 2138120
            neighbors[15] = 6373424
            neighbors[16] = 4358208
            neighbors[17] = 12910976
            neighbors[18] = 9044480
            neighbors[19] = 9706496
            neighbors[20] = 15216640
            neighbors[21] = 13688832
            neighbors[22] = 11763712
            neighbors[23] = 8257536
        }
        return getNearestPpIdx(posX, posY, posZ)
    }

    /**
     * Returns the index into pathPoints of the nearest PathPoint.
     */
    fun getNearestPpIdx(x: Double, y: Double, z: Double): Int
    {
        var f = 10000.0f
        var i = 0
        val pathpoint = PathPoint(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z))
        var j = 0
        if (fightManager == null || fightManager?.numAliveCrystals == 0)
        {
            j = 12
        }
        for (k in j..23)
        {
            if (pathPoints[k] != null)
            {
                val f1 = pathPoints[k]!!.distanceToSquared(pathpoint)
                if (f1 < f)
                {
                    f = f1
                    i = k
                }
            }
        }
        return i
    }

    /**
     * Find and return a path among the circles described by pathPoints, or null if the shortest path would just be
     * directly between the start and finish with no intermediate points.
     *
     * Starting with pathPoint[startIdx], it searches the neighboring points (and their neighboring points, and so on)
     * until it reaches pathPoint[finishIdx], at which point it calls makePath to seal the deal.
     */
    fun findPath(startIdx: Int, finishIdx: Int, andThen: PathPoint?): Path?
    {
        for (i in 0..23)
        {
            val pint = pathPoints[i]
            pint!!.visited = false
            pint.distanceToTarget = 0.0f
            pint.totalPathDistance = 0.0f
            pint.distanceToNext = 0.0f
            pint.previous = null
            pint.index = -1
        }
        val start = pathPoints[startIdx]!!
        var end = pathPoints[finishIdx]!!
        start.totalPathDistance = 0.0f
        start.distanceToNext = start.distanceTo(end)
        start.distanceToTarget = start.distanceToNext
        pathFindQueue.clearPath()
        pathFindQueue.addPoint(start)
        var pathpoint1 = start
        var j = 0
        if (fightManager == null || fightManager?.numAliveCrystals == 0)
        {
            j = 12
        }
        while (!pathFindQueue.isPathEmpty)
        {
            val pathpoint2 = pathFindQueue.dequeue()
            if (pathpoint2 == end)
            {
                if (andThen != null)
                {
                    andThen.previous = end
                    end = andThen
                }
                return makePath(start, end)
            }
            if (pathpoint2.distanceTo(end) < pathpoint1.distanceTo(end))
            {
                pathpoint1 = pathpoint2
            }
            pathpoint2.visited = true
            var k = 0
            for (l in 0..23)
            {
                if (pathPoints[l] === pathpoint2)
                {
                    k = l
                    break
                }
            }
            for (i1 in j..23)
            {
                if (neighbors[k] and 1 shl i1 > 0)
                {
                    val pathpoint3 = pathPoints[i1]
                    if (!pathpoint3!!.visited)
                    {
                        val f = pathpoint2.totalPathDistance + pathpoint2.distanceTo(pathpoint3)
                        if (!pathpoint3.isAssigned || f < pathpoint3.totalPathDistance)
                        {
                            pathpoint3.previous = pathpoint2
                            pathpoint3.totalPathDistance = f
                            pathpoint3.distanceToNext = pathpoint3.distanceTo(end)
                            if (pathpoint3.isAssigned)
                            {
                                pathFindQueue.changeDistance(
                                    pathpoint3,
                                    pathpoint3.totalPathDistance + pathpoint3.distanceToNext
                                )
                            } else
                            {
                                pathpoint3.distanceToTarget = pathpoint3.totalPathDistance + pathpoint3.distanceToNext
                                pathFindQueue.addPoint(pathpoint3)
                            }
                        }
                    }
                }
            }
        }
        return if (pathpoint1 === start)
        {
            null
        } else
        {
            LOGGER.debug("Failed to find path from {} to {}", startIdx, finishIdx)
            if (andThen != null)
            {
                andThen.previous = pathpoint1
                pathpoint1 = andThen
            }
            makePath(start, pathpoint1)
        }
    }

    /**
     * Create and return a new PathEntity defining a path from the start to the finish, using the connections already
     * made by the caller, findPath.
     */
    private fun makePath(start: PathPoint?, finish: PathPoint?): Path
    {
        val list: ArrayList<PathPoint?> = Lists.newArrayList()
        var pathpoint = finish
        list.add(0, finish)
        while (pathpoint!!.previous != null)
        {
            pathpoint = pathpoint.previous
            list.add(0, pathpoint)
        }
        return Path(list, BlockPos(finish!!.x, finish.y, finish.z), true)
    }

    override fun writeAdditional(compound: CompoundNBT)
    {
        super.writeAdditional(compound)
        compound.putInt("DragonPhase", phaseManager.currentPhase!!.type.getId())
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    override fun readAdditional(compound: CompoundNBT)
    {
        super.readAdditional(compound)
        if (compound.contains("DragonPhase"))
        {
            phaseManager.setPhase(PhaseType.getById(compound.getInt("DragonPhase")))
        }
    }

    /**
     * Makes the entity despawn if requirements are reached
     */
    override fun checkDespawn()
    {
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    override fun canBeCollidedWith(): Boolean = false

    override fun getSoundCategory(): SoundCategory = SoundCategory.HOSTILE

    override fun getAmbientSound(): SoundEvent? = SoundEvents.ENTITY_ENDER_DRAGON_AMBIENT

    override fun getHurtSound(damageSourceIn: DamageSource): SoundEvent? = SoundEvents.ENTITY_ENDER_DRAGON_HURT

    /**
     * Returns the volume for the sounds this mob makes.
     */
    override fun getSoundVolume(): Float = 5.0f

    @OnlyIn(Dist.CLIENT)
    fun getHeadPartYOffset(p_184667_1_: Int, p_184667_2_: DoubleArray, p_184667_3_: DoubleArray): Float
    {
        val iphase = phaseManager.currentPhase
        val phasetype = iphase!!.type
        val d0: Double
        d0 = if (phasetype !== LANDING && phasetype !== TAKEOFF)
        {
            when
            {
                iphase.isStationary -> p_184667_1_.toDouble()
                p_184667_1_ == 6    -> 0.0
                else                -> p_184667_3_[1] - p_184667_2_[1]
            }
        } else
        {
            val blockpos =
                world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.END_PODIUM_LOCATION)
            val f = Math.max(MathHelper.sqrt(blockpos.distanceSq(this.positionVec, true)) / 4.0f, 1.0f)
            (p_184667_1_.toFloat() / f).toDouble()
        }
        return d0.toFloat()
    }

    fun getHeadLookVec(p_184665_1_: Float): Vec3d
    {
        val iphase = phaseManager.currentPhase
        val phasetype = iphase!!.type
        val vec3d: Vec3d
        if (phasetype !== LANDING && phasetype !== TAKEOFF)
        {
            if (iphase.isStationary)
            {
                val f4 = rotationPitch
                val f5 = 1.5f
                rotationPitch = -45.0f
                vec3d = getLook(p_184665_1_)
                rotationPitch = f4
            } else
            {
                vec3d = getLook(p_184665_1_)
            }
        } else
        {
            val blockpos =
                world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.END_PODIUM_LOCATION)
            val f = Math.max(MathHelper.sqrt(blockpos.distanceSq(this.positionVec, true)) / 4.0f, 1.0f)
            val f1 = 6.0f / f
            val f2 = rotationPitch
            val f3 = 1.5f
            rotationPitch = -f1 * 1.5f * 5.0f
            vec3d = getLook(p_184665_1_)
            rotationPitch = f2
        }
        return vec3d
    }

    fun onCrystalDestroyed(crystal: EnderCrystalEntity, pos: BlockPos, dmgSrc: DamageSource)
    {
        val playerentity: PlayerEntity? = if (dmgSrc.trueSource is PlayerEntity)
        {
            dmgSrc.trueSource as? PlayerEntity?
        } else
        {
            world.getClosestPlayer(
                CRYSTAL_DESTROY_TARGETING,
                pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble()
            )
        }
        if (crystal === closestEnderCrystal)
        {
            hurt(dragonPartHead, DamageSource.causeExplosionDamage(playerentity), 10.0f)
        }
        phaseManager.currentPhase?.onCrystalDestroyed(crystal, pos, dmgSrc, playerentity)
    }

    override fun notifyDataManagerChange(key: DataParameter<*>)
    {
        if (PHASE == key && world.isRemote)
        {
            phaseManager.setPhase(PhaseType.getById(getDataManager().get(PHASE)))
        }
        super.notifyDataManagerChange(key)
    }

    override fun addPotionEffect(effectInstanceIn: EffectInstance): Boolean = false

    override fun canBeRidden(entityIn: Entity): Boolean = false

    /**
     * Returns false if this Entity is a boss, true otherwise.
     */
    override fun isNonBoss(): Boolean = false

    companion object
    {
        private val LOGGER = LogManager.getLogger()
        val PHASE = EntityDataManager.createKey(AbstractEnderDragonEntity::class.java, DataSerializers.VARINT)
        private val CRYSTAL_DESTROY_TARGETING = EntityPredicate().setDistance(64.0)
    }

    init
    {
        dragonParts = arrayOf(
            dragonPartHead,
            dragonPartNeck,
            dragonPartBody, dragonPartTail1, dragonPartTail2, dragonPartTail3, dragonPartRightWing, dragonPartLeftWing
        )
        this.health = this.maxHealth
        noClip = true
        ignoreFrustumCheck = true
        fightManager = if (!worldIn.isRemote && worldIn.dimension is EndDimension)
        {
            (worldIn.dimension as EndDimension).dragonFightManager
        } else
        {
            null
        }
        phaseManager = AbstractPhaseManager(this)
    }
}
