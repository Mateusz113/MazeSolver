package uk.ac.aber.dcs.cs39440.maze_solver.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.*
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs39440.maze_solver.ui.navigation.Screen
import uk.ac.aber.dcs.cs39440.maze_solver.ui.theme.Maze_solverTheme
import uk.ac.aber.dcs.cs39440.maze_solver.ui.theme.md_theme_light_onSurfaceVariant
import uk.ac.aber.dcs.cs39440.maze_solver.ui.theme.md_theme_light_primary
import uk.ac.aber.dcs.cs39440.maze_solver.ui.theme.typography

/**
 * Tabs that sort as a navigation throughout the app
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun ScreenSelectionTabs(
    screenList: List<Screen>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    reloadMaze: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = Color.Transparent,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier
                    .pagerTabIndicatorOffset(
                        pagerState = pagerState,
                        tabPositions = tabPositions
                    )
            )
        },
        modifier = modifier
    ) {
        screenList.forEachIndexed { index, screen ->
            Tab(
                selected = pagerState.currentPage == screenList.indexOf(screen),
                onClick = {
                    if (pagerState.currentPage != index) {
                        coroutineScope.launch(Dispatchers.Main) {
                            pagerState.animateScrollToPage(page = index)
                            if (index == 0) {
                                reloadMaze()
                            }
                        }
                    }
                },
                modifier = Modifier.clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = {}
                ),
                text = {
                    if (pagerState.currentPage == screenList.indexOf(screen)) {
                        Text(
                            text = stringResource(id = screenList[index].label),
                            color = md_theme_light_primary,
                            fontStyle = typography.titleSmall.fontStyle
                        )
                    } else {
                        Text(
                            text = stringResource(id = screenList[index].label),
                            color = md_theme_light_onSurfaceVariant,
                            fontStyle = typography.titleSmall.fontStyle
                        )
                    }
                }
            )
        }

    }
}

@OptIn(ExperimentalPagerApi::class)
@Preview
@Composable
private fun ScreenSelectionTabPreview() {
    Maze_solverTheme {
        val pagerState = rememberPagerState()
        ScreenSelectionTabs(
            screenList = listOf(Screen.Maze, Screen.Settings),
            pagerState = pagerState,
            reloadMaze = {}
        )
    }
}