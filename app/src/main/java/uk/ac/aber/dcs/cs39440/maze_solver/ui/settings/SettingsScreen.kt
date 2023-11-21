package uk.ac.aber.dcs.cs39440.maze_solver.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import uk.ac.aber.dcs.cs39440.maze_solver.ui.main.MainViewModel
import uk.ac.aber.dcs.cs39440.maze_solver.R
import uk.ac.aber.dcs.cs39440.maze_solver.ui.components.OptionSelectionDialog
import uk.ac.aber.dcs.cs39440.maze_solver.ui.components.RadioButtonsGenerator
import uk.ac.aber.dcs.cs39440.maze_solver.ui.components.SettingsOption
import uk.ac.aber.dcs.cs39440.maze_solver.ui.theme.Maze_solverTheme
import uk.ac.aber.dcs.cs39440.maze_solver.ui.theme.typography
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.Algorithm
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.MazeGenerator
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.MazeInfo
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.algorithmDescriptionMap
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.algorithmLabelsMap
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.mazeGeneratorDescriptionMap
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 20.dp)
    ) {
        PathfindingAlgorithmSection(
            modifier = Modifier
                .padding(top = 10.dp),
            currentAlgorithm = currentAlgorithm,
            isOptionDialogOpen = isAlgorithmSelectionDialogOpen,
            optionDialogOpen = { isOpen ->
                isAlgorithmSelectionDialogOpen = isOpen
            },
            isDescDialogOpen = isAlgorithmDescDialogOpen,
            descDialogOpen = { isOpen ->
                isAlgorithmDescDialogOpen = isOpen
            },
            algorithmSelection = { algorithm ->
                if (currentAlgorithm != algorithm) {
                    viewModel.changeAlgorithm(algorithm)
                }
            }
        )

        Divider(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(vertical = 20.dp)
                .align(Alignment.CenterHorizontally)
        )

        MazeGeneratingAlgorithmSection(
            modifier = Modifier,
            currentMazeGenAlgorithm = currentMazeGenerator,
            isSelectDialogOpen = isGenerationAlgorithmSelectionDialogOpen,
            selectDialogOpen = { isOpen ->
                isGenerationAlgorithmSelectionDialogOpen = isOpen
            },
            isDescDialogOpen = isGenerationAlgorithmDescDialogOpen,
            descDialogOpen = { isOpen ->
                isGenerationAlgorithmDescDialogOpen = isOpen
            },
            mazeGenAlgorithmSelection = { algorithm ->
                if (currentMazeGenerator != algorithm) {
                    viewModel.changeMazeGenerator(algorithm)
                }
            }
        )

        Divider(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(vertical = 20.dp)
                .align(Alignment.CenterHorizontally)
        )

        MazeSizeSection(
            modifier = Modifier,
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
    isOptionDialogOpen: Boolean,
    optionDialogOpen: (Boolean) -> Unit,
    isDescDialogOpen: Boolean,
    descDialogOpen: (Boolean) -> Unit,
    algorithmSelection: (Algorithm) -> Unit,
) {
    Column(
        modifier = modifier
    ) {
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
        )

        SettingsOption(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            optionDescription = stringResource(id = R.string.current_pathfinding_algorithm),
            currentlySelectedOption = stringResource(id = algorithmLabelsMap[currentAlgorithm]!!),
            dialogOpen = { isOpen ->
                optionDialogOpen(isOpen)
            }
        )

        SettingsOption(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
            optionDescription = stringResource(id = R.string.pathfinder_desc),
            dialogOpen = { isOpen ->
                descDialogOpen(isOpen)
            }
        )
    }
    //Dialog that pops up when the user clicked on algorithm selection
    OptionSelectionDialog(
        dialogLabel = stringResource(id = R.string.pathfinding_algorithm),
        isDialogOpen = isOptionDialogOpen,
        dialogOpen = { isOpen ->
            optionDialogOpen(isOpen)
        }
    ) {
        RadioButtonsGenerator(
            modifier = Modifier,
            listOfOptions = Algorithm.values().toList(),
            labels = algorithmLabelsMap,
            currentlySelectedOption = currentAlgorithm,
            optionSelection = { newMazeSolver ->
                if (currentAlgorithm != newMazeSolver) {
                    algorithmSelection(newMazeSolver)
                    optionDialogOpen(false)
                }
            },
        )
    }

    //Dialog that pops up when user clicked on algorithm description
    OptionSelectionDialog(
        dialogLabel = stringResource(id = R.string.pathfinding_algorithm),
        isDialogOpen = isDescDialogOpen,
        dialogOpen = { isOpen ->
            descDialogOpen(isOpen)
        }
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 10.dp),
            text = stringResource(id = algorithmDescriptionMap[currentAlgorithm]!!),
            style = TextStyle(
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            )
        )
    }
}

