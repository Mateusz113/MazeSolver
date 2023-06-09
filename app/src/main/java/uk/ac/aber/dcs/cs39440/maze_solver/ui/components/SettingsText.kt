package uk.ac.aber.dcs.cs39440.maze_solver.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import uk.ac.aber.dcs.cs39440.maze_solver.R
import uk.ac.aber.dcs.cs39440.maze_solver.ui.theme.typography
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.Algorithm
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.MazeGenerator
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.MazeInfo
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.algorithmLabelsMap
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.mazeGeneratorLabelsMap
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.mazeInfoLabelsMap


/**
 * Composable that is responsible for the informative text about the current algorithm and maze size
 * @param mazeSize Size of the maze
 * @param algorithm Currently used algorithm
 * @param modifier Modifier passed to the composable
 */
@Composable
fun SettingsText(
    algorithm: Algorithm,
    mazeInfo: MazeInfo,
    mazeGenerator: MazeGenerator,
    modifier: Modifier
) {
    val algorithmText = stringResource(id = algorithmLabelsMap[algorithm]!!)
    val mazeInfoText = stringResource(id = mazeInfoLabelsMap[mazeInfo]!!)
    val mazeGeneratorText = stringResource(id = mazeGeneratorLabelsMap[mazeGenerator]!!)

    Text(
        modifier = modifier.padding(top = 5.dp),
        textAlign = TextAlign.Center,
        style = TextStyle(fontSize = typography.titleLarge.fontSize),
        text = stringResource(
            R.string.settings_text,
            algorithmText,
            mazeInfoText,
            mazeGeneratorText
        ),
    )
}