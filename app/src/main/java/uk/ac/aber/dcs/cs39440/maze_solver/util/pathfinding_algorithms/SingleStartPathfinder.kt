package uk.ac.aber.dcs.cs39440.maze_solver.util.pathfinding_algorithms

import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.Node
import uk.ac.aber.dcs.cs39440.maze_solver.util.maze_map.Cell
import kotlin.math.pow
import kotlin.math.sqrt

interface SingleStartPathfinder : Pathfinder {
    //Map used to retrieve the path from end to the start of maze
    val parentMap: MutableMap<Cell, Cell>
    //Start cell of the maze
    val startCell: Cell
    //End cell of the maze
    val endCell: Cell
    //Cell used as current cell in getFinalPath function
    var finalPathCell: Cell?

    override fun getFinalPath(
        updateAffiliation: (Cell, Node) -> Unit
    ): Boolean {
        var isCellTheEnd = true

        finalPathCell = parentMap[finalPathCell]!!

        finalPathCell?.let {
            if (it.x != startCell.x || it.y != startCell.y) {
                //Updates the node in map to be the final path
                updateAffiliation(it, Node.FinalPath)
                //Update the return value to reflect the state
                isCellTheEnd = false
            }
        }
        return isCellTheEnd
    }

    override fun updateMazeIndicators(
        updateAffiliation: (Cell, Node) -> Unit
    ) {
        updateAffiliation(startCell, Node.Start)
        updateAffiliation(endCell, Node.End)
    }

    /**
     * Function to calculate the distance function to the end of maze
     */
    fun cellDistanceFunction(
        cell: Cell,
        costToReach: Int
    ): Int {
        val currentCellHeuristics = sqrt(
            (endCell.x - cell.x).toDouble().pow(2) + (endCell.y - cell.y).toDouble()
                .pow(2)
        ).toInt()
        return currentCellHeuristics + costToReach
    }
}