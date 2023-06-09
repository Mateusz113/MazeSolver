package uk.ac.aber.dcs.cs39440.maze_solver.util.maze_map

import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.Direction


/**
 * Helper function for maze generators that returns the map of unvisited neighbours of given cell
 * @param currentCell Cell to check for unvisited neighbours
 * @param mazeMap Maze map representation
 * @param mazeWidth Width of maze
 * @param mazeHeight Height of maze
 * @return Map of unvisited cells to the direction relative to the current cell
 */
fun getUnvisitedNeighbours(
    currentCell: Cell,
    mazeMap: MutableList<MutableList<Cell>>,
    mazeWidth: Int,
    mazeHeight: Int
): MutableMap<Cell, Direction> {
    val mapToReturn = mutableMapOf<Cell, Direction>()
    for (direction in Direction.values()) {
        when (direction) {
            Direction.TOP -> {
                if (currentCell.y - 1 >= 0 && !mazeMap[currentCell.x][currentCell.y - 1].visited) {
                    mapToReturn[mazeMap[currentCell.x][currentCell.y - 1]] = Direction.TOP
                }
            }

            Direction.RIGHT -> {
                if (currentCell.x + 1 < mazeWidth && !mazeMap[currentCell.x + 1][currentCell.y].visited) {
                    mapToReturn[mazeMap[currentCell.x + 1][currentCell.y]] = Direction.RIGHT
                }
            }

            Direction.DOWN -> {
                if (currentCell.y + 1 < mazeHeight && !mazeMap[currentCell.x][currentCell.y + 1].visited) {
                    mapToReturn[mazeMap[currentCell.x][currentCell.y + 1]] = Direction.DOWN
                }

            }

            Direction.LEFT -> {
                if (currentCell.x - 1 >= 0 && !mazeMap[currentCell.x - 1][currentCell.y].visited) {
                    mapToReturn[mazeMap[currentCell.x - 1][currentCell.y]] = Direction.LEFT
                }
            }
        }
    }
    return mapToReturn
}