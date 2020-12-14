package phoenix.utils.capablity

import net.minecraft.entity.Entity
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityProvider
import net.minecraftforge.common.util.LazyOptional
import phoenix.Phoenix

class CapabilityProvider : CapabilityProvider<Entity>(Entity::class.java)
{
    override fun <T : Any?> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T>
    {
        return super.getCapability(Phoenix.CHAPTER_CAPA, side).cast()
    }
}