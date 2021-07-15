package phoenix.api.block

import net.minecraft.block.BlockState


/**
 * Capability for targets that can hold facades.
 * @author rubensworks
 */
interface IFacadeable
{
    /**
     * @return If this data has a facade.
     */
    fun hasFacade(): Boolean
    /**
     * @return The blockstate of the facade.
     */
    /**
     * Set the new facade
     * @param blockState The new facade or null.
     */
    var facade: BlockState?
}
