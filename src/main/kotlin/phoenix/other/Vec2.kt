package phoenix.other

import kotlin.math.sqrt

class Vec2(var x: Double, var y: Double)
{
    constructor(x : Number, y : Number) : this(x.toDouble(), y.toDouble())
    fun toUnit() : Vec2
    {
        this /= this()
        return this
    }


    operator fun plus(o : Vec2) : Vec2 = Vec2(x + o.x, y + o.y)

    operator fun minus(o : Vec2) : Vec2 = Vec2(x - o.x, y - o.y)

    operator fun times(o : Number) = Vec2(x * o.toDouble(), y * o.toDouble())

    operator fun div(o : Number) = times(1 / o.toDouble())
    
    operator fun divAssign(o : Double)
    {
        x /= o
        y /= o
    }

    operator fun invoke() = sqrt(x * x + y * y)

    override fun toString(): String = "Vec[x:$x, y:$y]"

    override fun equals(other: Any?): Boolean
    {
        if (this === other)
            return true
        if (other !is Vec2)
            return false
        return x == other.x && y == other.y
    }

    override fun hashCode(): Int
    {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }
}
