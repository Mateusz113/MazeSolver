package uk.ac.aber.dcs.cs39440.maze_solver.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import uk.ac.aber.dcs.cs39440.maze_solver.ui.main.MainViewModel
import uk.ac.aber.dcs.cs39440.maze_solver.ui.maze.MazeScreen
import uk.ac.aber.dcs.cs39440.maze_solver.ui.navigation.Screen
import uk.ac.aber.dcs.cs39440.maze_solver.ui.settings.SettingsScreen
import uk.ac.aber.dcs.cs39440.maze_solver.ui.theme.Maze_solverTheme

/**
 * Wrapper around all the scrollable pages
 * @param viewModel MainViewModel instance that is passed to Composables
 * @param screenList List of screens in the pager
 * @param pagerState State of the pager that is controlling the scrolling
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun HorizontalPagerWrapper(
    viewModel: MainViewModel,
    screenList: List<Screen>,
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    //State of pages that will be passed to reset the maze
    val isMazePageActive by remember {
        derivedStateOf {
            pagerState.currentPage == 0
        }
    }
    HorizontalPager(
        count = screenList.size,
        state = pagerState,
        modifier = modifier
    ) { page ->
        when (page) {
            0 -> {
                MazeScreen(viewModel = viewModel, isMazePageActive = isMazePageActive)
            }

            1 -> {
                SettingsScreen(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Preview
@Composable
fun HorizontalPagerWrapperPreview() {
    Maze_solverTheme {
        val pagerState = rememberPagerState()
        val viewModel = MainViewModel()
        HorizontalPagerWrapper(
            viewModel = viewModel,
            screenList = listOf(Screen.Maze, Screen.Settings),
            pagerState = pagerState
        )
    }
}