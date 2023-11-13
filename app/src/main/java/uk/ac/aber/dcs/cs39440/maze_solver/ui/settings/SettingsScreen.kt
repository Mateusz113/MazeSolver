package uk.ac.aber.dcs.cs39440.maze_solver.ui.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import uk.ac.aber.dcs.cs39440.maze_solver.MainViewModel
import uk.ac.aber.dcs.cs39440.maze_solver.ui.theme.Maze_solverTheme
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.Algorithm
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.MazeGenerator
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.MazeInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import uk.ac.aber.dcs.cs39440.maze_solver.R
import uk.ac.aber.dcs.cs39440.maze_solver.ui.components.OptionSelectionDialog
import uk.ac.aber.dcs.cs39440.maze_solver.ui.components.SettingsOption
import uk.ac.aber.dcs.cs39440.maze_solver.ui.theme.typography
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.algorithmLabelsMap
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.mazeGeneratorLabelsMap
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.mazeInfoLabelsMap

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

    var isAlgorithmSelectionDialogOpen by rememberSaveable {
        mutableStateOf(false)
    }
    var isAlgorithmDescDialogOpen by rememberSaveable {
        mutableStateOf(false)
    }
    var isGenerationAlgorithmSelectionDialogOpen by rememberSaveable {
        mutableStateOf(false)
    }
    var isGenerationAlgorithmDescDialogOpen by rememberSaveable {
        mutableStateOf(false)
    }
    var isMazeSizeSelectionDialogOpen by rememberSaveable {
        mutableStateOf(false)
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        //Creating refs for items
        val (
            algorithmSection,
            mazeGenSection,
            mazeSizeSection
        ) = createRefs()

        PathfindingAlgorithmSection(
            modifier = Modifier
                .constrainAs(algorithmSection) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
                .padding(top = 10.dp),
            currentAlgorithm = currentAlgorithm,
            isDialogOpen = isAlgorithmSelectionDialogOpen,
            dialogOpen = { isDialogOpen ->
                isAlgorithmSelectionDialogOpen = isDialogOpen
            },
            algorithmSelection = { algorithm ->
                if (currentAlgorithm != algorithm) {
                    viewModel.changeAlgorithm(algorithm)
                }
            }
        )

        MazeGeneratingAlgorithmSection(
            modifier = Modifier
                .constrainAs(mazeGenSection) {
                    start.linkTo(parent.start)
                    top.linkTo(algorithmSection.bottom)
                }
                .padding(top = 10.dp),
            currentMazeGenAlgorithm = currentMazeGenerator,
            isDialogOpen = isGenerationAlgorithmSelectionDialogOpen,
            dialogOpen = { isDialogOpen ->
                isGenerationAlgorithmSelectionDialogOpen = isDialogOpen
            },
            mazeGenAlgorithmSelection = { algorithm ->
                if (currentMazeGenerator != algorithm) {
                    viewModel.changeMazeGenerator(algorithm)
                }
            }
        )

        MazeSizeSection(
            modifier = Modifier
                .constrainAs(mazeSizeSection) {
                    start.linkTo(parent.start)
                    top.linkTo(mazeGenSection.bottom)
                }
                .padding(top = 10.dp),
            currentMazeSize = currentMazeInfo,
            mazeSizeSelection = { mazeInfo ->
                if (mazeInfo != currentMazeInfo) {
                    viewModel.changeMazeInformation(mazeInfo)
                }
            },
            isDialogOpen = isMazeSizeSelectionDialogOpen,
            dialogOpen = { isOpen ->
                isMazeSizeSelectionDialogOpen = isOpen
            }
        )
    }
}

