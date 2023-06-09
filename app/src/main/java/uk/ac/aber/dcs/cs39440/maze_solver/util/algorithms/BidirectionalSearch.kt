package uk.ac.aber.dcs.cs39440.maze_solver.util.algorithms

import android.util.Log
import kotlinx.coroutines.delay
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.Direction
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.Node
import uk.ac.aber.dcs.cs39440.maze_solver.util.maze_map.Cell
import java.util.LinkedList
import java.util.Queue

//Value used for Log purposes
private const val LOG_TAG = "BIDIRECTIONAL"

/**
 * Bidirectional breadth-first search
 * @param mazeWidth Width of the maze that will be searched in
 * @param mazeHeight Height of the maze that will be searched in
 * @param mazeMap Map of the maze to search
 * @param startX Horizontal coordinate of the start point in maze
 * @param startY Vertical coordinate of the start point in maze
 * @param endX Horizontal coordinate of the end point in maze
 * @param endY Vertical coordinate of the end point in maze
 * @param updateAffiliation Throws the data to be updated in viewModel
 */
suspend fun bidirectionalSearch(
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
    //Enables to extinguish the intersection
    val startConsideredList = Array(mazeWidth) { Array(mazeHeight) { false } }
    val endConsideredList = Array(mazeWidth) { Array(mazeHeight) { false } }

    //Condition variables
    var startFoundEndPath: Pair<Int, Int>?
    var endFoundStartPath: Pair<Int, Int>?

    //List of not considered neighbours returned from helper function
    var foundPaths: Map<Direction, Cell>

    //Variable that holds the temp value retrieved from the map of neighbours
    var foundCell: Cell

    //Queues of the cells to check if they are solution
    val startQueueToCheck: Queue<Cell> = LinkedList()
    val endQueueToCheck: Queue<Cell> = LinkedList()

    //Stores the parents of cells
    val endParentMap = mutableMapOf<Cell, Cell>()
    val startParentMap = mutableMapOf<Cell, Cell>()

    //Holds the coordinates for the current cells from the start and the end
    var startCurrentCell: Cell
    var endCurrentCell: Cell

    //Add the start and end to the queue
    startQueueToCheck.add(mazeMap[startX][startY])
    endQueueToCheck.add(mazeMap[endX][endY])

    while (startQueueToCheck.isNotEmpty() && endQueueToCheck.isNotEmpty()) {
        //Get the next cells from queue
        startCurrentCell = startQueueToCheck.remove()
        endCurrentCell = endQueueToCheck.remove()

        //Check for the solution
        startFoundEndPath = checkForMeetingPoint(
            mazeMap = mazeMap,
            currentCell = startCurrentCell,
            otherEndConsideredList = endConsideredList,
            mazeWidth = mazeWidth,
            mazeHeight = mazeHeight
        )
        endFoundStartPath = checkForMeetingPoint(
            mazeMap = mazeMap,
            currentCell = endCurrentCell,
            otherEndConsideredList = startConsideredList,
            mazeWidth = mazeWidth,
            mazeHeight = mazeHeight
        )

        if (startFoundEndPath != null || endFoundStartPath != null) {
            //Reinitialize the start and end indications
            updateAffiliation(mazeMap[endX][endY], Node.End)
            updateAffiliation(mazeMap[startX][startY], Node.Start)

            var meetingCell = Cell(-1, -1)

            if (startFoundEndPath != null) {
                meetingCell =
                    mazeMap[startFoundEndPath.first][startFoundEndPath.second].copy(affiliation = Node.Corridor)
                startParentMap[meetingCell] = startCurrentCell

            } else if (endFoundStartPath != null) {
                meetingCell =
                    mazeMap[endFoundStartPath.first][endFoundStartPath.second].copy(affiliation = Node.Corridor)
                endParentMap[meetingCell] = endCurrentCell
            }

            //Goes through the parent list to find the final path through the maze
            getBidirectionalFinalPath(
                startParentMap = startParentMap,
                endParentMap = endParentMap,
                meetingCell = meetingCell,
                startCell = mazeMap[startX][startY],
                endCell = mazeMap[endX][endY],
                updateAffiliation = { cell, node ->
                    updateAffiliation(cell, node)
                },
                delayLength = delayLength
            )
            return true
        } else {
            //Set the node to considered so the algorithm does not go back
            updateAffiliation(startCurrentCell, Node.Considered)
            updateAffiliation(endCurrentCell, Node.Considered)
        }

        //Get a list of not considered start going cell neighbours
        foundPaths = checkForValidPaths(
            mazeMap = mazeMap,
            currentCell = startCurrentCell,
            mazeWidth = mazeWidth,
            mazeHeight = mazeHeight
        )

        //Loop through the neighbours, add them to the queue and update the start parent map
        if (foundPaths.isNotEmpty()) {
            for (direction in Direction.values()) {
                if (foundPaths.containsKey(direction)) {
                    foundCell = foundPaths[direction]!!
                    startParentMap[foundCell] = startCurrentCell
                    startConsideredList[foundCell.x][foundCell.y] = true
                    startQueueToCheck.add(foundCell)
                    updateAffiliation(foundCell, Node.Queued)
                }
            }
        }

        //Get a list of not considered end going cell neighbours
        foundPaths = checkForValidPaths(
            mazeMap = mazeMap,
            currentCell = endCurrentCell,
            mazeWidth = mazeWidth,
            mazeHeight = mazeHeight
        )

        //Loop through the neighbours, add them to the queue and update the end parent map
        if (foundPaths.isNotEmpty()) {
            for (direction in Direction.values()) {
                if (foundPaths.containsKey(direction)) {
                    foundCell = foundPaths[direction]!!
                    endParentMap[foundCell] = endCurrentCell
                    endConsideredList[foundCell.x][foundCell.y] = true
                    endQueueToCheck.add(foundCell)
                    updateAffiliation(foundCell, Node.Queued)
                }
            }
        }
        //Slows down the execution
        delay(delayLength)
    }
    return false
}

