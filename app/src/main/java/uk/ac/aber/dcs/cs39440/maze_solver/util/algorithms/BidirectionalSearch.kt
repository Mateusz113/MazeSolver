package uk.ac.aber.dcs.cs39440.maze_solver.util.algorithms

import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.Direction
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.Node
import uk.ac.aber.dcs.cs39440.maze_solver.util.maze_map.Cell
import java.util.LinkedList
import java.util.Queue

class BidirectionalSearch(
    override var mazeWidth: Int,
    override var mazeHeight: Int,
    override val entries: List<Cell>,
) : MultipleStartPathfinder {
    //Variables inherited from the interface
    override var finalPathFirstEnd: Cell? = null
    override var finalPathSecondEnd: Cell? = null
    override var finalPathFirstEndIndex: Int? = null
    override var finalPathSecondEndIndex: Int? = null
    override var currentCellList: MutableList<Cell?> = mutableListOf()
    override var solution: Triple<Int, Int, Int>? = null
    override var finalFirstEndReached: Boolean = false
    override var finalSecondEndReached: Boolean = false
    override val entriesQueues: MutableList<Queue<Cell>> = mutableListOf()
    override val entriesParentMaps: MutableList<MutableMap<Cell, Cell>> = mutableListOf()
    override val entriesConsideredMaps: MutableList<Array<Array<Boolean>>> = mutableListOf()

    init {
        //Add the queue, parent map and considered items map for every entry to the maze
        entries.forEach {
            entriesQueues.add(LinkedList())
            entriesParentMaps.add(mutableMapOf())
            entriesConsideredMaps.add(Array(mazeWidth) { Array(mazeHeight) { false } })
        }
        //Add the first item to each entry queue
        for (i in entries.indices) {
            entriesQueues[i].add(entries[i])
        }
    }

    override fun run(
        mazeMap: MutableList<MutableList<Cell>>,
        updateAffiliation: (Cell, Node) -> Unit
    ): Boolean {
        //Fill the list with current cells and null if there is an empty queue for the entry
        entriesQueues.forEach {
            if (it.isNotEmpty()) {
                currentCellList.add(it.remove())
            } else {
                currentCellList.add(null)
            }
        }

        //Check for solution and exit if found
        currentCellList.forEachIndexed { index, currentCell ->
            currentCell?.let {
                solution = checkForSolution(
                    mazeMap = mazeMap,
                    currentCell = currentCell,
                    currentCellIndex = index,
                    entriesConsideredMaps = entriesConsideredMaps
                )
                solution?.let { solution ->
                    //Update the affiliation of the meeting point
                    updateAffiliation(mazeMap[solution.first][solution.second], Node.FinalPath)
                    //Set the starting cells for drawing the final path
                    finalPathFirstEnd = currentCell
                    finalPathSecondEnd =
                            //The copy function is used because the solution from mazeMap has different affiliation that the one in parentMap of the second end
                        entriesParentMaps[solution.third][mazeMap[solution.first][solution.second].copy(
                            affiliation = Node.Corridor
                        )]
                    //Set the information about each end parent map index
                    finalPathFirstEndIndex = index
                    finalPathSecondEndIndex = solution.third
                    //Return true as the solution to maze is found
                    return true
                }
                updateAffiliation(currentCell, Node.Considered)
            }
        }

        //Update the queue for each entry and affiliation of the found cells
        currentCellList.forEachIndexed { index, currentCell ->
            currentCell?.let {
                addNeighboursToQueue(
                    mazeMap = mazeMap,
                    currentCell = it,
                    cellIndex = index
                ).forEach { queuedCell ->
                    updateAffiliation(queuedCell, Node.Queued)
                }
            }
        }

        //Clear all the elements before next iteration
        currentCellList.clear()

        return false
    }

    //Function returns the list of items that were added to the map
    private fun addNeighboursToQueue(
        mazeMap: MutableList<MutableList<Cell>>,
        currentCell: Cell,
        cellIndex: Int
    ): List<Cell> {
        val cellList = mutableListOf<Cell>()
        val validPathsMap = checkForValidPaths(
            mazeMap = mazeMap,
            currentCell = currentCell
        )
        for (direction in Direction.values()) {
            validPathsMap[direction]?.let {
                entriesParentMaps[cellIndex][it] = currentCell
                entriesQueues[cellIndex].add(it)
                entriesConsideredMaps[cellIndex][it.x][it.y] = true
                cellList.add(it)
            }
        }
        return cellList.toList()
    }
}