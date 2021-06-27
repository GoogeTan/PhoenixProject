package phoenix.api.block.cable


/**
 * Capability for cables that can become unreal.
 * A cable can only become fake for a full block, not just for one side.
 * This means that for example parts can exist in that block space without the cable being there.
 * @author rubensworks
 */
interface ICableFakeable
{
    /**
     * @return If this cable is a real cable, otherwise it is just a holder block for parts without connections.
     */
    /**
     * @param real If this cable is a real cable, otherwise it is just a holder block for parts without connections.
     */
    var isRealCable: Boolean
}
