package phoenix.init.events

import com.google.common.collect.ImmutableList
import net.minecraft.entity.Entity
import net.minecraft.entity.merchant.villager.VillagerProfession
import net.minecraft.entity.merchant.villager.VillagerTrades
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.item.MerchantOffer
import net.minecraft.particles.ParticleTypes
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.dimension.DimensionType
import net.minecraft.world.gen.feature.template.PlacementSettings
import net.minecraft.world.server.ServerWorld
import net.minecraft.world.storage.loot.ItemLootEntry
import net.minecraft.world.storage.loot.LootPool
import net.minecraft.world.storage.loot.LootTables
import net.minecraftforge.event.LootTableLoadEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.TickEvent.WorldTickEvent
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.event.village.VillagerTradesEvent
import net.minecraftforge.event.village.WandererTradesEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.LogicalSide
import net.minecraftforge.fml.common.Mod
import phoenix.client.gui.diaryPages.Chapters
import phoenix.init.PhoenixBlocks
import phoenix.init.PhoenixItems
import phoenix.network.NetworkHandler
import phoenix.network.NetworkHandler.sendTo
import phoenix.network.SyncBookPacket
import phoenix.network.SyncStagePacket
import phoenix.utils.IChapterReader
import phoenix.utils.LogManager
import phoenix.utils.LogManager.error
import phoenix.utils.LogManager.log
import phoenix.utils.Tuple
import phoenix.utils.addChapter
import phoenix.world.GenSaveData
import phoenix.world.StageManager
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.isNotEmpty
import kotlin.collections.set

@Mod.EventBusSubscriber
object PhoenixEvents
{
    @JvmStatic
    @SubscribeEvent
    fun onPlay(event: PlayerEvent.PlayerRespawnEvent)
    {
        if(!event.player.world.isRemote)
        {
            val world = event.player.world as ServerWorld
            val player = event.player
            LogManager.log(this, "Particles!!!")
            world.spawnParticle(ParticleTypes.PORTAL, player.posX, player.posY, player.posZ, 32, 0.1, 2.0, 0.1, 0.5)
        }
    }

    @JvmStatic
    @SubscribeEvent
    fun onSave(event: WorldEvent.Save)
    {
        if(!event.world.isRemote)
        {
            val nbt = event.world.worldInfo.getDimensionData(DimensionType.THE_END)
            StageManager.write(nbt)
            event.world.worldInfo.setDimensionData(DimensionType.THE_END, nbt)
        }
    }

    @JvmStatic
    @SubscribeEvent
    fun onLoad(event: WorldEvent.Load)
    {
        if(!event.world.isRemote)
        {
            val nbt = event.world.worldInfo.getDimensionData(DimensionType.THE_END)
            StageManager.read(nbt)
            NetworkHandler.sendToAll(SyncStagePacket(StageManager.getStage(), StageManager.getPart()))
        }
    }

    @SubscribeEvent
    @JvmStatic
    fun trades(event: VillagerTradesEvent)
    {
        if(event.type == VillagerProfession.TOOLSMITH)
        {
            event.trades[1] =
                ImmutableList.of(VillagerTrades.ITrade { _: Entity, _: Random ->
                    MerchantOffer(
                        ItemStack(Items.EMERALD, 8), ItemStack(
                            PhoenixItems.STEEL_AXE.get()
                        ), 7, 1, 0.1f
                    )
                })
        }
        else if(event.type == VillagerProfession.WEAPONSMITH)
        {
            event.trades[1] =
                ImmutableList.of(VillagerTrades.ITrade { _: Entity, _: Random ->
                    MerchantOffer(
                        ItemStack(Items.EMERALD, 8), ItemStack(
                            PhoenixItems.STEEL_SWORD.get()
                        ), 7, 1, 0.1f
                    )
                })
        }
    }

