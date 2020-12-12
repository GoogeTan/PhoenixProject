package phoenix.enity.tasks.dragon

import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.boss.dragon.EnderDragonEntity
import phoenix.world.EndDimension

class AttackPlayerGoal(var dragon : EnderDragonEntity) : Goal()
{

    override fun shouldExecute(): Boolean
    {
        return if(dragon.world.dimension is EndDimension)
            (dragon.world.dimension as EndDimension).dragonFightManager.bossInfo.players.isNotEmpty()
        else
            false
    }
}