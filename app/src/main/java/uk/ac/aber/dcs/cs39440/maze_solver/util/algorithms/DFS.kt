package uk.ac.aber.dcs.cs39440.maze_solver.util.algorithms

import android.util.Log
import kotlinx.coroutines.delay
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.Direction
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.Node
import uk.ac.aber.dcs.cs39440.maze_solver.util.maze_map.Cell
import java.util.*

//Used for logging
private const val LOG_TAG = "DFS"

/**
 * Depth-first search algorithm used for pathfinding within the maze
 * @param mazeWidth Width of the maze that will be searched in
 * @param mazeHeight Height of the maze that will be searched in
 * @param mazeMap Map of the maze to search
 * @param startX Horizontal coordinate of the start point in maze
 * @param startY Vertical coordinate of the start point in maze
 * @param endX Horizontal coordinate of the end point in maze
 * @param endY Vertical coordinate of the end point in maze
 * @param updateAffiliation Hoisting the state about the maze changes
 */
suspend fun dfs(
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
//    Log.i(LOG_TAG, "Starting the pathfinding")

    /**
     * Init of the parameters used
     */
    //Hold the coordinates for the current cell
    var currentCell: Cell

    //List of not considered neighbours returned from helper function
    var foundPaths: Map<Direction, Cell>

    //Variable that holds the temp value retrieved from the map of neighbours
    var foundCell: Cell

    //Double-ended queue of the cells to check if they are solution
    val queueToCheck: Deque<Cell> = LinkedList()

    //Stores the map of parents coordinates of cells
    val parentMap = mutableMapOf<Cell, Cell>()

    //Set up the end indication
    updateAffiliation(mazeMap[endX][endY], Node.End)

    //Initialize the dequeue with start
    currentCell = mazeMap[startX][startY]
    queueToCheck.add(currentCell)
    updateAffiliation(currentCell, Node.Considered)


    while (queueToCheck.isNotEmpty()) {
        //Get the next cell from the queue
        currentCell = queueToCheck.remove()

        //Check if the current node is the end
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
//            Log.d(LOG_TAG, "Set the cell [${currentCell.x},${currentCell.y}] as the considered.")
        }

        //Get a list of not considered neighbours
        foundPaths = checkForValidPaths(
            mazeMap = mazeMap,
            currentCell = currentCell,
            mazeWidth = mazeWidth,
            mazeHeight = mazeHeight
        )

//        Log.d(LOG_TAG, "Found neighbours: $foundPaths")

        //Loop through the neighbours, add them to the queue and update the parent map
        if (foundPaths.isNotEmpty()) {
            for (direction in Direction.values().reversedArray()) {
                if (foundPaths.containsKey(direction)) {
                    foundCell = foundPaths[direction]!!
                    parentMap[foundCell] = currentCell
                    queueToCheck.addFirst(foundCell)
                    updateAffiliation(foundCell, Node.Queued)
                }
            }
        }

        //Slows down the execution
        delay(delayLength)
    }
    //Return false, because pathfinding failed
    return false
}
