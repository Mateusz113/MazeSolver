package uk.ac.aber.dcs.cs39440.maze_solver.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import uk.ac.aber.dcs.cs39440.maze_solver.R
import uk.ac.aber.dcs.cs39440.maze_solver.ui.theme.typography

@Composable
fun OptionSelectionDialog(
    dialogLabel: String,
    isDialogOpen: Boolean,
    dialogOpen: (Boolean) -> Unit,
    dialogContent: @Composable () -> Unit
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
                    val (label, labelDivider, radioButtons, buttonDivider, cancelButton) = createRefs()
                    Text(
                        modifier = Modifier
                            .constrainAs(label) {
                                top.linkTo(parent.top)
                                centerHorizontallyTo(parent)
                            }
                            .padding(top = 15.dp),
                        text = dialogLabel,
                        style = TextStyle(
                            fontSize = typography.titleMedium.fontSize,
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                    Divider(
                        modifier = Modifier
                            .constrainAs(labelDivider) {
                                top.linkTo(label.bottom)
                            }
                            .fillMaxWidth()
                            .padding(top = 15.dp),
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Column(
                        Modifier
                            .constrainAs(radioButtons) {
                                top.linkTo(labelDivider.bottom)
                                start.linkTo(parent.start)
                            }
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                            .heightIn(min = 0.dp, max = 300.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        dialogContent()
                    }

                    Divider(
                        modifier = Modifier
                            .constrainAs(buttonDivider) {
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
                                top.linkTo(buttonDivider.bottom)
                            }
                            .padding(5.dp),
                        onClick = { dialogOpen(false) },
                    ) {
                        Text(
                            text = stringResource(id = R.string.cancel),
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }
        }
    }
}