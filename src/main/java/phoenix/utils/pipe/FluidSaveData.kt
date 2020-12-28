package phoenix.utils.pipe

import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld
import phoenix.utils.SizedArrayList
import phoenix.utils.graph.GraphNode
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class FluidSaveData
{
    val INF = Integer.MAX_VALUE / 3;
    var dp = SizedArrayList<SizedArrayList<Int>>()
    var parents = SizedArrayList<SizedArrayList<Int>>()
    var graph = ArrayList<HashSet<Int>>()
    var removedIndexes = LinkedList<Int>()
    var elements = ArrayList<GraphNode>()

    fun addBlock(world : IWorld, pos: BlockPos, tile : IFluidPipe, isInput : Boolean, isOutput : Boolean)
    {
        if(!removedIndexes.isEmpty())
        {
            tile.numberInGraph = removedIndexes.first
            removedIndexes.removeFirst()
            graph[tile.numberInGraph] = HashSet()
            elements[tile.numberInGraph] = GraphNode(world, pos, isInput, isOutput)
        }
        else
        {
            tile.numberInGraph = graph.size
            graph.add(HashSet())
            elements.add(GraphNode(world, pos, isInput, isOutput))
        }

        dp.resize(graph.size, SizedArrayList(graph.size, INF))
        for (i in dp)
            i.resize(graph.size, INF)

        parents.resize(graph.size, SizedArrayList(graph.size, -1))
        for (i in parents)
            i.resize(graph.size, -1)

        for (dir in Direction.values())
        {
            val neig = world.getTileEntity(pos.offset(dir))
            if (neig != null && neig is IFluidPipe)
            {
                graph[tile.numberInGraph].add(neig.numberInGraph)
                graph[neig.numberInGraph].add(tile.numberInGraph)
                dp[tile.numberInGraph][neig.numberInGraph] = 1
            }
        }

        for (i in 0 until dp.size)
            for (j in 0 until dp.size)
                if (dp[i][j] != INF)
                    parents[i][j] = i

        for (i in 0 until dp.size) dp[i][i] = 0

        for (k in 0 until dp.size)
            for (i in 0 until dp.size)
                for (j in 0 until dp.size)
                    if (dp[i][k] + dp[k][j] < dp[i][j])
                    {
                        dp[i][j] = dp[i][k] + dp[k][j]
                        parents[i][j] = parents[k][j]
                    }
    }

    fun getWay(u : Int, v : Int) : ArrayList<Int>
    {
        val ans : ArrayList<Int> = ArrayList()
        var v = v
        var i = 0
        while (v != u)
        {
            assert(ans.size <= parents.size)
            i++
            ans.add(v)
            v = parents[u][v]
        }
        ans.reverse()
        return ans
    }


    fun removeBlock(numberInGraph: Int)
    {
        assert(numberInGraph < graph.size && numberInGraph > 0)
        for (i in graph[numberInGraph])
            graph[i].remove(numberInGraph)

        for (el in elements)
        {
            val entity = el.tileEntity
            if (entity is IFluidMechanism)
            {
                (entity as IFluidMechanism).removeMechanismByIndex(numberInGraph)
            }
        }
        graph[numberInGraph] = HashSet()
        removedIndexes.add(numberInGraph)
    }
}