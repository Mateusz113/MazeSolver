package uk.ac.aber.dcs.cs39440.maze_solver

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import uk.ac.aber.dcs.cs39440.maze_solver.ui.components.HorizontalPagerWrapper
import uk.ac.aber.dcs.cs39440.maze_solver.ui.components.ScreenSelectionTabs
import uk.ac.aber.dcs.cs39440.maze_solver.ui.navigation.Screen
import uk.ac.aber.dcs.cs39440.maze_solver.ui.theme.Maze_solverTheme


/**
 * Starter of the application
 */
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Maze_solverTheme {
                MainContent(viewModel = viewModel)
            }
        }
    }
}

/**
 * Composable that generates the content of an app
 * @param viewModel Viewmodel that is used across the application
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainContent(
    viewModel: MainViewModel
) {
    //List of screens for tabs and pager to use
    val screenList = listOf(
        Screen.Maze,
        Screen.Settings
    )
    //Used for navigation purposes
    val pagerState = rememberPagerState()

    //Wrapper around the application content
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (tabs, pageContent) = createRefs()

        //Top of the screen navigation tool and page reference
        ScreenSelectionTabs(
            screenList = screenList,
            pagerState = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(tabs) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                },
            reloadMaze = {
                viewModel.restoreSavedMaze()
            }
        )

        //Screen content rendered
        HorizontalPagerWrapper(
            viewModel = viewModel,
            screenList = screenList,
            pagerState = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(pageContent) {
                    top.linkTo(tabs.bottom)
                    start.linkTo(parent.start)
                }
        )
    }
}