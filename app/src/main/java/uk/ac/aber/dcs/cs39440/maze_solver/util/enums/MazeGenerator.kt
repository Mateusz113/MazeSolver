package uk.ac.aber.dcs.cs39440.maze_solver.util.enums

import uk.ac.aber.dcs.cs39440.maze_solver.R

/**
 * Enum of the implemented maze generators
 */
enum class MazeGenerator {
    RandomizedDFS, Prims
}

//Map of labels to display
val mazeGeneratorLabelsMap = mapOf(
    MazeGenerator.RandomizedDFS to R.string.randomized_dfs,
    MazeGenerator.Prims to R.string.prims
)