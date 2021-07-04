package phoenix.world.structures.bunker

import phoenix.world.structures.IPieceProperties

data class BunkerProperties(val deep : Int, val waterLevel : Int, val brokened : Boolean) : IPieceProperties
{
    constructor(of : BunkerProperties, deep : Int = of.deep, waterLevel : Int = of.waterLevel, brokened : Boolean = of.brokened) : this(deep, waterLevel, brokened)
}