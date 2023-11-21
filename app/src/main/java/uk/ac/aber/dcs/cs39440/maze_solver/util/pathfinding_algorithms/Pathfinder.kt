package uk.ac.aber.dcs.cs39440.maze_solver.util.pathfinding_algorithms

import kotlinx.coroutines.delay
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.Direction
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.Node
import uk.ac.aber.dcs.cs39440.maze_solver.util.maze_map.Cell

interface Pathfinder {
    //Maze size indicators
    val mazeWidth: Int
    val mazeHeight: Int

    /**
     * Invokes the next step of the algorithm
     * @param mazeMap Map of the maze it is working on
     * @param updateAffiliation Affiliation updating lambda
     * @return boolean indicating if the path through the maze was found
     */
    fun run(
        mazeMap: MutableList<MutableList<Cell>>,
        updateAffiliation: (Cell, Node) -> Unit
    ): Boolean

    /**
     * Invokes the next step of finding final path
     * @param updateAffiliation lambda that is sent back and used to update maze
     * @return boolean indicating if the final path is done
     */
    fun getFinalPath(
        updateAffiliation: (Cell, Node) -> Unit
    ): Boolean

    /**
     * Updates the indicators for special cells
     * @param updateAffiliation lambda that is sent back and used to update maze
     */
    fun updateMazeIndicators(
        updateAffiliation: (Cell, Node) -> Unit
    )

    /**
     * Function that checks for valid paths from the current cell in 4 basic directions
     * @param mazeMap map of the maze it is searching in
     * @param currentCell current cell that needs to be checked for valid neighbours
     * @return map of direction to cell that indicates there is a valid neighbour if the direction is in the map
     */
    fun checkForValidPaths(
        mazeMap: MutableList<MutableList<Cell>>,
        currentCell: Cell
    ): Map<Direction, Cell> {
        val mapOfPaths = mutableMapOf<Direction, Cell>()
        var foundPath: Cell

        for (direction in Direction.values()) {
            when (direction) {
                //Checks top
                Direction.TOP -> {
                    if (currentCell.y - 1 >= 0
                        && (mazeMap[currentCell.x][currentCell.y - 1].affiliation == Node.Corridor
                                || mazeMap[currentCell.x][currentCell.y - 1].affiliation == Node.End)
                        && currentCell.walls[Direction.TOP] == false
                        && mazeMap[currentCell.x][currentCell.y - 1].walls[Direction.DOWN] == false
                    ) {
                        foundPath = mazeMap[currentCell.x][currentCell.y - 1]
                        mapOfPaths[Direction.TOP] = foundPath
                    }
                }

                //Checks right
                Direction.RIGHT -> {
                    if (currentCell.x + 1 < mazeWidth
                        && (mazeMap[currentCell.x + 1][currentCell.y].affiliation == Node.Corridor
                                || mazeMap[currentCell.x + 1][currentCell.y].affiliation == Node.End)
                        && currentCell.walls[Direction.RIGHT] == false
                        && mazeMap[currentCell.x + 1][currentCell.y].walls[Direction.LEFT] == false
                    ) {
                        foundPath = mazeMap[currentCell.x + 1][currentCell.y]
                        mapOfPaths[Direction.RIGHT] = foundPath
                    }
                }

                //Checks down
                Direction.DOWN -> {
                    if (currentCell.y + 1 < mazeHeight
                        && (mazeMap[currentCell.x][currentCell.y + 1].affiliation == Node.Corridor
                                || mazeMap[currentCell.x][currentCell.y + 1].affiliation == Node.End)
                        && currentCell.walls[Direction.DOWN] == false
                        && mazeMap[currentCell.x][currentCell.y + 1].walls[Direction.TOP] == false
                    ) {
                        foundPath = mazeMap[currentCell.x][currentCell.y + 1]
                        mapOfPaths[Direction.DOWN] = foundPath
                    }
                }

                //Checks left
                Direction.LEFT -> {
                    if (currentCell.x - 1 >= 0
                        && (mazeMap[currentCell.x - 1][currentCell.y].affiliation == Node.Corridor
                                || mazeMap[currentCell.x - 1][currentCell.y].affiliation == Node.End)
                        && currentCell.walls[Direction.LEFT] == false
                        && mazeMap[currentCell.x - 1][currentCell.y].walls[Direction.RIGHT] == false
                    ) {
                        foundPath = mazeMap[currentCell.x - 1][currentCell.y]
                        mapOfPaths[Direction.LEFT] = foundPath
                    }
                }
            }
        }
        return mapOfPaths.toMap()
    }

    /**
     * Function that can be called to slow down the execution of the algorithm
     * @param time Number of milliseconds to delay
     */
    suspend fun delayExecution(time: Long) {
        delay(time)
    }
}