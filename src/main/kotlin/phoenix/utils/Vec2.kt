package phoenix.utils

import kotlin.math.sqrt

class Vec2(var x: Double, var y: Double)
{
    fun toUnit()
    {
        this /= this()
    }


    operator fun plus(o : Vec2) : Vec2 = Vec2(x + o.x, y + o.y)

    operator fun minus(o : Vec2) : Vec2 = Vec2(x - o.x, y - o.y)

    operator fun times(o : Double) = Vec2(x * o, y * o)
    /*
    operator fun timesAssign(o : Double)
    {
        x *= o
        y *= o
    }
    */

    operator fun div(o : Double) = times(1 / o)
    operator fun divAssign(o : Double)
    {
        x /= o
        y /= o
    }

    operator fun invoke() = sqrt(x * x + y * y)
}
