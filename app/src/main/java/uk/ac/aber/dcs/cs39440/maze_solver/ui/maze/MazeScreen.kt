package uk.ac.aber.dcs.cs39440.maze_solver.ui.maze

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.withContext
import uk.ac.aber.dcs.cs39440.maze_solver.MainViewModel
import uk.ac.aber.dcs.cs39440.maze_solver.R
import uk.ac.aber.dcs.cs39440.maze_solver.ui.components.MazeRender
import uk.ac.aber.dcs.cs39440.maze_solver.ui.components.SettingsText
import uk.ac.aber.dcs.cs39440.maze_solver.ui.theme.Maze_solverTheme
import uk.ac.aber.dcs.cs39440.maze_solver.util.algorithms.aStar
import uk.ac.aber.dcs.cs39440.maze_solver.util.algorithms.bfs
import uk.ac.aber.dcs.cs39440.maze_solver.util.algorithms.bidirectionalSearch
import uk.ac.aber.dcs.cs39440.maze_solver.util.algorithms.dfs
import uk.ac.aber.dcs.cs39440.maze_solver.util.algorithms.greedySearch
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.Algorithm


/**
 * Maze screen composable
 * @param viewModel MainViewModel that holds the information about the maze and algorithm
 */
@Composable
fun MazeScreen(
    viewModel: MainViewModel,
    isMazePageActive: Boolean
) {
    //State of the maze map representation
    val mazeMap by viewModel.mazeMap.collectAsState()
    //State from viewModel that represents the current maze information
    val mazeInformation by viewModel.mazeInformation.collectAsState()
    //State from viewModel that represents the current algorithm information
    val algorithm by viewModel.algorithmChosen.collectAsState()

    //State used to start the pathfinding
    var isPathfinding by remember { mutableStateOf(false) }
    //State used to start the reload of maze from the file
    var isLoading by remember { mutableStateOf(false) }
    //State of reload button
    var canReload by remember { mutableStateOf(false) }
    //State of start button
    var canPathfind by remember { mutableStateOf(true) }
    //Lifecycle owner to manage the states on recompositions
    var lifecycleOwner = LocalLifecycleOwner.current
    //Delay amount for algorithms
    val delayLength = 40L

    //Launches the correct pathfinding algorithm when the state is changed
    if (isPathfinding) {
        LaunchedEffect(key1 = isMazePageActive) {
            if (!isMazePageActive) {
                this.cancel()
            }
            canPathfind = false
            when (algorithm) {
                Algorithm.BFS -> {
                    bfs(
                        mazeWidth = mazeInformation.size.first,
                        mazeHeight = mazeInformation.size.second,
                        mazeMap = mazeMap,
                        startX = 0,
                        startY = 0,
                        endX = mazeInformation.size.first - 1,
                        endY = mazeInformation.size.second - 1,
                        updateAffiliation = { cell, node ->
                            viewModel.updateIndexAffiliation(
                                currentCell = cell,
                                newType = node
                            )
                        },
                        delayLength = delayLength
                    )
                }

                Algorithm.DFS -> {
                    dfs(
                        mazeWidth = mazeInformation.size.first,
                        mazeHeight = mazeInformation.size.second,
                        mazeMap = mazeMap,
                        startX = 0,
                        startY = 0,
                        endX = mazeInformation.size.first - 1,
                        endY = mazeInformation.size.second - 1,
                        updateAffiliation = { cell, node ->
                            viewModel.updateIndexAffiliation(
                                currentCell = cell,
                                newType = node
                            )
                        },
                        delayLength = delayLength
                    )
                }

                Algorithm.BidirectionalBFS -> {
                    bidirectionalSearch(
                        mazeWidth = mazeInformation.size.first,
                        mazeHeight = mazeInformation.size.second,
                        mazeMap = mazeMap,
                        startX = 0,
                        startY = 0,
                        endX = mazeInformation.size.first - 1,
                        endY = mazeInformation.size.second - 1,
                        updateAffiliation = { cell, node ->
                            viewModel.updateIndexAffiliation(
                                currentCell = cell,
                                newType = node
                            )
                        },
                        delayLength = delayLength
                    )
                }

                Algorithm.Astar -> {
                    aStar(
                        mazeWidth = mazeInformation.size.first,
                        mazeHeight = mazeInformation.size.second,
                        mazeMap = mazeMap,
                        startX = 0,
                        startY = 0,
                        endX = mazeInformation.size.first - 1,
                        endY = mazeInformation.size.second - 1,
                        updateAffiliation = { cell, node ->
                            viewModel.updateIndexAffiliation(
                                currentCell = cell,
                                newType = node
                            )
                        },
                        delayLength = delayLength
                    )
                }

                Algorithm.GreedySearch -> {
                    greedySearch(
                        mazeWidth = mazeInformation.size.first,
                        mazeHeight = mazeInformation.size.second,
                        mazeMap = mazeMap,
                        startX = 0,
                        startY = 0,
                        endX = mazeInformation.size.first - 1,
                        endY = mazeInformation.size.second - 1,
                        updateAffiliation = { cell, node ->
                            viewModel.updateIndexAffiliation(
                                currentCell = cell,
                                newType = node
                            )
                        },
                        delayLength = delayLength
                    )
                }
            }
            withContext(Dispatchers.Main) {
                isPathfinding = false
                canReload = true
            }
        }
    }

    //Reloads the maze
    if (isLoading) {
        viewModel.restoreSavedMaze()
        isLoading = false
        canReload = false
        canPathfind = true
    }

    //Restores the state when the algorithm is not running or complete
    LaunchedEffect(key1 = isMazePageActive) {
        if (!isMazePageActive) {
            viewModel.restoreSavedMaze()
            isPathfinding = false
            canReload = false
            canPathfind = true
        }
    }

    //Restores the initial state when the Composable is reloaded
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                viewModel.restoreSavedMaze()
                isPathfinding = false
                canPathfind = true
                canReload = false
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    //Wrapper function for all the items on the page
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        //Refs for constraint layout definitions
        val (settingsText, maze, startBtn, stopBtn) = createRefs()

        //Displays the current chosen algorithm as well as maze size
        SettingsText(
            algorithm = algorithm,
            mazeInfo = mazeInformation,
            mazeGenerator = viewModel.mazeGenerator.collectAsState().value,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(settingsText) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .padding(top = 5.dp, bottom = 20.dp)
        )

        //Maze rendering
        MazeRender(
            mazeMap = mazeMap,
            mazeInfo = mazeInformation,
            modifier = Modifier
                .constrainAs(maze) {
                    top.linkTo(settingsText.bottom)
                }
                .height(600.dp)
                .fillMaxWidth()
        )


        //Start button - starts pathfinding onClick
        Button(
            onClick = {
                //Starts the pathfinding
                isPathfinding = true
            },
            modifier = Modifier
                .constrainAs(startBtn) {
                    start.linkTo(parent.start)
                    top.linkTo(maze.bottom)
                }
                .padding(start = 50.dp, top = 20.dp)
                .width(120.dp),
            enabled = canPathfind
        ) {
            Text(text = stringResource(R.string.start))
        }

        //Stop button - reloads the maze onClick
        Button(
            onClick = {
                //Starts the reloading a maze from file
                isLoading = true
            },
            modifier = Modifier
                .constrainAs(stopBtn) {
                    end.linkTo(parent.end)
                    top.linkTo(maze.bottom)
                }
                .padding(end = 50.dp, top = 20.dp)
                .width(120.dp),
            enabled = canReload
        ) {
            Text(text = stringResource(R.string.reload))
        }
    }
}


@Preview
@Composable
private fun MazeScreenPreview() {
    Maze_solverTheme {
        val viewModel: MainViewModel by viewModel()
        MazeScreen(
            viewModel = viewModel,
            isMazePageActive = true
        )
    }
}