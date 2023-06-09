package uk.ac.aber.dcs.cs39440.maze_solver.util.enums

import androidx.compose.ui.graphics.Color

/**
 * Class that works as an element of the maze
 * @param color Color that a node should be rendered to
 */
enum class Node(val color: Color) {
    Wall(Color.Black),
    Corridor(Color.White),
    Considered(Color.Blue),
    Queued(Color.Cyan),
    FinalPath(Color.Red),
    Start(Color.Green),
    End(Color.Yellow)
}