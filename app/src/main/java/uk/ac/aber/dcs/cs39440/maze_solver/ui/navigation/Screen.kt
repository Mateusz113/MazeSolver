package uk.ac.aber.dcs.cs39440.maze_solver.ui.navigation

import androidx.compose.runtime.Composable
import uk.ac.aber.dcs.cs39440.maze_solver.MainViewModel
import uk.ac.aber.dcs.cs39440.maze_solver.R
import uk.ac.aber.dcs.cs39440.maze_solver.ui.maze.MazeScreen
import uk.ac.aber.dcs.cs39440.maze_solver.ui.settings.SettingsScreen

/**
 * Class that represents one screen
 */
sealed class Screen(
    val label: Int
) {
    object Maze : Screen(
        label = R.string.maze
    )

    object Settings : Screen(
        label = R.string.settings
    )
}