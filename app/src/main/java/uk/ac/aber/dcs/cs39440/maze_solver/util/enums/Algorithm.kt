package uk.ac.aber.dcs.cs39440.maze_solver.util.enums

import uk.ac.aber.dcs.cs39440.maze_solver.R

/**
 * Enum classed used to identify algorithms used
 */
enum class Algorithm {
    BFS,
    DFS,
    BidirectionalBFS,
    Astar,
    GreedySearch
}

//Map of labels to display
val algorithmLabelsMap = mapOf(
    Algorithm.BFS to R.string.bfs,
    Algorithm.DFS to R.string.dfs,
    Algorithm.BidirectionalBFS to R.string.bidirectional_bfs,
    Algorithm.Astar to R.string.a_star,
    Algorithm.GreedySearch to R.string.greedy_search
)