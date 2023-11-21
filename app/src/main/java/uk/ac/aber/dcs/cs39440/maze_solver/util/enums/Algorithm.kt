package uk.ac.aber.dcs.cs39440.maze_solver.util.enums

import uk.ac.aber.dcs.cs39440.maze_solver.R

/**
 * Enum classed used to identify pathfinding_algorithms used
 */
enum class Algorithm {
    BFS,
    DFS,
    BidirectionalBFS,
    Astar,
    GreedySearch
}

val algorithmLabelsMap = mapOf(
    Algorithm.BFS to R.string.bfs,
    Algorithm.DFS to R.string.dfs,
    Algorithm.BidirectionalBFS to R.string.bidirectional_bfs,
    Algorithm.Astar to R.string.a_star,
    Algorithm.GreedySearch to R.string.greedy_search
)

val algorithmDescriptionMap = mapOf(
    Algorithm.BFS to R.string.bfs_desc,
    Algorithm.DFS to R.string.dfs_desc,
    Algorithm.BidirectionalBFS to R.string.bidirectional_bfs_desc,
    Algorithm.Astar to R.string.a_star_desc,
    Algorithm.GreedySearch to R.string.greedy_search_desc
)