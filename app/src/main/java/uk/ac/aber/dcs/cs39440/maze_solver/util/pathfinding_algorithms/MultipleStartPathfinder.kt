package uk.ac.aber.dcs.cs39440.maze_solver.util.pathfinding_algorithms

import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.Direction
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.Node
import uk.ac.aber.dcs.cs39440.maze_solver.util.maze_map.Cell
import java.util.Queue

interface MultipleStartPathfinder : Pathfinder {
    //List of entries to the maze
    val entries: List<Cell>
    //List of queues that each maze entry has
    val entriesQueues: List<Queue<Cell>>
    //List of parent maps for each entry to the maze
    val entriesParentMaps: List<Map<Cell, Cell>>
    //List of considered maze cells for each entry
    val entriesConsideredMaps: List<Array<Array<Boolean>>>
    //Cells that go to each end of the found final path
    var finalPathFirstEnd: Cell?
    var finalPathSecondEnd: Cell?
    //Indexes of the entries that are part of the final path
    var finalPathFirstEndIndex: Int?
    var finalPathSecondEndIndex: Int?
    //Cell used as current cell in getFinalPath function
    var currentCellList: MutableList<Cell?>
    //Solution consists of x and y coordinates of the collision cell and index of the entry considered map that the cell has collided with
    var solution: Triple<Int, Int, Int>?
    //Indicate if each end of the final path were reached
    var finalFirstEndReached: Boolean
    var finalSecondEndReached: Boolean


    override fun getFinalPath(
        updateAffiliation: (Cell, Node) -> Unit
    ): Boolean {
        if (!finalFirstEndReached) {
            finalPathFirstEnd?.let { finalPathFirstEndCell ->
                if (finalPathFirstEndCell.x == entries[finalPathFirstEndIndex!!].x && finalPathFirstEndCell.y == entries[finalPathFirstEndIndex!!].y) {
                    finalFirstEndReached = true
                } else {
                    updateAffiliation(finalPathFirstEndCell, Node.FinalPath)
                }
            }
        }

        if (!finalSecondEndReached) {
            finalPathSecondEnd?.let { finalPathSecondEndCell ->
                if (finalPathSecondEndCell.x == entries[finalPathSecondEndIndex!!].x && finalPathSecondEndCell.y == entries[finalPathSecondEndIndex!!].y) {
                    finalSecondEndReached = true
                } else {
                    updateAffiliation(finalPathSecondEndCell, Node.FinalPath)
                }
            }
        }

        return if (finalFirstEndReached && finalSecondEndReached) {
            true
        } else {
            finalPathFirstEnd = entriesParentMaps[finalPathFirstEndIndex!!][finalPathFirstEnd]
            finalPathSecondEnd = entriesParentMaps[finalPathSecondEndIndex!!][finalPathSecondEnd]
            false
        }
    }


    override fun updateMazeIndicators(updateAffiliation: (Cell, Node) -> Unit) {
        entries.forEach {
            updateAffiliation(it, Node.Start)
        }
    }

    //Checks all the possible collisions of the given point neighbours with the checked cells of other entries
    fun checkForSolution(
        mazeMap: MutableList<MutableList<Cell>>,
        currentCell: Cell,
        currentCellIndex: Int,
        entriesConsideredMaps: List<Array<Array<Boolean>>>
    ): Triple<Int, Int, Int>? {
        for (direction in Direction.values()) {
            when (direction) {
                Direction.TOP -> {
                    if (currentCell.y - 1 >= 0) {
                        entriesConsideredMaps.forEachIndexed { index, consideredMap ->
                            if (currentCellIndex != index) {
                                if (consideredMap[currentCell.x][currentCell.y - 1]
                                    && currentCell.walls[Direction.TOP] == false
                                    && mazeMap[currentCell.x][currentCell.y - 1].walls[Direction.DOWN] == false
                                ) {
                                    return Triple(currentCell.x, currentCell.y - 1, index)
                                }
                            }
                        }
                    }
                }

                Direction.RIGHT -> {
                    if (currentCell.x + 1 < mazeWidth) {
                        entriesConsideredMaps.forEachIndexed { index, consideredMap ->
                            if (currentCellIndex != index) {
                                if (consideredMap[currentCell.x + 1][currentCell.y]
                                    && currentCell.walls[Direction.RIGHT] == false
                                    && mazeMap[currentCell.x + 1][currentCell.y].walls[Direction.LEFT] == false
                                ) {
                                    return Triple(currentCell.x + 1, currentCell.y, index)
                                }
                            }
                        }
                    }
                }

                Direction.DOWN -> {
                    if (currentCell.y + 1 < mazeHeight) {
                        entriesConsideredMaps.forEachIndexed { index, consideredMap ->
                            if (currentCellIndex != index) {
                                if (consideredMap[currentCell.x][currentCell.y + 1]
                                    && currentCell.walls[Direction.DOWN] == false
                                    && mazeMap[currentCell.x][currentCell.y + 1].walls[Direction.TOP] == false
                                ) {
                                    return Triple(currentCell.x, currentCell.y + 1, index)
                                }
                            }
                        }
                    }
                }

                Direction.LEFT -> {
                    if (currentCell.x - 1 >= 0) {
                        entriesConsideredMaps.forEachIndexed { index, consideredMap ->
                            if (currentCellIndex != index) {
                                if (consideredMap[currentCell.x - 1][currentCell.y]
                                    && currentCell.walls[Direction.LEFT] == false
                                    && mazeMap[currentCell.x - 1][currentCell.y].walls[Direction.RIGHT] == false
                                ) {
                                    return Triple(currentCell.x - 1, currentCell.y, index)
                                }
                            }
                        }
                    }
                }
            }
        }
        //Return null if there are no solutions found
        return null
    }
}