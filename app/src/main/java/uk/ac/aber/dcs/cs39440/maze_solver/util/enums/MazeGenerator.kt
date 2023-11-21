package uk.ac.aber.dcs.cs39440.maze_solver.util.enums

import uk.ac.aber.dcs.cs39440.maze_solver.R

/**
 * Enum of the implemented maze generators
 */
enum class MazeGenerator {
    RandomizedDFS, Prims
}

val mazeGeneratorLabelsMap = mapOf(
    MazeGenerator.RandomizedDFS to R.string.randomized_dfs,
    MazeGenerator.Prims to R.string.prims
)

val mazeGeneratorDescriptionMap = mapOf(
    MazeGenerator.RandomizedDFS to R.string.randomized_dfs_desc,
    MazeGenerator.Prims to R.string.prims_desc
)