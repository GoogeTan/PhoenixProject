package phoenix.enity.boss

import net.minecraft.entity.EntityType
import net.minecraft.entity.boss.dragon.EnderDragonEntity
import net.minecraft.world.World
import phoenix.init.PhoenixEntities

class DragonRedoStageEntity(type : EntityType<out DragonRedoStageEntity>, world: World) : EnderDragonEntity(type, world)
{
    constructor(world: World) : this(PhoenixEntities.DRAGON_REDO_STAGE.get(), world)

    init
    {
        
    }
}