package phoenix.api.block

/**
 * Capability that can have its light level updated and stored.
 * @author rubensworks
 */
interface IDynamicLight
{
    /**
     * Get the light level.
     * @return The light level.
     */
    /**
     * Set the light level.
     * @param level The light level.
     */
    var lightLevel: Int
}
