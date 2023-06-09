package uk.ac.aber.dcs.cs39440.maze_solver.util.maze_map

import android.util.Log
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.Direction
import java.util.Random

private const val LOG_TAG = "PRIMS"

/**
 * Maze generator done using the randomized depth-first search and implemented with stack
 * @param mazeMap data structure that holds the maze information
 * @param mazeWidth width of the maze
 * @param mazeHeight height of the maze
 */
fun primsMazeGenerator(
    mazeMap: MutableList<MutableList<Cell>>,
    mazeWidth: Int,
    mazeHeight: Int
): MutableList<MutableList<Cell>> {

    //Initialize the cell variables
    var currentCell: Cell
    val listOfAdjacentCells: MutableList<Triple<Cell, Cell, Direction>> = mutableListOf()
    var adjacentCells: Triple<Cell, Cell, Direction>

    //Variable to hold the generated map of neighbours
    var neighbourMap: Map<Cell, Direction>

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
//        Log.i(
//            LOG_TAG,
//            "Column $i is: ${mazeMap[i]}"
//        )
//    }

    //Initialize the random cell as the start
    val random = Random()
    val randomColumn = random.nextInt(mazeWidth)
    val randomRow = random.nextInt(mazeHeight)
    currentCell = mazeMap[randomColumn][randomRow]
    currentCell = currentCell.copy(visited = true)

    //Get the list
    getUnvisitedNeighbours(
        currentCell = currentCell,
        mazeMap = mazeMap,
        mazeWidth = mazeWidth,
        mazeHeight = mazeHeight
    ).forEach {
        listOfAdjacentCells.add(Triple(currentCell, it.key, it.value))
    }

    while (listOfAdjacentCells.isNotEmpty()) {
        //Get the random adjacent cells
        val randomIndex = random.nextInt(listOfAdjacentCells.size)
        adjacentCells = listOfAdjacentCells[randomIndex]
        listOfAdjacentCells.removeAt(randomIndex)

//        Log.d(
//            LOG_TAG,
//            "Current adjacent cell $adjacentCells. Adjacent cells list ${listOfAdjacentCells.size}."
//        )

        //If any one of the cells was not visited then remove walls between, set them as visited and add the unvisited adjacent cells to the list
        if (!mazeMap[adjacentCells.first.x][adjacentCells.first.y].visited || !mazeMap[adjacentCells.second.x][adjacentCells.second.y].visited) {

            removeWalls(
                currentCell = adjacentCells.first,
                nextCell = adjacentCells.second,
                directionToRemove = adjacentCells.third,
                mazeMap = mazeMap
            )

            val unvisitedCell: Cell =
                if (!mazeMap[adjacentCells.first.x][adjacentCells.first.y].visited) {
                    mazeMap[adjacentCells.first.x][adjacentCells.first.y]
                } else {
                    mazeMap[adjacentCells.second.x][adjacentCells.second.y]
                }

            mazeMap[adjacentCells.first.x][adjacentCells.first.y] =
                mazeMap[adjacentCells.first.x][adjacentCells.first.y].copy(visited = true)
            mazeMap[adjacentCells.second.x][adjacentCells.second.y] =
                mazeMap[adjacentCells.second.x][adjacentCells.second.y].copy(visited = true)


            getUnvisitedNeighbours(
                currentCell = unvisitedCell,
                mazeMap = mazeMap,
                mazeHeight = mazeHeight,
                mazeWidth = mazeWidth
            ).forEach {
                listOfAdjacentCells.add(Triple(unvisitedCell, it.key, it.value))
            }
        }
    }
    return mazeMap
}
