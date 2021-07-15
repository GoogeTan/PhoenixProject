package phoenix.api.block.cable


/**
 * Capability for cables that can become unreal.
 * A cable can only become fake for a full blockOf, not just for one side.
 * This means that for example parts can exist in that blockOf space without the cable being there.
 * @author rubensworks
 */
interface ICableFakeable
{
    /**
     * @return If this cable is a real cable, otherwise it is just a holder blockOf for parts without connections.
     */
    /**
     * @param real If this cable is a real cable, otherwise it is just a holder blockOf for parts without connections.
     */
    var isRealCable: Boolean
}