    @SubscribeEvent
    @JvmStatic
    fun trades(event: WandererTradesEvent)
    {
        event.rareTrades.add(VillagerTrades.ITrade { _: Entity, _: Random -> MerchantOffer(ItemStack(Items.EMERALD, 14), ItemStack(PhoenixBlocks.SETA.get()), 7, 1, 0.1f) })
        event.rareTrades.add(VillagerTrades.ITrade { _: Entity, _: Random -> MerchantOffer(ItemStack(Items.EMERALD, 24), ItemStack(PhoenixItems.GOLDEN_SETA.get(), 4), 7, 1, 0.1f) })
    }

    @SubscribeEvent
    @JvmStatic
    fun capa(event: LootTableLoadEvent)
    {
        if(event.name == LootTables.CHESTS_JUNGLE_TEMPLE)
        {
            event.table.addPool(LootPool.builder().addEntry(ItemLootEntry.builder(PhoenixItems.ZIRCONIUM_INGOT.get()).weight(2)).build())
        }
    }

    var tasks = ArrayList<Tuple<Int, Int, Runnable>>()

    @JvmStatic
    @SubscribeEvent
    fun deferredTasks(event: WorldTickEvent)
    {
        if (!event.world.isRemote)
        {
            if (tasks.isNotEmpty()) if (event.phase == TickEvent.Phase.END)
            {
                var i = 0
                while (i < tasks.size)
                {
                    val current = tasks[i]
                    current.first++
                    if (current.first >= current.second)
                    {
                        current.third.run()
                        tasks.removeAt(i)
                        i--
                    }
                    ++i
                }
            }
        }
    }

    fun addTask(time: Int, r: Runnable)
    {
        tasks.add(Tuple(0, time, r))
    }

    @JvmStatic
    @SubscribeEvent
    fun onSave(event: PlayerEvent.PlayerChangedDimensionEvent)
    {
        if(!event.player.world.isRemote)
        {
            LogManager.log(this, "Phoenix is starting saving")
            val nbt = event.player.world.worldInfo.getDimensionData(DimensionType.THE_END)
            StageManager.write(nbt)
            LogManager.log(this, "Stage = ${StageManager.getStage() + 1} Part = ${StageManager.getPart()}")
            event.player.world.worldInfo.setDimensionData(DimensionType.THE_END, nbt)
            LogManager.log(this, "Phoenix has ended saving")
        }
    }

    var time = 0
    @JvmStatic
    @SubscribeEvent
    fun playerTick(event: TickEvent.PlayerTickEvent)
    {
        if(!event.player.world.isRemote && time % 20 == 0 && event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END)
        {
            val player = event.player as ServerPlayerEntity
            if(player is IChapterReader)
            {
                time++
                for (i in player.inventory.mainInventory)
                {
                    if (i.item === Items.IRON_INGOT)
                    {
                        player.addChapter(Chapters.STEEL)
                    }
                }
            }
        }
    }

    @JvmStatic
    @SubscribeEvent
    fun cornGen(event: EntityJoinWorldEvent)
    {
        val world = event.world
        val entity = event.entity
        if (!world.isRemote && world is ServerWorld && world.dimension.type === DimensionType.THE_END && !GenSaveData[world].isCornGenned)
        {
            val template =  world.structureTemplateManager.getTemplate(ResourceLocation("phoenix:corn/corn"))
            if (template != null)
            {
                GenSaveData[world].setCornGenned()
                template.addBlocksToWorld(world, BlockPos(1000, 100, 1000), PlacementSettings())
                template.addBlocksToWorld(world, BlockPos(-1000, 100, 1000), PlacementSettings())
                template.addBlocksToWorld(world, BlockPos(1000, 100, -1000), PlacementSettings())
                template.addBlocksToWorld(world, BlockPos(-1000, 100, -1000), PlacementSettings())
                log("<Other events> ", "Corn genned ^)")
            }
            else
            {
                error("<Other events> ", "Corn was not genned ^(. template is null... I it is very bad think.")
            }
        }
        if (!event.world.isRemote && entity is ServerPlayerEntity && entity is IChapterReader)
        {
            sendTo(SyncBookPacket(entity.getOpenedChapters()), entity)
        }
    }
}