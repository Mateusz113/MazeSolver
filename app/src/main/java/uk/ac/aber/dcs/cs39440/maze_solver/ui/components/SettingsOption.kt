package uk.ac.aber.dcs.cs39440.maze_solver.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.ac.aber.dcs.cs39440.maze_solver.ui.theme.typography

@Composable
fun SettingsOption(
    modifier: Modifier,
    optionDescription: String,
    currentlySelectedOption: String?,
    dialogOpen: (Boolean) -> Unit,
) {
    Column(
        modifier = modifier
            .clickable {
                dialogOpen(true)
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            modifier = Modifier.padding(start = 20.dp),
            text = optionDescription,
            style = TextStyle(
                fontSize = typography.titleMedium.fontSize,
                fontWeight = FontWeight.SemiBold
            )
        )
        currentlySelectedOption?.let {
            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = currentlySelectedOption
            )
        }
    }
}