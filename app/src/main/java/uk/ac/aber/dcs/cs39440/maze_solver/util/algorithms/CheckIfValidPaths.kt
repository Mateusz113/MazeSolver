package uk.ac.aber.dcs.cs39440.maze_solver.util.algorithms

import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.Direction
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.Node
import uk.ac.aber.dcs.cs39440.maze_solver.util.maze_map.Cell

/**
 * Functions that checks for a valid paths in the maze
 * @param mazeWidth Width of the maze that it searches in
 * @param currentCell Cell that is checked for available neighbours
 * @param mazeHeight Height of the maze that it searches in
 * @param mazeMap Map of the maze
 */
fun checkForValidPaths(
    mazeMap: MutableList<MutableList<Cell>>,
    currentCell: Cell,
    mazeWidth: Int,
    mazeHeight: Int,
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