@Composable
fun MazeGeneratingAlgorithmSection(
    modifier: Modifier,
    currentMazeGenAlgorithm: MazeGenerator,
    mazeGenAlgorithmSelection: (MazeGenerator) -> Unit,
    isSelectDialogOpen: Boolean,
    selectDialogOpen: (Boolean) -> Unit,
    isDescDialogOpen: Boolean,
    descDialogOpen: (Boolean) -> Unit
) {
    Column(
        modifier = modifier
    ) {
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
        )

        SettingsOption(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            optionDescription = stringResource(id = R.string.current_maze_gen_algorithm),
            currentlySelectedOption = stringResource(id = mazeGeneratorLabelsMap[currentMazeGenAlgorithm]!!),
            dialogOpen = { isOpen ->
                selectDialogOpen(isOpen)
            }
        )

        SettingsOption(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            optionDescription = stringResource(id = R.string.current_maze_gen_algorithm_desc),
            dialogOpen = { isOpen ->
                descDialogOpen(isOpen)
            }
        )
    }

    OptionSelectionDialog(
        dialogLabel = stringResource(id = R.string.maze_gen_algorithm),
        isDialogOpen = isSelectDialogOpen,
        dialogOpen = { isOpen ->
            selectDialogOpen(isOpen)
        }
    ) {
        RadioButtonsGenerator(
            modifier = Modifier,
            listOfOptions = MazeGenerator.values().toList(),
            labels = mazeGeneratorLabelsMap,
            currentlySelectedOption = currentMazeGenAlgorithm,
            optionSelection = { newMazeGen ->
                if (currentMazeGenAlgorithm != newMazeGen) {
                    mazeGenAlgorithmSelection(newMazeGen)
                    selectDialogOpen(false)
                }
            },
        )
    }

    OptionSelectionDialog(
        dialogLabel = stringResource(id = R.string.maze_gen_algorithm),
        isDialogOpen = isDescDialogOpen,
        dialogOpen = { isOpen ->
            descDialogOpen(isOpen)
        }
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 10.dp),
            text = stringResource(id = mazeGeneratorDescriptionMap[currentMazeGenAlgorithm]!!),
            style = TextStyle(
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            )
        )
    }
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
            currentlySelectedOption = stringResource(id = mazeInfoLabelsMap[currentMazeSize]!!),
            dialogOpen = { isOpen ->
                dialogOpen(isOpen)
            }
        )
    }

    OptionSelectionDialog(
        dialogLabel = stringResource(id = R.string.maze_size),
        isDialogOpen = isDialogOpen,
        dialogOpen = { isOpen ->
            dialogOpen(isOpen)
        }
    ) {
        RadioButtonsGenerator(
            modifier = Modifier,
            listOfOptions = MazeInfo.values().toList(),
            labels = mazeInfoLabelsMap,
            currentlySelectedOption = currentMazeSize,
            optionSelection = { newMazeSize ->
                if (currentMazeSize != newMazeSize) {
                    mazeSizeSelection(newMazeSize)
                    dialogOpen(false)
                }
            }
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