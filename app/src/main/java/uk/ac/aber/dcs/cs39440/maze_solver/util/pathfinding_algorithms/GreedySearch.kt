package uk.ac.aber.dcs.cs39440.maze_solver.util.pathfinding_algorithms

import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.Node
import uk.ac.aber.dcs.cs39440.maze_solver.util.maze_map.Cell
import java.util.PriorityQueue

class GreedySearch(
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

    //Comparator to distinguish which cell is closer to the end for priority queue
    private val cellComparator = Comparator<Cell> { cell1, cell2 ->
        cellDistanceFunction(cell = cell1, costToReach = 0)
        -cellDistanceFunction(cell = cell2, costToReach = 0)
    }
    private val queueToCheck: PriorityQueue<Cell> = PriorityQueue<Cell>(cellComparator)

    //Initialize the class with the starting node already inserted into queue
    init {
        queueToCheck.add(startCell)
    }

    override fun run(
        mazeMap: MutableList<MutableList<Cell>>,
        updateAffiliation: (Cell, Node) -> Unit
    ): Boolean {
        if (queueToCheck.isNotEmpty()) {
            currentCell = queueToCheck.remove()

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
            queueToCheck.add(it.value)
            cellList.add(it.value)
        }
        return cellList.toList()
    }
}