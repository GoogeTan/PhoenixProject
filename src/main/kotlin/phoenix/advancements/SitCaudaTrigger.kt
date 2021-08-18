package phoenix.advancements

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.minecraft.advancements.criterion.AbstractCriterionTrigger
import net.minecraft.advancements.criterion.CriterionInstance
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.ResourceLocation
import phoenix.MOD_ID
import phoenix.Phoenix

object SitCaudaTrigger  : AbstractCriterionTrigger<SitCaudaTrigger.Instance>()
{
    private val id = ResourceLocation(MOD_ID, "sit_cauda")

    override fun getId() = id

    override fun deserializeInstance(json: JsonObject, context: JsonDeserializationContext): Instance = Instance()

    fun test(player: ServerPlayerEntity)
    {
        func_227070_a_(player.advancements) { inst: Instance -> inst.test() }
    }

    class Instance : CriterionInstance(ResourceLocation(MOD_ID, "sit_cauda"))
    {
        fun test() : Boolean = true

        override fun serialize(): JsonElement = JsonObject()

        override fun toString(): String = "Instance()"
    }
}
