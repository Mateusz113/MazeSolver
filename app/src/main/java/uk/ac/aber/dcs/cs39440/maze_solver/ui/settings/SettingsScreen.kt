package uk.ac.aber.dcs.cs39440.maze_solver.ui.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import uk.ac.aber.dcs.cs39440.maze_solver.MainViewModel
import uk.ac.aber.dcs.cs39440.maze_solver.ui.components.RadioButtonsGenerator
import uk.ac.aber.dcs.cs39440.maze_solver.ui.theme.Maze_solverTheme
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.Algorithm
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.MazeGenerator
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.MazeInfo

/**
 * Settings screen of the application
 * @param viewModel MainViewModel that holds the information about the maze and algorithm
 */
@Composable
fun SettingsScreen(
    viewModel: MainViewModel
) {
    //Variables to pass to lower composable
    val currentAlgorithm by viewModel.algorithmChosen.collectAsState()
    val currentMazeInfo by viewModel.mazeInformation.collectAsState()
    val currentMazeGenerator by viewModel.mazeGenerator.collectAsState()

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        //Creating refs for items
        val (algorithmButtons, mazeGeneratorButtons, mazeButtons) = createRefs()

        //Algorithm selection buttons
        RadioButtonsGenerator(
            modifier = Modifier
                .constrainAs(algorithmButtons) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
                .padding(top = 20.dp),
            optionSelection = { algorithm ->
                if (currentAlgorithm != algorithm) {
                    viewModel.changeAlgorithm(algorithm as Algorithm)
                }
            },
            currentlySelectedOption = currentAlgorithm,
            listOfOptions = Algorithm.values().toList()
        )

        //Maze generator selection buttons
        RadioButtonsGenerator(
            modifier = Modifier
                .constrainAs(mazeGeneratorButtons) {
                    start.linkTo(parent.start)
                    top.linkTo(algorithmButtons.bottom)
                }
                .padding(top = 20.dp),
            optionSelection = { mazeGenerator ->
                if (currentMazeGenerator != mazeGenerator) {
                    viewModel.changeMazeGenerator(mazeGenerator as MazeGenerator)
                    viewModel.generateMaze()
                }
            },
            currentlySelectedOption = currentMazeGenerator,
            listOfOptions = MazeGenerator.values().toList()
        )

        //Maze info selection buttons
        RadioButtonsGenerator(
            modifier = Modifier
                .constrainAs(mazeButtons) {
                    start.linkTo(parent.start)
                    top.linkTo(mazeGeneratorButtons.bottom)
                }
                .padding(top = 20.dp),
            optionSelection = { mazeInfo ->
                if (currentMazeInfo != mazeInfo) {
                    viewModel.changeMazeInformation(mazeInfo as MazeInfo)
                    viewModel.generateMaze()
                }
            },
            currentlySelectedOption = currentMazeInfo,
            listOfOptions = MazeInfo.values().toList()
        )
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    Maze_solverTheme {
        val viewModel: MainViewModel by viewModel()
        SettingsScreen(
            viewModel = viewModel
        )
    }
}