//Helper function that checks if the current cell is the meeting point between the searches
private fun checkForMeetingPoint(
    mazeMap: MutableList<MutableList<Cell>>,
    currentCell: Cell,
    otherEndConsideredList: Array<Array<Boolean>>,
    mazeWidth: Int,
    mazeHeight: Int
): Pair<Int, Int>? {
    var coordinatesPair: Pair<Int, Int>? = null
    for (direction in Direction.values()) {
        when (direction) {
            Direction.TOP -> {
                if (currentCell.y - 1 >= 0) {
                    if (otherEndConsideredList[currentCell.x][currentCell.y - 1]
                        && currentCell.walls[Direction.TOP] == false
                        && mazeMap[currentCell.x][currentCell.y - 1].walls[Direction.DOWN] == false
                    ) {
                        coordinatesPair = Pair(currentCell.x, currentCell.y - 1)
                    }
                }
            }

            Direction.RIGHT -> {
                if (currentCell.x + 1 < mazeWidth) {
                    if (otherEndConsideredList[currentCell.x + 1][currentCell.y]
                        && currentCell.walls[Direction.RIGHT] == false
                        && mazeMap[currentCell.x + 1][currentCell.y].walls[Direction.LEFT] == false
                    ) {
                        coordinatesPair = Pair(currentCell.x + 1, currentCell.y)
                    }
                }
            }

            Direction.DOWN -> {
                if (currentCell.y + 1 < mazeHeight) {
                    if (otherEndConsideredList[currentCell.x][currentCell.y + 1]
                        && currentCell.walls[Direction.DOWN] == false
                        && mazeMap[currentCell.x][currentCell.y + 1].walls[Direction.TOP] == false
                    ) {
                        coordinatesPair = Pair(currentCell.x, currentCell.y + 1)
                    }
                }
            }

            Direction.LEFT -> {
                if (currentCell.x - 1 >= 0) {
                    if (otherEndConsideredList[currentCell.x - 1][currentCell.y]
                        && currentCell.walls[Direction.LEFT] == false
                        && mazeMap[currentCell.x - 1][currentCell.y].walls[Direction.RIGHT] == false
                    ) {
                        coordinatesPair = Pair(currentCell.x - 1, currentCell.y)
                    }
                }
            }
        }
    }
    return coordinatesPair
}

