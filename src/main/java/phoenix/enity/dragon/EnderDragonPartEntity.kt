package phoenix.enity.dragon

import net.minecraft.entity.Entity
import net.minecraft.entity.EntitySize
import net.minecraft.entity.Pose
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.IPacket
import net.minecraft.util.DamageSource

class EnderDragonPartEntity(dragon: EnderDragonEntity, p_i50232_2_: String, p_i50232_3_: Float, p_i50232_4_: Float) :
    Entity(dragon.type, dragon.world)
{
    val dragon: EnderDragonEntity
    val field_213853_c: String
    private val field_213854_d: EntitySize
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
    override fun canBeCollidedWith(): Boolean
    {
        return true
    }

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
    override fun isEntityEqual(entityIn: Entity): Boolean
    {
        return this === entityIn || dragon === entityIn
    }

    override fun createSpawnPacket(): IPacket<*>
    {
        throw UnsupportedOperationException()
    }

    override fun getSize(poseIn: Pose): EntitySize
    {
        return field_213854_d
    }

    init
    {
        field_213854_d = EntitySize.flexible(p_i50232_3_, p_i50232_4_)
        recalculateSize()
        this.dragon = dragon
        field_213853_c = p_i50232_2_
    }
}
