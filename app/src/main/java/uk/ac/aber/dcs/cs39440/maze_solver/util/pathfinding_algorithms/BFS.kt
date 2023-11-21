package uk.ac.aber.dcs.cs39440.maze_solver.util.pathfinding_algorithms

import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.Direction
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.Node
import uk.ac.aber.dcs.cs39440.maze_solver.util.maze_map.Cell
import java.util.LinkedList
import java.util.Queue


class BFS(
    override var mazeWidth: Int,
    override var mazeHeight: Int,
    override var startCell: Cell,
    override var endCell: Cell
) : SingleStartPathfinder {
    //Inherited variables from interface
    override val parentMap = mutableMapOf<Cell, Cell>()
    override var finalPathCell: Cell? = null

    //Class specific variables
    private val queueToCheck: Queue<Cell> = LinkedList()
    private var currentCell = Cell(-1, -1)

    //Initialize the class with the starting node already inserted into queue
    init {
        queueToCheck.add(startCell)
    }

    //Breadth-first search run implementation
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
        val validPathsMap = checkForValidPaths(
            mazeMap = mazeMap,
            currentCell = currentCell
        )
        for (direction in Direction.values()) {
            validPathsMap[direction]?.let {
                parentMap[it] = currentCell
                queueToCheck.add(it)
                cellList.add(it)
            }
        }
        return cellList.toList()
    }

}
