package uk.ac.aber.dcs.cs39440.maze_solver.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import uk.ac.aber.dcs.cs39440.maze_solver.ui.theme.typography

@Composable
fun <T> RadioButtonsGenerator(
    modifier: Modifier,
    listOfOptions: List<T>,
    labels: Map<T, Int>,
    currentlySelectedOption: T,
    optionSelection: (T) -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        listOfOptions.forEach { option ->
            //Each radio button is packed into the row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (option == currentlySelectedOption),
                        onClick = {
                            optionSelection(option)
                        },
                        role = Role.RadioButton
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                //Button
                RadioButton(
                    modifier = Modifier.padding(start = 10.dp),
                    selected = (option == currentlySelectedOption),
                    onClick = {
                        optionSelection(option)
                    }
                )
                //Button text
                Text(
                    text = stringResource(labels[option]!!),
                    style = typography.labelLarge,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
        }
    }
}