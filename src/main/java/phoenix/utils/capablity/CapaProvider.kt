package phoenix.utils.capablity

import net.minecraft.entity.Entity
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityProvider
import net.minecraftforge.common.util.LazyOptional
import phoenix.Phoenix
import phoenix.utils.LogManager

class CapabilityProvider : CapabilityProvider<Entity>(Entity::class.java)
{
    override fun <T : Any> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T>
    {
        LogManager.log(this, "${capabilities != null} ${cap.defaultInstance is IChapterReader}")
        return if(capabilities != null && cap.defaultInstance is IChapterReader)
        {
            this.capabilities!!.getCapability(Phoenix.CHAPTER_CAPA) as LazyOptional<T>
        }
        else
        {
            super.getCapability(cap, side)
        }
    }
}