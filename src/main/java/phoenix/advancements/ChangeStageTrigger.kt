package phoenix.advancements

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.minecraft.advancements.criterion.AbstractCriterionTrigger
import net.minecraft.advancements.criterion.CriterionInstance
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.ResourceLocation
import phoenix.Phoenix
import phoenix.utils.getInt

class ChangeStageTrigger : AbstractCriterionTrigger<ChangeStageTrigger.Instance>()
{
    override fun getId() = ResourceLocation(Phoenix.MOD_ID, "change_stage")

    override fun deserializeInstance(json: JsonObject, context: JsonDeserializationContext): Instance
    {
        val stageOld : Int = json.getInt("sold")
        val stageNew : Int = json.getInt("snew")
        return Instance(stageOld, stageNew)
    }

    fun test(player: ServerPlayerEntity, stageOld : Int, stageNew : Int)
    {
        func_227070_a_(player.advancements) { inst: Instance -> inst.test(stageOld, stageNew) }
    }

    class Instance(private val stageOld : Int, private val  stageNew : Int) : CriterionInstance(ResourceLocation(Phoenix.MOD_ID, "change_stage_trigger"))
    {
        fun test(stageOld : Int, stageNew : Int) = stageOld == this.stageOld && stageNew == this.stageNew

        override fun serialize(): JsonElement
        {
            val json = JsonObject()
            json.addProperty("sold", stageOld)
            json.addProperty("snew", stageNew)
            return json
        }
    }
}