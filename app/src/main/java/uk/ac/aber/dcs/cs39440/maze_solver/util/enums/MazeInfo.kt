package uk.ac.aber.dcs.cs39440.maze_solver.util.enums

import uk.ac.aber.dcs.cs39440.maze_solver.R

/**
 * Enum class used to store the information about the maze
 * @param size Size of the maze
 * @param textFileName Name of the text file to read from
 */
enum class MazeInfo(val size: Pair<Int, Int>) {
    Small(Pair(20, 30)),
    Medium(Pair(30, 45)),
    Large(Pair(40, 60));
}

//Map of labels to display
val mazeInfoLabelsMap = mapOf(
    MazeInfo.Small to R.string.twenty_by_thirty,
    MazeInfo.Medium to R.string.thirty_by_fourtyfive,
    MazeInfo.Large to R.string.fourty_by_sixty
)