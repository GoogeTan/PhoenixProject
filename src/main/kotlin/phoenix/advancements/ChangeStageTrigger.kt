package phoenix.advancements

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.minecraft.advancements.criterion.AbstractCriterionTrigger
import net.minecraft.advancements.criterion.CriterionInstance
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.ResourceLocation
import phoenix.Phoenix
import phoenix.other.addProp
import phoenix.other.getInt

object ChangeStageTrigger : AbstractCriterionTrigger<ChangeStageTrigger.Instance>()
{
    private val id = ResourceLocation(Phoenix.MOD_ID, "achieve_stage")

    override fun getId() = id

    override fun deserializeInstance(json: JsonObject, context: JsonDeserializationContext): Instance = Instance(json.getInt("stage"))

    fun test(player: ServerPlayerEntity, stage : Int)
    {
        func_227070_a_(player.advancements) { inst: Instance ->
            inst.test(stage)
        }
    }

    class Instance(private val stage : Int) : CriterionInstance(ResourceLocation(Phoenix.MOD_ID, "achieve_stage"))
    {
        fun test(stageIn : Int) : Boolean = stageIn >= this.stage

        override fun serialize(): JsonElement = JsonObject().addProp("stage", stage)

        override fun toString(): String = "Instance(stage=$stage)"
    }
}
