package uk.ac.aber.dcs.cs39440.maze_solver.util.pathfinding_algorithms

import com.google.common.truth.Truth
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.MazeGenerator
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.MazeInfo
import uk.ac.aber.dcs.cs39440.maze_solver.util.maze_map.Cell
import uk.ac.aber.dcs.cs39440.maze_solver.util.maze_map.primsMazeGenerator
import uk.ac.aber.dcs.cs39440.maze_solver.util.maze_map.randomizedDfsMazeGenerator

class DFSTest {
    private lateinit var mazeGenerators: List<MazeGenerator>
    private lateinit var mazeInformation: List<MazeInfo>
    private lateinit var mazeMap: MutableList<MutableList<Cell>>

    @Before
    fun setUp() {
        mazeGenerators = MazeGenerator.values().toList()
        mazeInformation = MazeInfo.values().toList()
        mazeMap = mutableListOf()
    }

    @Test
    fun `DFS running through all maze combinations, true on completion`() = runBlocking {
        mazeGenerators.forEach { mazeGenerator ->
            mazeInformation.forEach { mazeInfo ->
                when (mazeGenerator) {
                    MazeGenerator.Prims -> {
                        mazeMap = primsMazeGenerator(
                            mazeMap = mazeMap,
                            mazeWidth = mazeInfo.size.first,
                            mazeHeight = mazeInfo.size.second
                        )
                    }

                    MazeGenerator.RandomizedDFS -> {
                        mazeMap = randomizedDfsMazeGenerator(
                            mazeMap = mazeMap,
                            mazeWidth = mazeInfo.size.first,
                            mazeHeight = mazeInfo.size.second
                        )
                    }
                }
                Truth.assertThat(
                    dfs(
                        mazeWidth = mazeInfo.size.first,
                        mazeHeight = mazeInfo.size.second,
                        mazeMap = mazeMap,
                        startX = 0,
                        startY = 0,
                        endX = mazeInfo.size.first - 1,
                        endY = mazeInfo.size.second - 1,
                        updateAffiliation = { cell, node ->
                            mazeMap[cell.x][cell.y] =
                                mazeMap[cell.x][cell.y].copy(affiliation = node)
                        },
                        delayLength = 0L
                    )
                ).isTrue()
                mazeMap.clear()
            }
        }
    }

    @Test
    fun `DFS running through unsolvable maze, false on completion`() = runBlocking {
        mazeMap = randomizedDfsMazeGenerator(
            mazeMap = mazeMap,
            mazeWidth = mazeInformation[0].size.first,
            mazeHeight = mazeInformation[0].size.second
        )

        //Set the unsolvable maze
        mazeMap[mazeInformation[0].size.first - 1][mazeInformation[0].size.second - 1] =
            Cell(mazeInformation[0].size.first - 1, mazeInformation[0].size.second - 1)

        Truth.assertThat(
            dfs(
                mazeWidth = mazeInformation[0].size.first,
                mazeHeight = mazeInformation[0].size.second,
                mazeMap = mazeMap,
                startX = 0,
                startY = 0,
                endX = mazeInformation[0].size.first - 1,
                endY = mazeInformation[0].size.second - 1,
                updateAffiliation = { cell, node ->
                    mazeMap[cell.x][cell.y] =
                        mazeMap[cell.x][cell.y].copy(affiliation = node)
                },
                delayLength = 0L
            )
        ).isFalse()
    }
}