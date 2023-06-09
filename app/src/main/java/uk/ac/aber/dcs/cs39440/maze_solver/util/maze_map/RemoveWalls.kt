package uk.ac.aber.dcs.cs39440.maze_solver.util.maze_map

import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.Direction


/**
 * Helper function for maze generators that removes the walls between two cells
 * @param currentCell Currently considered cell
 * @param nextCell Cell that is a neighbour of current cell
 * @param directionToRemove Direction of nextCell from currentCell
 * @param mazeMap Maze map representation
 */
fun removeWalls(
    currentCell: Cell,
    nextCell: Cell,
    directionToRemove: Direction,
    mazeMap: MutableList<MutableList<Cell>>
) {
    //Remove the walls between the current and next node
    when (directionToRemove) {
        Direction.TOP -> {
            mazeMap[currentCell.x][currentCell.y] =
                mazeMap[currentCell.x][currentCell.y].copy(walls = mazeMap[currentCell.x][currentCell.y].walls.apply {
                    this[Direction.TOP] = false
                })
            mazeMap[nextCell.x][nextCell.y] =
                mazeMap[nextCell.x][nextCell.y].copy(walls = mazeMap[nextCell.x][nextCell.y].walls.apply {
                    this[Direction.DOWN] = false
                })
        }

        Direction.RIGHT -> {
            mazeMap[currentCell.x][currentCell.y] =
                mazeMap[currentCell.x][currentCell.y].copy(walls = mazeMap[currentCell.x][currentCell.y].walls.apply {
                    this[Direction.RIGHT] = false
                })
            mazeMap[nextCell.x][nextCell.y] =
                mazeMap[nextCell.x][nextCell.y].copy(walls = mazeMap[nextCell.x][nextCell.y].walls.apply {
                    this[Direction.LEFT] = false
                })
        }

        Direction.DOWN -> {
            mazeMap[currentCell.x][currentCell.y] =
                mazeMap[currentCell.x][currentCell.y].copy(walls = mazeMap[currentCell.x][currentCell.y].walls.apply {
                    this[Direction.DOWN] = false
                })
            mazeMap[nextCell.x][nextCell.y] =
                mazeMap[nextCell.x][nextCell.y].copy(walls = mazeMap[nextCell.x][nextCell.y].walls.apply {
                    this[Direction.TOP] = false
                })
        }

        Direction.LEFT -> {
            mazeMap[currentCell.x][currentCell.y] =
                mazeMap[currentCell.x][currentCell.y].copy(walls = mazeMap[currentCell.x][currentCell.y].walls.apply {
                    this[Direction.LEFT] = false
                })
            mazeMap[nextCell.x][nextCell.y] =
                mazeMap[nextCell.x][nextCell.y].copy(walls = mazeMap[nextCell.x][nextCell.y].walls.apply {
                    this[Direction.RIGHT] = false
                })
        }
    }
}

