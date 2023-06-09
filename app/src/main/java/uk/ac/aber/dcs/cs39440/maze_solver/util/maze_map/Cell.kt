package uk.ac.aber.dcs.cs39440.maze_solver.util.maze_map

import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.Direction
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.Node

/**
 * Data class that works as a maze element
 * @param x x position in maze
 * @param y y position in maze
 * @param visited used in the generation process to distinguish unvisited cells
 * @param affiliation cell role in the maze
 * @param walls walls that the cells has present
 */
data class Cell(
    val x: Int,
    val y: Int,
    val visited: Boolean = false,
    val affiliation: Node = Node.Corridor,
    val walls: MutableMap<Direction, Boolean> = mutableMapOf(
        Direction.TOP to true,
        Direction.RIGHT to true,
        Direction.DOWN to true,
        Direction.LEFT to true
    )
)