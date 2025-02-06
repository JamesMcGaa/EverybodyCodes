import kotlin.math.abs
import kotlin.math.min

fun main() {
    println("Part A: ${runP13("inputs/input13a.txt")}")
    println("Part B: ${runP13("inputs/input13b.txt")}")
    println("Part C: ${runP13("inputs/input13c.txt")}")
}

fun runP13(filename: String): Int {
    val grid =
        Utils.readAsGrid(filename, null) { it }.filterValues { it != ' ' && it != '#' }.toMutableMap()
    val startCoord = Utils.findCoord('E', grid)
    val relaxedDists = Utils.generalizedDijkstra(
        startCoord, grid.keys,
        neighbors = { coord -> coord.manhattanNeighbors intersect grid.keys },
        edgeWeight = { c1, c2 ->
            val heightDiff = abs(heightP13(grid[c1]!!) - heightP13(grid[c2]!!))
            min(heightDiff, 10 - heightDiff) + 1
        }
    )
    val endCoords = grid.filterValues { it == 'S' }.keys
    return endCoords.minOf { relaxedDists.first[it]!! }
}

fun heightP13(char: Char): Int {
    return when (char) {
        'S', 'E' -> 0
        else -> char.digitToInt()
    }
}

