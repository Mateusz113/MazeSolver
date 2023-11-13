package uk.ac.aber.dcs.cs39440.maze_solver.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import uk.ac.aber.dcs.cs39440.maze_solver.R

@Composable
fun <T> OptionSelectionDialog(
    listOfOptions: List<T>,
    labels: Map<T, Int>,
    currentlySelectedOption: T,
    optionSelection: (T) -> Unit,
    isDialogOpen: Boolean,
    dialogOpen: (Boolean) -> Unit,
) {
    if (isDialogOpen) {

        Dialog(
            onDismissRequest = {
                dialogOpen(false)
            },
        ) {
            Card(
                shape = RoundedCornerShape(size = 16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)
            ) {
                ConstraintLayout {
                    val (radioButtons, divider, cancelButton) = createRefs()
                    RadioButtonsGenerator(
                        modifier = Modifier
                            .constrainAs(radioButtons) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                            }
                            .fillMaxWidth()
                            .padding(top = 10.dp, start = 10.dp),
                        listOfOptions = listOfOptions,
                        labels = labels,
                        currentlySelectedOption = currentlySelectedOption,
                        optionSelection = { algorithm ->
                            if (currentlySelectedOption != algorithm) {
                                optionSelection(algorithm)
                                dialogOpen(false)
                            }
                        }
                    )

                    Divider(
                        modifier = Modifier
                            .constrainAs(divider) {
                                top.linkTo(radioButtons.bottom)
                            }
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    TextButton(
                        modifier = Modifier
                            .constrainAs(cancelButton) {
                                bottom.linkTo(parent.bottom)
                                end.linkTo(parent.end)
                                top.linkTo(divider.bottom)
                            }
                            .padding(end = 10.dp, bottom = 10.dp, top = 5.dp),
                        onClick = { dialogOpen(false) }
                    ) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                }
            }
        }
    }
}