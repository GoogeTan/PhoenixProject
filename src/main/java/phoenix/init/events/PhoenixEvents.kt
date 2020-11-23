package phoenix.init.events

import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.particles.ParticleType
import net.minecraft.particles.ParticleTypes
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.dimension.DimensionType
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import phoenix.init.PhoenixParticles
import phoenix.particles.PhoenixParticleData
import phoenix.world.StageManager

@Mod.EventBusSubscriber
object PhoenixEvents
{
    @JvmStatic
    @SubscribeEvent
    fun onPlay(event: PlayerEvent.Clone)
    {
        val world = event.player.world
        val player = event.player
        if (!world.isRemote)
        {
            val i = MathHelper.floor(player.getPosX())
            val j = MathHelper.floor(player.getPosY() - 0.2f.toDouble())
            val k = MathHelper.floor(player.getPosZ())
            val blockpos = BlockPos(i, j, k)
            val blockstate: BlockState = world.getBlockState(blockpos)
            if (!blockstate.addRunningEffects(world, blockpos, player)) if (blockstate.renderType != BlockRenderType.INVISIBLE)
            {
                val vec3d: Vec3d = player.motion
                //world.addParticle(PhoenixParticleData(ParticleTypes.EFFECT), player.getPosX() + (world.rand.nextFloat().toDouble() - 0.5) * player.getSize(player.pose).width.toDouble(), player.getPosY() + 0.1, player.getPosZ() + (world.rand.nextFloat() as Double - 0.5) * player.getSize(player.pose).width as Double, vec3d.x * -4.0, 1.5, vec3d.z * -4.0)
            }
        }
    }


    @JvmStatic
    @SubscribeEvent
    fun onSave(event: WorldEvent.Save)
    {
        val nbt = event.world.worldInfo.getDimensionData(DimensionType.THE_END)
        StageManager.write(nbt)
        event.world.worldInfo.setDimensionData(DimensionType.THE_END, nbt)
    }

    @JvmStatic
    @SubscribeEvent
    fun onLoad(event: WorldEvent.Load)
    {
        val nbt = event.world.worldInfo.getDimensionData(DimensionType.THE_END)
        StageManager.read(nbt)
    }
}