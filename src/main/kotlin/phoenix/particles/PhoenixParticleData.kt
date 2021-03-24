package phoenix.particles

import com.mojang.brigadier.StringReader
import net.minecraft.network.PacketBuffer
import net.minecraft.particles.IParticleData
import net.minecraft.particles.IParticleData.IDeserializer
import net.minecraft.particles.ParticleType
import net.minecraft.util.math.MathHelper
import phoenix.init.PhoenixParticles
import java.awt.Color
import java.util.*
import javax.annotation.Nonnull

private fun constrainDiameterToValidRange(diameter: Double): Double
{
    val MIN_DIAMETER = 0.05
    val MAX_DIAMETER = 1.0
    return MathHelper.clamp(diameter, MIN_DIAMETER, MAX_DIAMETER)
}

class PhoenixParticleData(tint: Color, diameter: Double, lifeTime: Int) : IParticleData
{
    companion object
    {
        public val DESERIALIZER: IDeserializer<PhoenixParticleData> = Des()
    }
    private var tint: Color? = null
    private var diameter = 0.0
    private var lifeTime = 0

    init
    {
        this.tint = tint
        this.lifeTime = lifeTime
        this.diameter = constrainDiameterToValidRange(diameter)
    }

    fun getTint() = tint

    fun getLifeTime() = lifeTime

    fun getDiameter() = diameter

    @Nonnull
    override fun getType(): ParticleType<*> = PhoenixParticles.PHOENIX_BORN.get()

    override fun write(buf: PacketBuffer)
    {
        buf.writeInt(tint!!.red)
        buf.writeInt(tint!!.green)
        buf.writeInt(tint!!.blue)
        buf.writeInt(lifeTime)
        buf.writeDouble(diameter)
    }

    @Nonnull
    override fun getParameters(): String?
    {
        return java.lang.String.format(
            Locale.ROOT, "%s %s %.2f %i %i %i",
            this.type.registryName, diameter, lifeTime, tint?.red, tint?.green, tint?.blue
        )
    }

}
class Des : IDeserializer<PhoenixParticleData>
{
    override fun read(particleTypeIn: ParticleType<PhoenixParticleData>, buf: PacketBuffer): PhoenixParticleData
    {
        val MIN_COLOUR = 0
        val MAX_COLOUR = 255
        val red = MathHelper.clamp(buf.readInt(), MIN_COLOUR, MAX_COLOUR)
        val green = MathHelper.clamp(buf.readInt(), MIN_COLOUR, MAX_COLOUR)
        val blue = MathHelper.clamp(buf.readInt(), MIN_COLOUR, MAX_COLOUR)
        val color = Color(red, green, blue)
        val diameter = constrainDiameterToValidRange(buf.readDouble())
        val lifeTime = buf.readInt()
        return PhoenixParticleData(color, diameter, lifeTime)
    }

    override fun deserialize(particleTypeIn: ParticleType<PhoenixParticleData?>, reader: StringReader): PhoenixParticleData
    {
        reader.expect(' ')
        val diameter = constrainDiameterToValidRange(reader.readDouble())
        reader.expect(' ')
        val lifeTime: Int = reader.readInt()
        val MIN_COLOUR = 0
        val MAX_COLOUR = 255
        reader.expect(' ')
        val red = MathHelper.clamp(reader.readInt(), MIN_COLOUR, MAX_COLOUR)
        reader.expect(' ')
        val green = MathHelper.clamp(reader.readInt(), MIN_COLOUR, MAX_COLOUR)
        reader.expect(' ')
        val blue = MathHelper.clamp(reader.readInt(), MIN_COLOUR, MAX_COLOUR)
        val color = Color(red, green, blue)
        return PhoenixParticleData(color, diameter, lifeTime)
    }
}
