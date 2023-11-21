package uk.ac.aber.dcs.cs39440.maze_solver.ui.maze

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.ui.platform.LocalConfiguration
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
import uk.ac.aber.dcs.cs39440.maze_solver.ui.main.MainViewModel
import uk.ac.aber.dcs.cs39440.maze_solver.R
import uk.ac.aber.dcs.cs39440.maze_solver.ui.components.MazeRender
import uk.ac.aber.dcs.cs39440.maze_solver.ui.components.SettingsText
import uk.ac.aber.dcs.cs39440.maze_solver.ui.theme.Maze_solverTheme
import uk.ac.aber.dcs.cs39440.maze_solver.util.algorithms.AStar
import uk.ac.aber.dcs.cs39440.maze_solver.util.algorithms.BFS
import uk.ac.aber.dcs.cs39440.maze_solver.util.algorithms.BidirectionalSearch
import uk.ac.aber.dcs.cs39440.maze_solver.util.algorithms.DFS
import uk.ac.aber.dcs.cs39440.maze_solver.util.algorithms.GreedySearch
import uk.ac.aber.dcs.cs39440.maze_solver.util.algorithms.Pathfinder
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.Algorithm
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.MazeInfo
import uk.ac.aber.dcs.cs39440.maze_solver.util.maze_map.Cell


