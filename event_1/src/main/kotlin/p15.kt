import Utils.Coord
import java.util.*
import kotlin.math.max

fun main() {
    println("Part A: ${run15("inputs/input15a.txt")}")
    println("Part B: ${run15("inputs/input15b.txt")}")
    println("Part C: ${run15("inputs/input15c.txt")}")
}

data class HerbMazeState(
    val coord: Coord,
    val seen: BitSet,
)

fun run15(filename: String): Int {
    val grid = Utils.readAsGrid(filename, null) { it }
    val letters = grid.values.filter { it !in setOf('.', '#', '~') }.map { it.toString() }.toSet()
    val start = grid.filter { entry -> entry.value == '.' && entry.key.x == 0 }.keys.first()
    val startState = HerbMazeState(start, BitSet(letters.size))
    var bestSoFar = 0 // Pruning
    val pruningFactor = 3 // 1 yields incorrect answer, 4 takes too long
    val bfs = Utils.generalizedBFSV2(
        startState,
        isLegal = { state -> state.coord in grid && grid[state.coord]!! !in setOf('#', '~') },
        neighbors = { state ->
            val neighborCoords = state.coord.manhattanNeighbors
            bestSoFar = max(bestSoFar, state.seen.cardinality())
            if (state.seen.cardinality() < bestSoFar - pruningFactor) {
                setOf()
            }
            else {
                neighborCoords.mapNotNull { neiCoord ->
                    if (grid[neiCoord] == '.') {
                        HerbMazeState(neiCoord, state.seen)
                    } else if (grid[neiCoord] !in setOf(null, '#', '~')) {
                        HerbMazeState(neiCoord, (state.seen.clone() as BitSet).apply { set(letters.indexOf(grid[neiCoord]!!.toString())) })
                    } else {
                        null
                    }
                }.toSet()
            }
        },
    )

    return bfs[HerbMazeState(start, BitSet(letters.size).apply { set(0, letters.size) })]!!
}
