package uk.ac.aber.dcs.cs39440.maze_solver.ui.settings

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import org.junit.Rule
import org.junit.Test
import uk.ac.aber.dcs.cs39440.maze_solver.ui.main.MainActivity

class SettingsScreenTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun swipeRight_settingsScreenIsVisible() {
        composeRule.onRoot().performTouchInput { swipeLeft() }
        composeRule.onNodeWithText("Algorithm:").assertIsDisplayed()
    }

    @Test
    fun checkTheAlgorithmButtons_isClickable() {
        composeRule.onNodeWithText("Depth-first search").assertHasClickAction()
    }

    @Test
    fun checkTheMazeInfoButtons_isClickable() {
        composeRule.onNodeWithText("30 x 45").assertHasClickAction()
    }

    @Test
    fun checkTheMazeGeneratorButtons_isClickable() {
        composeRule.onNodeWithText("Randomized DFS").assertHasClickAction()
    }
}