package phoenix.enity.boss

import net.minecraft.entity.Entity
import net.minecraft.entity.EntitySize
import net.minecraft.entity.Pose
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.IPacket
import net.minecraft.util.DamageSource

class AbstractDragonPartEntity(val dragon: AbstractEnderDragonEntity, val name: String, width: Float, height: Float) : Entity(dragon.type, dragon.world)
{
    private val size: EntitySize = EntitySize.flexible(width, height)
    override fun registerData()
    {
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    override fun readAdditional(compound: CompoundNBT)
    {
    }

    override fun writeAdditional(compound: CompoundNBT)
    {
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    override fun canBeCollidedWith(): Boolean = true

    /**
     * Called when the entity is attacked.
     */
    override fun attackEntityFrom(source: DamageSource, amount: Float): Boolean
    {
        return if (isInvulnerableTo(source)) false else dragon.func_213403_a(this, source, amount)
    }

    /**
     * Returns true if Entity argument is equal to this Entity
     */
    override fun isEntityEqual(entityIn: Entity): Boolean = this === entityIn || dragon === entityIn

    override fun createSpawnPacket(): IPacket<*>
    {
        throw UnsupportedOperationException()
    }

    override fun getSize(poseIn: Pose): EntitySize= size

    init
    {
        recalculateSize()
    }
}
