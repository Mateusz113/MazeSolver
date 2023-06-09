package uk.ac.aber.dcs.cs39440.maze_solver.util.algorithms

import android.util.Log
import kotlinx.coroutines.delay
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.Node
import uk.ac.aber.dcs.cs39440.maze_solver.util.maze_map.Cell

//Used for logging
private const val LOG_TAG = "FINAL_PATH"

/**
 * Function that works out the final path from start to the exit of the maze.
 * @param parentMap Map of the parents of each node
 * @param startCell Cell that is the start of the maze
 * @param endCell Cell that is the end of the maze
 * @param updateAffiliation Hoisting the state of the maze changes
 */
suspend fun getFinalPath(
    parentMap: MutableMap<Cell, Cell>,
    startCell: Cell,
    endCell: Cell,
    updateAffiliation: (Cell, Node) -> Unit,
    delayLength: Long
) {
    //Called to skip the overriding of maze end as final path
    var currentCell = endCell
    currentCell = parentMap[currentCell]!!

    //Stops when reaches the start of maze
    while (currentCell.x != startCell.x || currentCell.y != startCell.y) {

        //Updates the node in map to be the final path
        updateAffiliation(currentCell, Node.FinalPath)

        //Get the parent of current node and override variables
        currentCell = parentMap[currentCell]!!

        //Slows down the execution
        delay(delayLength)
    }
}

/**
 * Function similar to the getFinalPath, but it works from the meeting points towards both start and end points.
 * @param startParentMap Map of parents towards the start
 * @param endParentMap Map of parents towards the end
 * @param meetingCell Meeting cell in the algorithm
 * @param startCell Start going cell
 * @param endCell End going cell
 * @param updateAffiliation Used to update the cell affiliation
 */
suspend fun getBidirectionalFinalPath(
    startParentMap: MutableMap<Cell, Cell>,
    endParentMap: MutableMap<Cell, Cell>,
    meetingCell: Cell,
    startCell: Cell,
    endCell: Cell,
    updateAffiliation: (Cell, Node) -> Unit,
    delayLength: Long
) {
    //Called to skip the overriding of maze end as final path
    updateAffiliation(meetingCell, Node.FinalPath)

    var startGoingCell = startParentMap[meetingCell]
    var endGoingCell = endParentMap[meetingCell]

    var startReached = false
    var endReached = false


    //Stops when reaches the start of maze
    while (!startReached || !endReached) {
        startGoingCell?.let {
            if (startGoingCell!!.x == startCell.x && startGoingCell!!.y == startCell.y) {
                startReached = true
            } else {
                updateAffiliation(it, Node.FinalPath)
            }
        }
        endGoingCell?.let {
            if (endGoingCell!!.x == endCell.x && endGoingCell!!.y == endCell.y) {
                endReached = true
            } else {
                updateAffiliation(it, Node.FinalPath)
            }
        }

        //Get the parent of current node and override variables
        startGoingCell = startParentMap[startGoingCell]
        endGoingCell = endParentMap[endGoingCell]

        //Slows down the execution
        delay(delayLength)
    }
}
