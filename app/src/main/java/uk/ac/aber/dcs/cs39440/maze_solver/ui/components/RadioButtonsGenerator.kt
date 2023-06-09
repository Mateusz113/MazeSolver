package uk.ac.aber.dcs.cs39440.maze_solver.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import uk.ac.aber.dcs.cs39440.maze_solver.R
import uk.ac.aber.dcs.cs39440.maze_solver.ui.theme.typography
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.Algorithm
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.MazeGenerator
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.MazeInfo
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.algorithmLabelsMap
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.mazeGeneratorLabelsMap
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.mazeInfoLabelsMap

@Composable
fun RadioButtonsGenerator(
    modifier: Modifier,
    optionSelection: (Any) -> Unit,
    currentlySelectedOption: Any,
    listOfOptions: List<Any>
) {
    ConstraintLayout(modifier = modifier) {
        //Refs for all the elements
        val (description, buttons) = createRefs()

        //Set the section label based on type
        val sectionLabel = when (currentlySelectedOption) {
            is Algorithm -> {
                R.string.algorithm_choosing_desc
            }

            is MazeGenerator -> {
                R.string.maze_generator
            }

            is MazeInfo -> {
                R.string.maze_size_desc
            }

            else -> {
                R.string.algorithm_choosing_desc
            }
        }

        // Algorithm text
        Text(
            text = stringResource(sectionLabel),
            textAlign = TextAlign.Start,
            style = TextStyle(fontSize = typography.headlineMedium.fontSize),
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(description) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
                .padding(start = 10.dp)
        )

        //Radio buttons
        RadioButtons(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(buttons) {
                    start.linkTo(parent.start)
                    top.linkTo(description.bottom)
                }
                .padding(horizontal = 5.dp),
            listOfOptions = listOfOptions,
            onOptionChange = { optionSelected ->
                optionSelection(optionSelected)
            },
            currentlySelectedOption = currentlySelectedOption
        )
    }
}

@Composable
private fun RadioButtons(
    modifier: Modifier,
    listOfOptions: List<Any>,
    onOptionChange: (Any) -> Unit,
    currentlySelectedOption: Any
) {
    //Variables to hold the buttons state
    val (selectedOption, onOptionSelected) = remember {
        mutableStateOf(
            listOfOptions[listOfOptions.indexOf(currentlySelectedOption)]
        )
    }

    //Reference to the map of labels
    val labelsMap = when (currentlySelectedOption) {
        is Algorithm -> {
            algorithmLabelsMap
        }

        is MazeGenerator -> {
            mazeGeneratorLabelsMap
        }

        is MazeInfo -> {
            mazeInfoLabelsMap
        }

        else -> {
            algorithmLabelsMap
        }
    }

    Column(
        modifier = modifier
            .selectableGroup()
    ) {
        listOfOptions.forEach { option ->
            //Each radio button is packed into the row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (option == selectedOption),
                        onClick = {
                            onOptionSelected(option)
                            onOptionChange(option)
                        },
                        role = Role.RadioButton
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                //Button
                RadioButton(
                    selected = (option == selectedOption),
                    onClick = {
                        onOptionSelected(option)
                        onOptionChange(option)
                    }
                )
                //Button text
                Text(
                    text = stringResource(labelsMap[option]!!),
                    style = typography.labelLarge,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
        }
    }
}