/**
 * Maze screen composable
 * @param viewModel MainViewModel that holds the information about the maze and algorithm
 * @param isMazePageActive Information if the maze screen is active
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
    val algorithmInformation by viewModel.algorithmChosen.collectAsState()

    //State used to start the pathfinding
    var isPathfinding by remember { mutableStateOf(false) }
    //Stop algorithm state of the right button
    var stopActive by remember { mutableStateOf(false) }
    //Reload maze state of the right button
    var reloadActive by remember { mutableStateOf(false) }

    //Lifecycle owner to manage the states on recompositions
    var lifecycleOwner = LocalLifecycleOwner.current
    //Delay amount for algorithms
    val delayLength = 20L

    //Indicates if pathfinder finished finding the route
    var isPathfinderFinished by remember { mutableStateOf(false) }
    //Indicates if pathfinder finished finding the final route
    var isFinalPathFinished by remember { mutableStateOf(false) }
    //Holds the pathfinder instance
    var pathfinder: Pathfinder? by remember { mutableStateOf(null) }

    //Launches the correct pathfinding algorithm when the state is changed
    if (isPathfinding) {
        LaunchedEffect(key1 = isMazePageActive) {
            //Cancel the job on screen change
            if (!isMazePageActive) {
                this.cancel()
            }

            //Instantiate the pathfinder only if it is null
            if (pathfinder == null) {
                pathfinder = instantiatePathfinder(
                    algorithmInformation = algorithmInformation,
                    mazeInformation = mazeInformation,
                    mazeMap = mazeMap
                )
            }

            pathfinder?.let { pathfinder ->
                //Looks for the path
                while (!isPathfinderFinished) {
                    isPathfinderFinished = pathfinder.run(
                        mazeMap = mazeMap,
                        updateAffiliation = { cell, node ->
                            viewModel.updateIndexAffiliation(cell, node)
                        }
                    )
                    pathfinder.updateMazeIndicators(
                        updateAffiliation = { cell, node ->
                            viewModel.updateIndexAffiliation(cell, node)
                        }
                    )
                    pathfinder.delayExecution(delayLength)
                }

                //Draws the final path
                while (!isFinalPathFinished) {
                    isFinalPathFinished = pathfinder.getFinalPath(
                        updateAffiliation = { cell, node ->
                            viewModel.updateIndexAffiliation(cell, node)
                        }
                    )
                    pathfinder.updateMazeIndicators(
                        updateAffiliation = { cell, node ->
                            viewModel.updateIndexAffiliation(cell, node)
                        }
                    )
                    pathfinder.delayExecution(delayLength)
                }
            }

            //Reset the variables after the pathfinder finishes all the work
            withContext(Dispatchers.Main) {
                pathfinder = null
                stopActive = false
                reloadActive = true
            }
        }
    }


    //Restores the initial state on screen change
    LaunchedEffect(key1 = isMazePageActive) {
        if (!isMazePageActive) {
            viewModel.restoreSavedMaze()
            isPathfinding = false
            pathfinder = null
            stopActive = false
            reloadActive = false
            isPathfinderFinished = false
            isFinalPathFinished = false
        }
    }

    //Restores the initial state when the Composable is reloaded (e.g. screen orientation changes)
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                viewModel.restoreSavedMaze()
                isPathfinding = false
                pathfinder = null
                stopActive = false
                reloadActive = false
                isPathfinderFinished = false
                isFinalPathFinished = false
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val localConfig = LocalConfiguration.current
    var screenHeight = localConfig.screenHeightDp.dp
    var screenWidth = localConfig.screenWidthDp.dp

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
            algorithm = algorithmInformation,
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
                    centerHorizontallyTo(parent)
                }
                .height(
                    if (screenHeight > 600.dp) {
                        600.dp
                    } else {
                        screenHeight
                    }
                )
                .width(
                    if (screenWidth > 400.dp) {
                        400.dp
                    } else {
                        screenWidth
                    }
                )
        )


        //Start button - starts pathfinding onClick
        Button(
            onClick = {
                //Starts the pathfinding
                isPathfinding = true
                stopActive = true
                reloadActive = false
            },
            modifier = Modifier
                .constrainAs(startBtn) {
                    start.linkTo(parent.start)
                    top.linkTo(maze.bottom)
                }
                .padding(start = 50.dp, top = 20.dp)
                .width(120.dp)
                .heightIn(min = 20.dp, max = 300.dp),
            enabled = !isPathfinding
        ) {
            Text(text = stringResource(R.string.start))
        }

        //Stop button - stops the pathfinder and reloads the maze
        Button(
            onClick = {
                //Starts the reloading a maze from file
                if (stopActive) {
                    isPathfinding = false
                    stopActive = false
                    reloadActive = true
                } else if (reloadActive) {
                    viewModel.restoreSavedMaze()
                    pathfinder = null
                    reloadActive = false
                    isPathfinding = false
                    //Resetting the variables responsible for keeping the pathfinder running
                    isPathfinderFinished = false
                    isFinalPathFinished = false
                }
            },
            modifier = Modifier
                .constrainAs(stopBtn) {
                    end.linkTo(parent.end)
                    top.linkTo(maze.bottom)
                }
                .padding(end = 50.dp, top = 20.dp)
                .width(120.dp),
            enabled = reloadActive || stopActive
        ) {
            if (reloadActive) {
                Text(text = stringResource(R.string.reload))
            } else {
                Text(text = stringResource(R.string.stop))
            }
        }

        Spacer(modifier = Modifier.height(60.dp))
    }
}

//Instantiate teh correct pathfinder based on the selected algorithm
private fun instantiatePathfinder(
    algorithmInformation: Algorithm,
    mazeInformation: MazeInfo,
    mazeMap: MutableList<MutableList<Cell>>
): Pathfinder {
    val pathfinder: Pathfinder
    when (algorithmInformation) {
        Algorithm.BFS -> {
            pathfinder = BFS(
                mazeWidth = mazeInformation.size.first,
                mazeHeight = mazeInformation.size.second,
                startCell = mazeMap[0][0],
                endCell = mazeMap[mazeInformation.size.first - 1][mazeInformation.size.second - 1]
            )
        }

        Algorithm.DFS -> {
            pathfinder = DFS(
                mazeWidth = mazeInformation.size.first,
                mazeHeight = mazeInformation.size.second,
                startCell = mazeMap[0][0],
                endCell = mazeMap[mazeInformation.size.first - 1][mazeInformation.size.second - 1]
            )
        }

        Algorithm.BidirectionalBFS -> {
            pathfinder = BidirectionalSearch(
                mazeWidth = mazeInformation.size.first,
                mazeHeight = mazeInformation.size.second,
                entries = listOf(
                    mazeMap[0][0],
                    mazeMap[mazeInformation.size.first - 1][mazeInformation.size.second - 1]
                )
            )
        }

        Algorithm.Astar -> {
            pathfinder = AStar(
                mazeWidth = mazeInformation.size.first,
                mazeHeight = mazeInformation.size.second,
                startCell = mazeMap[0][0],
                endCell = mazeMap[mazeInformation.size.first - 1][mazeInformation.size.second - 1]
            )
        }

        Algorithm.GreedySearch -> {
            pathfinder = GreedySearch(
                mazeWidth = mazeInformation.size.first,
                mazeHeight = mazeInformation.size.second,
                startCell = mazeMap[0][0],
                endCell = mazeMap[mazeInformation.size.first - 1][mazeInformation.size.second - 1]
            )
        }
    }
    return pathfinder
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