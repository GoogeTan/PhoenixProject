package phoenix.other

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.state.IProperty
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.IWorld
import net.minecraft.world.IWorldReader
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraft.world.chunk.IChunk
import net.minecraft.world.gen.GenerationStage
import net.minecraft.world.gen.IExtendedNoiseRandom
import net.minecraft.world.gen.area.IArea
import net.minecraft.world.gen.area.IAreaFactory
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.IFeatureConfig
import net.minecraft.world.gen.feature.NoFeatureConfig
import net.minecraft.world.gen.feature.OreFeatureConfig
import net.minecraft.world.gen.feature.structure.Structure
import net.minecraft.world.gen.layer.traits.IAreaTransformer0
import net.minecraft.world.gen.layer.traits.IAreaTransformer1
import net.minecraft.world.gen.layer.traits.IAreaTransformer2
import net.minecraft.world.gen.placement.CountRangeConfig
import net.minecraft.world.gen.placement.IPlacementConfig
import net.minecraft.world.gen.placement.Placement
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilderConfig
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder
import phoenix.api.entity.Date
import phoenix.api.entity.IPhoenixPlayer
import phoenix.client.gui.diary.Chapter
import phoenix.init.PhxBlocks
import phoenix.network.SyncBookPacket
import phoenix.network.sendToPlayer
import kotlin.math.sqrt

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
fun World.destroyBlock(pos: BlockPos, shouldDrop: Boolean, entity: Entity?, stack: ItemStack) : Boolean
{
    val state = this[pos]
    return if (state.isAir(this, pos))
    {
        false
    } else
    {
        val fluidState = this.getFluidState(pos)
        if (shouldDrop)
        {
            val tile = if (state.hasTileEntity()) this.getTileEntity(pos) else null
            Block.spawnDrops(state, this, pos, tile, entity, stack)
        }
        this.setBlockState(pos, fluidState.blockState)
    }
}

operator fun <T : Comparable<T>, V : T> World.set(pos: BlockPos, property: IProperty<T>, value: V)
{
    val state = this[pos]
    this[pos] = state.with(property, value)
}

operator fun IWorldReader.get(pos: BlockPos): BlockState = getBlockState(pos)
operator fun <T : Comparable<T>> World.get(pos: BlockPos, property: IProperty<T>) : T = this[pos][property]
operator fun IWorld.get(pos: BlockPos) : BlockState = this.getBlockState(pos)
operator fun IWorld.set(pos: BlockPos, state : BlockState, flag : Int) : Boolean = this.setBlockState(pos, state, flag)
operator fun IWorld.set(pos: BlockPos, state : BlockState) : Boolean = this.setBlockState(pos, state, 3)

operator fun IChunk.get(pos: BlockPos) : BlockState = this.getBlockState(pos)
operator fun IChunk.set(pos: BlockPos, state : BlockState) = this.setBlockState(pos, state, false)
operator fun IChunk.set(pos: BlockPos, state : BlockState, isMoving : Boolean) = this.setBlockState(pos, state, isMoving)

fun BlockPos.add(vec : Vec3d): BlockPos = add(vec.x.toInt(), vec.y.toInt(), vec.z.toInt())

fun IWorld.getDownHeight(pos: BlockPos, max: Int): BlockPos
{
    val pos2 = BlockPos(pos.x, 0, pos.z)
    for (i in 0 until max)
    {
        if (!this.isAirBlock(pos2.add(0, i, 0))) return pos2.add(0, i - 1, 0)
    }
    return pos
}

private const val daysAYear = 319
private const val dayLength = 12000
private const val secondLength = 12000

fun World.getDate() = Date((795 + dayTime) % dayLength / secondLength, (gameTime + 2005) % daysAYear, (gameTime + 2005) / daysAYear)

fun ServerPlayerEntity.addChapter(chapter: Chapter?)
{
    if (chapter == null)
        return
    if(this is IPhoenixPlayer)
    {
        this.addChapter(chapter.id, world.getDate())
        SyncBookPacket(this.getOpenedChapters()).sendToPlayer(this)
        sendMessage("Chapter ${getOpenedChapters()}")
    }
}

inline fun <reified T> IWorld.getTileAt(pos: BlockPos): T?
{
    val tile = getTileEntity(pos)
    return if(tile is T) tile else null
}

fun Biome.addStructure(structure: Structure<NoFeatureConfig>)
{
    addStructure(structure.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG))
    addFeature(
        GenerationStage.Decoration.SURFACE_STRUCTURES,
        structure.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG)
            .withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG))
    )
}

fun Biome.addZirconiumOre()
{
    addFeature(
        GenerationStage.Decoration.UNDERGROUND_ORES,
        Feature.ORE.withConfiguration(
            OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, PhxBlocks.zirconium.defaultState, 4)
        ).withPlacement(
            Placement.COUNT_RANGE.configure(CountRangeConfig(20, 0, 0, 64))
        )
    )
}

operator fun <R : IArea> IAreaTransformer1.invoke(context: IExtendedNoiseRandom<R>, area: IAreaFactory<R>) : IAreaFactory<R> = apply(context, area)
operator fun <R : IArea> IAreaTransformer2.invoke(context: IExtendedNoiseRandom<R>, area1: IAreaFactory<R>, area2: IAreaFactory<R>) : IAreaFactory<R> = apply(context, area1, area2)
operator fun <R : IArea> IAreaTransformer0.invoke(context: IExtendedNoiseRandom<R>) : IAreaFactory<R> = apply(context)

operator fun BlockPos.minus(second: BlockPos) : Double
{
    return sqrt(((x - second.x) * (x - second.x) + (x - second.y) * (y - second.y) + (x - second.z) * (z - second.z)).toDouble())
}

fun isNear(pos: BlockPos, poses: Collection<BlockPos>, range: Int): Boolean
{
    for (pos1 in poses) if (pos - pos1 < range) return false
    return true
}

fun <T : ISurfaceBuilderConfig> defaultSettingsForEnd(surfaceBuilderIn: SurfaceBuilder<T>, surfaceBuilderConfigIn: T): Biome.Builder
{
    return Biome.Builder().surfaceBuilder(surfaceBuilderIn, surfaceBuilderConfigIn)
        .precipitation(Biome.RainType.NONE)
        .category(Biome.Category.THEEND)
        .depth(0.1f)
        .scale(0.2f)
        .temperature(0.5f)
        .downfall(0.5f)
        .waterColor(4159204)
        .waterFogColor(329011)
        .parent(null)
}