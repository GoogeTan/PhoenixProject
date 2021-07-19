package phoenix.enity

import net.minecraft.block.Blocks
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.NBTUtil
import net.minecraft.network.datasync.DataParameter
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.util.DamageSource
import net.minecraft.util.math.BlockPos
import net.minecraft.world.Explosion
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.fml.network.NetworkHooks
import phoenix.enity.boss.AbstractEnderDragonEntity
import phoenix.init.PhxEntities
import phoenix.utils.get
import phoenix.world.EndDimension
import java.util.*

class EnderCrystalEntity(type: EntityType<out EnderCrystalEntity>, world: World) : Entity(type, world)
{
    var innerRotation = 0

    constructor(world: World, x: Double, y: Double, z: Double) : this(PhxEntities.enderCrystal, world)
    {
        setPosition(x, y, z)
    }

    init
    {
        preventEntitySpawning = true
        this.innerRotation = rand.nextInt(100000)
    }

    override fun canTriggerWalking() = false

    override fun registerData()
    {
        getDataManager().register(BEAM_TARGET, Optional.empty())
        getDataManager().register(SHOW_BOTTOM, true)
    }


    override fun tick()
    {
        ++innerRotation
        if (!world.isRemote)
        {
            val blockpos = BlockPos(this)
            if (world.dimension is EndDimension && world[blockpos].isAir)
            {
                world.setBlockState(blockpos, Blocks.FIRE.defaultState)
            }
        }
    }

    fun onCrystalDestroyed(source: DamageSource)
    {
        if (world.dimension is EndDimension)
        {
            (world.dimension as EndDimension).dragonFightManager?.onCrystalDestroyed(this, source)
        }
    }

    override fun writeAdditional(p_213281_1_: CompoundNBT)
    {
        if (this.getBeamTarget() != null)
        {
            p_213281_1_.put("BeamTarget", NBTUtil.writeBlockPos(this.getBeamTarget()!!))
        }
        p_213281_1_.putBoolean("ShowBottom", shouldShowBottom())
    }

    override fun readAdditional(p_70037_1_: CompoundNBT)
    {
        if (p_70037_1_.contains("BeamTarget", 10))
        {
            setBeamTarget(NBTUtil.readBlockPos(p_70037_1_.getCompound("BeamTarget")))
        }
        if (p_70037_1_.contains("ShowBottom", 1))
        {
            setShowBottom(p_70037_1_.getBoolean("ShowBottom"))
        }
    }

    override fun canBeCollidedWith(): Boolean
    {
        return true
    }

    override fun attackEntityFrom(source: DamageSource, p_70097_2_: Float): Boolean
    {
        return when
        {
            isInvulnerableTo(source)                       ->
            {
                false
            }
            source.trueSource is AbstractEnderDragonEntity ->
            {
                false
            }
            else                                               ->
            {
                if (!removed && !world.isRemote)
                {
                    this.remove()
                    if (!source.isExplosion)
                    {
                        world.createExplosion(
                            null as Entity?,
                            posX, posY, posZ, 6.0f, Explosion.Mode.DESTROY
                        )
                    }
                    this.onCrystalDestroyed(source)
                }
                true
            }
        }
    }

    override fun onKillCommand()
    {
        this.onCrystalDestroyed(DamageSource.GENERIC)
        super.onKillCommand()
    }

    fun setBeamTarget(value : BlockPos?)
    {
        getDataManager().set(BEAM_TARGET, Optional.ofNullable(value))
    }

    fun getBeamTarget(): BlockPos? = (getDataManager().get(BEAM_TARGET) as Optional<*>).orElse(null) as? BlockPos

    fun setShowBottom(value : Boolean) = getDataManager().set(SHOW_BOTTOM, value)

    fun shouldShowBottom(): Boolean
    {
        return getDataManager().get(SHOW_BOTTOM) as Boolean
    }

    @OnlyIn(Dist.CLIENT)
    override fun isInRangeToRenderDist(range: Double): Boolean = super.isInRangeToRenderDist(range) || this.getBeamTarget() != null

    override fun createSpawnPacket() = NetworkHooks.getEntitySpawningPacket(this)!!

    companion object
    {
        var BEAM_TARGET: DataParameter<Optional<BlockPos>> = EntityDataManager.createKey(EnderCrystalEntity::class.java, DataSerializers.OPTIONAL_BLOCK_POS)
        var SHOW_BOTTOM: DataParameter<Boolean> = EntityDataManager.createKey(EnderCrystalEntity::class.java, DataSerializers.BOOLEAN)
    }
}