@Composable
fun PathfindingAlgorithmSection(
    modifier: Modifier,
    currentAlgorithm: Algorithm,
    isDialogOpen: Boolean,
    dialogOpen: (Boolean) -> Unit,
    algorithmSelection: (Algorithm) -> Unit,
) {
    ConstraintLayout(
        modifier = modifier
    ) {
        val (algorithmLabelText, algorithmSelect, algorithmDesc) = createRefs()
        Text(
            text = stringResource(R.string.pathfinding_algorithm),
            textAlign = TextAlign.Start,
            style = TextStyle(
                fontSize = typography.headlineMedium.fontSize,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding(start = 10.dp)
                .fillMaxWidth()
                .constrainAs(algorithmLabelText) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
        )

        SettingsOption(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(algorithmSelect) {
                    top.linkTo(algorithmLabelText.bottom)
                    start.linkTo(parent.start)
                }
                .padding(top = 10.dp),
            optionDescription = stringResource(id = R.string.current_pathfinding_algorithm),
            labels = algorithmLabelsMap,
            currentlySelectedOption = currentAlgorithm,
            dialogOpen = { isOpen ->
                dialogOpen(isOpen)
            }
        )
    }

    OptionSelectionDialog(
        listOfOptions = Algorithm.values().toList(),
        labels = algorithmLabelsMap,
        currentlySelectedOption = currentAlgorithm,
        optionSelection = { algorithm ->
            algorithmSelection(algorithm)
        },
        isDialogOpen = isDialogOpen,
        dialogOpen = { isOpen ->
            dialogOpen(isOpen)
        }
    )
}

@Composable
fun MazeGeneratingAlgorithmSection(
    modifier: Modifier,
    currentMazeGenAlgorithm: MazeGenerator,
    mazeGenAlgorithmSelection: (MazeGenerator) -> Unit,
    isDialogOpen: Boolean,
    dialogOpen: (Boolean) -> Unit,
) {
    ConstraintLayout(
        modifier = modifier
    ) {
        val (mazeGenAlgorithmLabelText, mazeGenAlgorithmSelect, algorithmDesc) = createRefs()
        Text(
            text = stringResource(R.string.maze_gen_algorithm),
            textAlign = TextAlign.Start,
            style = TextStyle(
                fontSize = typography.headlineMedium.fontSize,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding(start = 10.dp)
                .fillMaxWidth()
                .constrainAs(mazeGenAlgorithmLabelText) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
        )

        SettingsOption(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(mazeGenAlgorithmSelect) {
                    top.linkTo(mazeGenAlgorithmLabelText.bottom)
                    start.linkTo(parent.start)
                }
                .padding(top = 10.dp),
            optionDescription = stringResource(id = R.string.current_maze_gen_algorithm),
            labels = mazeGeneratorLabelsMap,
            currentlySelectedOption = currentMazeGenAlgorithm,
            dialogOpen = { isOpen ->
                dialogOpen(isOpen)
            }
        )
    }

    OptionSelectionDialog(
        listOfOptions = MazeGenerator.values().toList(),
        labels = mazeGeneratorLabelsMap,
        currentlySelectedOption = currentMazeGenAlgorithm,
        optionSelection = { option ->
            mazeGenAlgorithmSelection(option)
        },
        isDialogOpen = isDialogOpen,
        dialogOpen = { isOpen ->
            dialogOpen(isOpen)
        }
    )
}

@Composable
fun MazeSizeSection(
    modifier: Modifier,
    currentMazeSize: MazeInfo,
    mazeSizeSelection: (MazeInfo) -> Unit,
    isDialogOpen: Boolean,
    dialogOpen: (Boolean) -> Unit,
) {
    ConstraintLayout(
        modifier = modifier
    ) {
        val (mazeSizeLabelText, mazeSizeSelect) = createRefs()
        Text(
            text = stringResource(R.string.maze_size),
            textAlign = TextAlign.Start,
            style = TextStyle(
                fontSize = typography.headlineMedium.fontSize,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding(start = 10.dp)
                .fillMaxWidth()
                .constrainAs(mazeSizeLabelText) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
        )

        SettingsOption(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(mazeSizeSelect) {
                    top.linkTo(mazeSizeLabelText.bottom)
                    start.linkTo(parent.start)
                }
                .padding(top = 10.dp),
            optionDescription = stringResource(id = R.string.current_maze_size),
            labels = mazeInfoLabelsMap,
            currentlySelectedOption = currentMazeSize,
            dialogOpen = { isOpen ->
                dialogOpen(isOpen)
            }
        )
    }

    OptionSelectionDialog(
        listOfOptions = MazeInfo.values().toList(),
        labels = mazeInfoLabelsMap,
        currentlySelectedOption = currentMazeSize,
        optionSelection = { option ->
            mazeSizeSelection(option)
        },
        isDialogOpen = isDialogOpen,
        dialogOpen = { isOpen ->
            dialogOpen(isOpen)
        }
    )
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