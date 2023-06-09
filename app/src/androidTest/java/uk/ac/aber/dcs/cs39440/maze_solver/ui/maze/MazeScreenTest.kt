package uk.ac.aber.dcs.cs39440.maze_solver.ui.maze

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import org.junit.Rule
import org.junit.Test
import uk.ac.aber.dcs.cs39440.maze_solver.MainActivity
import java.util.Timer
import kotlin.concurrent.schedule

class MazeScreenTest {
    @get:Rule
    var composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun checkMazeScreenAfterSwipe_isNotVisible() {
        composeRule.onRoot().performTouchInput { swipeLeft() }
        composeRule.onNodeWithText("Start").assertIsNotDisplayed()
    }

    @Test
    fun checkStartButton_isClickable() {
        composeRule.onNodeWithText("Start").assertHasClickAction()
    }

    @Test
    fun checkStartButton_isNotClickableAfterClick() {
        composeRule.onNodeWithText("Start").performClick()
        composeRule.onNodeWithText("Start").assertIsNotEnabled()
    }

    @Test
    fun checkReloadButton_isNotClickable() {
        composeRule.onNodeWithText("Reload").assertIsNotEnabled()
    }

    @Test
    fun checkReloadButtonAfterMazeSolved_isEnabled() {
        composeRule.onNodeWithText("Start").performClick()
        val delay = 40_000L
        var expired = false
        //Waits 40s for the algorithm to complete
        composeRule.waitUntil(delay + 2000L) {
            Timer().schedule(delay) {
                expired = true
            }
            expired
        }
        composeRule.onNodeWithText("Reload").assertIsEnabled()
    }

    @Test
    fun checkStartButtonAfterMazeSolvedAndReloaded_isEnabled() {
        composeRule.onNodeWithText("Start").performClick()
        val delay = 40_000L
        var expired = false
        //Waits 40s for the algorithm to complete
        composeRule.waitUntil(delay + 2000L) {
            Timer().schedule(delay) {
                expired = true
            }
            expired
        }
        composeRule.onNodeWithText("Reload").performClick()
        composeRule.onNodeWithText("Start").assertIsEnabled()
    }

    @Test
    fun checkReloadButtonAfterMazeSolvingStart_isEnabled() {
        composeRule.onNodeWithText("Start").performClick()
        composeRule.onNodeWithText("Reload").assertIsEnabled()
    }
}