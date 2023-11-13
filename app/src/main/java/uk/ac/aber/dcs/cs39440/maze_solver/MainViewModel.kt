package uk.ac.aber.dcs.cs39440.maze_solver

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.Algorithm
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.MazeGenerator
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.MazeInfo
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.Node
import uk.ac.aber.dcs.cs39440.maze_solver.util.maze_map.Cell
import uk.ac.aber.dcs.cs39440.maze_solver.util.maze_map.primsMazeGenerator
import uk.ac.aber.dcs.cs39440.maze_solver.util.maze_map.randomizedDfsMazeGenerator

/**
 * MainViewModel used across the application.
 * Holds the algorithm, maze map and information settings.
 */
class MainViewModel : ViewModel() {
    //Private internal StateFlows
    private val _algorithmChosen = MutableStateFlow(Algorithm.values()[0])
    private val _mazeInformation = MutableStateFlow(MazeInfo.values()[0])
    private val _mazeGenerator = MutableStateFlow(MazeGenerator.values()[0])
    private var _mazeMap = MutableStateFlow(mutableStateListOf<MutableList<Cell>>())
    private var _mazeMapBackup: MutableList<MutableList<Cell>> = mutableListOf()

    //Public exposed StateFlows
    val algorithmChosen = _algorithmChosen.asStateFlow()
    val mazeInformation = _mazeInformation.asStateFlow()
    val mazeGenerator = _mazeGenerator.asStateFlow()
    val mazeMap = _mazeMap.asStateFlow()

    //Generates maze on viewModel initialization
    init {
        generateMaze()
    }

    //Generates maze based on the current settings and saves the new version to reload
    private fun generateMaze() {
        val newMazeMap = _mazeMap.value.toMutableList()
        newMazeMap.clear()
        when (_mazeGenerator.value) {
            MazeGenerator.RandomizedDFS -> {
                randomizedDfsMazeGenerator(
                    mazeMap = newMazeMap,
                    mazeWidth = _mazeInformation.value.size.first,
                    mazeHeight = _mazeInformation.value.size.second
                )
            }

            MazeGenerator.Prims -> {
                primsMazeGenerator(
                    mazeMap = newMazeMap,
                    mazeWidth = _mazeInformation.value.size.first,
                    mazeHeight = _mazeInformation.value.size.second
                )
            }
        }
        saveMaze(newMazeMap)
    }

    //Saves the new maze version to the _mazeMapBackup
    private fun saveMaze(newMaze: MutableList<MutableList<Cell>>) {
        _mazeMap.value.clear()
        _mazeMapBackup.clear()
        _mazeMap.value.addAll(newMaze)
        _mazeMapBackup.addAll(newMaze)
    }

    //Used to update the cell affiliation inside the mazeMap
    fun updateIndexAffiliation(
        currentCell: Cell,
        newType: Node
    ) {
        val updatedCell = currentCell.copy(affiliation = newType)
        val updatedRow = _mazeMap.value[currentCell.x].toMutableList().apply {
            set(currentCell.y, updatedCell)
        }
        _mazeMap.value = _mazeMap.value.apply {
            set(currentCell.x, updatedRow)
        }

    }

    //Restored the clear maze from _mazeMapBackup
    fun restoreSavedMaze() {
        _mazeMap.value.clear()
        _mazeMap.value.addAll(_mazeMapBackup)
    }

    //Switches the algorithm to the new one provided
    fun changeAlgorithm(algorithm: Algorithm) {
        _algorithmChosen.value = algorithm
    }

    //Switches the mazeInfo to the new one provided and generates new maze
    fun changeMazeInformation(mazeInfo: MazeInfo) {
        _mazeInformation.value = mazeInfo
        generateMaze()
    }

    //Switches the maze generator to one provided
    fun changeMazeGenerator(mazeGenerator: MazeGenerator) {
        _mazeGenerator.value = mazeGenerator
    }
}