package uk.ac.aber.dcs.cs39440.maze_solver.util.pathfinding_algorithms

import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.Node
import uk.ac.aber.dcs.cs39440.maze_solver.util.maze_map.Cell
import java.util.PriorityQueue

class AStar(
    override var mazeWidth: Int,
    override var mazeHeight: Int,
    override var startCell: Cell,
    override var endCell: Cell
) : SingleStartPathfinder {
    //Variables inherited from the interface
    override val parentMap = mutableMapOf<Cell, Cell>()
    override var finalPathCell: Cell? = null

    //Class variables
    private var currentCell = Cell(-1, -1)
    private var currentPair = Pair(currentCell, 0)

    //Comparator to distinguish which cell is closer to the end for priority queue
    private val cellComparator = Comparator<Pair<Cell, Int>> { entry1, entry2 ->
        cellDistanceFunction(cell = entry1.first, costToReach = entry1.second)
        -cellDistanceFunction(cell = entry2.first, costToReach = entry2.second)
    }
    private val queueToCheck: PriorityQueue<Pair<Cell, Int>> =
        PriorityQueue<Pair<Cell, Int>>(cellComparator)

    //Initialize the class with the starting node already inserted into queue
    init {
        queueToCheck.add(Pair(startCell, 0))
    }

    override fun run(
        mazeMap: MutableList<MutableList<Cell>>,
        updateAffiliation: (Cell, Node) -> Unit
    ): Boolean {
        if (queueToCheck.isNotEmpty()) {
            //First item in pair is the cell
            val currentPair = queueToCheck.remove()
            currentCell = currentPair.first

            //Returns true as algorithm is finished
            if (currentCell.x == endCell.x && currentCell.y == endCell.y) {
                finalPathCell = currentCell
                return true
            } else {
                updateAffiliation(currentCell, Node.Considered)
            }
            addNeighboursToQueue(mazeMap).forEach { cell ->
                updateAffiliation(cell, Node.Queued)
            }
        }
        return false
    }

    //Function returns the list of items that were added to the map
    private fun addNeighboursToQueue(
        mazeMap: MutableList<MutableList<Cell>>,
    ): List<Cell> {
        val cellList = mutableListOf<Cell>()
        checkForValidPaths(
            mazeMap = mazeMap,
            currentCell = currentCell
        ).forEach {
            parentMap[it.value] = currentCell
            queueToCheck.add(Pair(it.value, currentPair.second + 1))
            cellList.add(it.value)
        }
        return cellList.toList()
    }
}