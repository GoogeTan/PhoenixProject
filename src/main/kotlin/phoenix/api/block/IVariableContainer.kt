package phoenix.api.block

//import org.cyclops.integrateddynamics.api.item.IVariableFacade
//import org.cyclops.integrateddynamics.api.network.INetwork


/**
 * Capability that can hold [IVariableFacade]s.
 * @author rubensworks
 */
interface IVariableContainer
{
    /**
     * @return The stored variable facades for this part.
     */
    val variableCache: Map<Int?, Any?>?

    /**
     * Invalidate variables in this cache, clear the cache and re-populate from the supplied inventory
     * @param network [INetwork] that the variables are in
     * @param inventory IInventory to re-populate the cache from
     * @param sendVariablesUpdateEvent if true post a VariableContentsUpdatedEvent to the network when done
     */
    //fun refreshVariables(network: INetwork?, inventory: IInventory?, sendVariablesUpdateEvent: Boolean)
}
