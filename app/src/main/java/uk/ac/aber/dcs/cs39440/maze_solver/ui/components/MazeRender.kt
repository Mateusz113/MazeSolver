package uk.ac.aber.dcs.cs39440.maze_solver.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.Direction
import uk.ac.aber.dcs.cs39440.maze_solver.util.enums.MazeInfo
import uk.ac.aber.dcs.cs39440.maze_solver.util.maze_map.Cell

/**
 * Maze rendering composable
 * @param mazeMap Map of the maze to render
 * @param mazeInfo Maze information enum
 * @param modifier Modifier passed to the composable
 */
@Composable
fun MazeRender(
    mazeMap: MutableList<MutableList<Cell>>,
    mazeInfo: MazeInfo,
    modifier: Modifier
) {
    Canvas(
        modifier = modifier.padding(horizontal = 5.dp)
    ) {
        var currentCell: Cell?
        var lineStart: Offset
        var lineEnd: Offset
        //Size of the rectangles calculated based on the maze size
        val rectangleSize =
            Size(
                size.width / mazeInfo.size.first,
                size.height / mazeInfo.size.second
            )

        for (col in 0 until mazeInfo.size.first) {
            for (row in 0 until mazeInfo.size.second) {
                currentCell = mazeMap.getOrNull(col)?.getOrNull(row)

                currentCell?.let {
                    drawRect(
                        color = currentCell.affiliation.color,
                        size = rectangleSize,
                        topLeft = Offset(
                            (col * rectangleSize.width),
                            (row * rectangleSize.height)
                        )
                    )
                    for (direction in Direction.values()) {
                        if (currentCell.walls[direction] == true) {
                            when (direction) {
                                Direction.TOP -> {
                                    //Top left corner
                                    lineStart = Offset(
                                        (col * rectangleSize.width),
                                        (row * rectangleSize.height)
                                    )
                                    //Top right corner
                                    lineEnd = Offset(
                                        (col * rectangleSize.width + rectangleSize.width),
                                        (row * rectangleSize.height)
                                    )
                                }

                                Direction.RIGHT -> {
                                    //Top right corner
                                    lineStart = Offset(
                                        (col * rectangleSize.width + rectangleSize.width),
                                        (row * rectangleSize.height)
                                    )
                                    //Bottom right corner
                                    lineEnd = Offset(
                                        (col * rectangleSize.width + rectangleSize.width),
                                        (row * rectangleSize.height + rectangleSize.height)
                                    )
                                }

                                Direction.DOWN -> {
                                    //Bottom right corner
                                    lineStart = Offset(
                                        (col * rectangleSize.width + rectangleSize.width),
                                        (row * rectangleSize.height + rectangleSize.height)
                                    )
                                    //Bottom left corner
                                    lineEnd = Offset(
                                        (col * rectangleSize.width),
                                        (row * rectangleSize.height + rectangleSize.height)
                                    )
                                }

                                Direction.LEFT -> {
                                    //Bottom left corner
                                    lineStart = Offset(
                                        (col * rectangleSize.width),
                                        (row * rectangleSize.height + rectangleSize.height)
                                    )
                                    //Top left corner
                                    lineEnd = Offset(
                                        (col * rectangleSize.width),
                                        (row * rectangleSize.height)
                                    )
                                }
                            }
                            //Draw a wall line
                            drawLine(
                                color = Color.Black,
                                start = lineStart,
                                end = lineEnd,
                                strokeWidth = 15f
                            )
                        }
                    }
                }
            }
        }
    }
}