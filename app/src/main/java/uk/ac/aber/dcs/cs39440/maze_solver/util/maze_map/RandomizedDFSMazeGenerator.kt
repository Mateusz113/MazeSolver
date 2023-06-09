package uk.ac.aber.dcs.cs39440.maze_solver.util.maze_map

import android.util.Log
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.Direction
import java.util.Random
import java.util.Stack

private const val LOG_TAG = "GENERATOR"

/**
 * Maze generator done using the randomized depth-first search and implemented with stack
 * @param mazeMap data structure that holds the maze information
 * @param mazeWidth width of the maze
 * @param mazeHeight height of the maze
 */
fun randomizedDfsMazeGenerator(
    mazeMap: MutableList<MutableList<Cell>>,
    mazeWidth: Int,
    mazeHeight: Int
): MutableList<MutableList<Cell>> {
    //Initialise the node stack
    val nodeStack: Stack<Cell> = Stack()

    //Initialize the cell variables
    var currentCell: Cell
    var nextCell: Cell

    //Variable to hold the generated map of neighbours
    var neighbourMap: MutableMap<Cell, Direction>

    //Initialize the maze as the grid of cells
    for (col in 0 until mazeWidth) {
        val rowList = mutableListOf<Cell>()
        for (row in 0 until mazeHeight) {
            currentCell = Cell(col, row)
            rowList.add(currentCell)
        }
        mazeMap.add(rowList)
    }

//    for (i in 0 until mazeWidth) {
//        Log.d(
//            LOG_TAG,
//            "Column $i is: ${mazeMap[i]}"
//        )
//    }

    //Initialize the random cells as the start
    val random = Random()
    val randomColumn = random.nextInt(mazeWidth)
    val randomRow = random.nextInt(mazeHeight)
    currentCell = mazeMap[randomColumn][randomRow]
    currentCell = currentCell.copy(visited = true)
    nodeStack.push(currentCell)

    //Loop through the cells until every one is visited
    while (nodeStack.isNotEmpty()) {
        currentCell = nodeStack.pop()

        //Get the list of unvisited neighbours
        neighbourMap = getUnvisitedNeighbours(
            currentCell = currentCell,
            mazeMap = mazeMap,
            mazeWidth = mazeWidth,
            mazeHeight = mazeHeight
        )
        if (neighbourMap.isNotEmpty()) {
            //Choose the random cell if the map is not empty
            nodeStack.push(currentCell)
            nextCell = neighbourMap.keys.random()
            mazeMap[nextCell.x][nextCell.y] = mazeMap[nextCell.x][nextCell.y].copy(visited = true)
            nodeStack.push(nextCell)

            //Remove the correct walls
            neighbourMap[nextCell]?.let { direction ->
                removeWalls(
                    currentCell = currentCell,
                    nextCell = nextCell,
                    directionToRemove = direction,
                    mazeMap = mazeMap
                )
            }
        }
    }
    return mazeMap
}
