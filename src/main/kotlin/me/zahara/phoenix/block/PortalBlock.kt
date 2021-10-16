package me.zahara.phoenix.block

import me.zahara.phoenix.init.PhxDimensions
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.util.ITeleporter
import org.apache.logging.log4j.LogManager

class PortalBlock(props: Properties) : PhoenixBlock(props)
{
    override fun stepOn(level: Level, pPos: BlockPos, pState: BlockState, pEntity: Entity)
    {
        super.stepOn(level, pPos, pState, pEntity)
        if (!level.isClientSide)
        {
            if (level.dimension() != PhxDimensions.decayLevel)
            {
                val server = level.server
                if (server == null)
                {
                    LogManager.getLogger().error("Server is null")
                    return
                }
                val targetLevel = server.getLevel(PhxDimensions.decayLevel)
                if (targetLevel == null)
                {
                    LogManager.getLogger().error("TargetLevel is null. ${server.levelKeys()}")
                    return
                }
                pEntity.changeDimension(targetLevel, object : ITeleporter {})
            }
            else
            {
                pEntity.changeDimension(level.server!!.getLevel(Level.END)!!)
            }
        }
    }
}