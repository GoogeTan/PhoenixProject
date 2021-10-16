package me.zahara.phoenix.world

object StageManager
{
    var stage : Int = 0
        set(valueIn)
        {
            val value = valueIn % Stage.values().size
            enumStage = Stage.values()[value]
            field = stage
        }
    var points : Int = 0
        set(value)
        {
            if (value >= Stage.values().size)
                ++stage
            field = value % Stage.values().size
        }

    var enumStage : Stage = Stage.DEATH
        private set

    enum class Stage(val hasSkyLight : Boolean = false, val ambientLight : Float = 0f)
    {
        DEATH(
        ),
        SLEEP(
            ambientLight = 0.1f
        ),
        REBIRTH(
            hasSkyLight = true,
            ambientLight = 0.1f
        ),
        SKY(
            hasSkyLight = true,
            ambientLight = 0.1f
        ),
        HELL(
        );
    }
}