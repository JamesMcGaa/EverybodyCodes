import kotlin.math.abs

fun main() {
    val grid =
        Utils.readAsGrid("inputs/input13a.txt", null) { it }.filterValues { it != ' ' && it != '#' }.toMutableMap()
    val startCoord = Utils.findCoord('S', grid)
    val endCoord = Utils.findCoord('E', grid)
    val relaxedDists = Utils.generalizedDijkstra(
        startCoord, grid.keys,
        neighbors = { coord -> coord.manhattanNeighbors intersect grid.keys },
        edgeWeight = { c1, c2 -> abs(heightP13(grid[c1]!!) - heightP13(grid[c2]!!)) + 1 },
    )
    println("Part A: ${relaxedDists.first[endCoord]}")
}

fun heightP13(char: Char): Int {
    return when (char) {
        'S', 'E' -> 0
        else -> char.digitToInt()
    }
}

