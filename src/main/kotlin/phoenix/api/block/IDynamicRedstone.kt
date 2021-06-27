package phoenix.api.block

/**
 * Capability that can have its redstone level updated and stored.
 * @author rubensworks
 */
interface IDynamicRedstone
{
    /**
     * Set the redstone level.
     * @param level The redstone level.
     * @param strongPower If the redstone power should be strong.
     */
    fun setRedstoneLevel(level: Int, strongPower: Boolean)

    /**
     * Get the redstone level.
     * @return The redstone level.
     */
    val redstoneLevel: Int

    /**
     * @return If the redstone power is strong.
     */
    val isStrong: Boolean
    /**
     * If this side allows redstone to be inputted.
     * @return If it allows input.
     */
    /**
     * Set if this side allows redstone to be inputted.
     * @param allow If it allows input.
     */
    var isAllowRedstoneInput: Boolean
    /**
     * @return The last pulse value.
     */
    /**
     * Store the last value that was used to trigger a redstone pulse.
     * @param value A pulse value.
     */
    var lastPulseValue: Int
}
