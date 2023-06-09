package uk.ac.aber.dcs.cs39440.maze_solver.util.algorithms

import kotlinx.coroutines.delay
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.Node
import uk.ac.aber.dcs.cs39440.maze_solver.util.maze_map.Cell
import kotlin.math.pow
import kotlin.math.sqrt


/**
 * Greedy search pathfinding algorithm. The heuristic used for determining the distance from the end is Euclidean distance.
 * @param mazeHeight Maze height
 * @param mazeWidth Maze width
 * @param mazeMap Map of the maze that is considered
 * @param startX X coordinate of the start
 * @param startY Y coordinate of the start
 * @param endX X coordinate of the end
 * @param endY Y coordinate of the end
 * @param updateAffiliation updates affiliation of cells in viewModel
 */
suspend fun greedySearch(
    mazeWidth: Int,
    mazeHeight: Int,
    mazeMap: MutableList<MutableList<Cell>>,
    startX: Int,
    startY: Int,
    endX: Int,
    endY: Int,
    updateAffiliation: (Cell, Node) -> Unit,
    delayLength: Long
): Boolean {
    //Currently considered cell
    var currentCell = mazeMap[startX][startY]
    //List of cells that will be checked
    val cellPriorityList: MutableList<Pair<Cell, Int>> = mutableListOf()
    //Map of parents of each node to get the final path
    val parentMap = mutableMapOf<Cell, Cell>()

    //Check for the neighbours of the start, add them to the list and update parent map
    checkForValidPaths(
        mazeMap = mazeMap,
        currentCell = currentCell,
        mazeWidth = mazeWidth,
        mazeHeight = mazeHeight
    ).forEach { neighbour ->
        cellPriorityList.add(
            Pair(
                neighbour.value,
                calculateDistanceFunction(neighbour.value, endX, endY)
            )
        )
        parentMap[neighbour.value] = currentCell
        updateAffiliation(neighbour.value, Node.Queued)
    }
    //Sort the list based on the combined heuristics and cost to reach
    cellPriorityList.sortBy { it.second }

    //Update the start and end affiliation
    updateAffiliation(currentCell, Node.Considered)
    updateAffiliation(mazeMap[endX][endY], Node.End)

    while (cellPriorityList.isNotEmpty()) {
        //Get the current information and cell
        currentCell = cellPriorityList.removeFirst().first

        if (currentCell.x == endX && currentCell.y == endY) {
            //Reinitialize the start and end indications
            updateAffiliation(mazeMap[endX][endY], Node.End)
            updateAffiliation(mazeMap[startX][startY], Node.Start)

            //Goes through the parent list to find the final path through the maze
            getFinalPath(
                parentMap = parentMap,
                startCell = mazeMap[startX][startY],
                endCell = currentCell,
                updateAffiliation = { cell, node ->
                    updateAffiliation(cell, node)
                },
                delayLength = delayLength
            )
            return true
        } else {
            //Set the node to considered so the algorithm does not go back
            updateAffiliation(currentCell, Node.Considered)
        }

        //Do the same operation as in the beginning, but for the current node
        checkForValidPaths(
            mazeMap = mazeMap,
            currentCell = currentCell,
            mazeWidth = mazeWidth,
            mazeHeight = mazeHeight
        ).forEach { neighbour ->
            cellPriorityList.add(
                Pair(
                    neighbour.value,
                    calculateDistanceFunction(neighbour.value, endX, endY)
                )
            )
            parentMap[neighbour.value] = currentCell
            updateAffiliation(neighbour.value, Node.Queued)
        }
        cellPriorityList.sortBy { it.second }

        //Delay the execution
        delay(delayLength)
    }
    return false
}

//Helper function to calculate the distance function
private fun calculateDistanceFunction(
    currentCell: Cell,
    endX: Int,
    endY: Int
): Int {
    return sqrt(
        (endX - currentCell.x).toDouble().pow(2) + (endY - currentCell.y).toDouble().pow(2)
    ).toInt()
}
