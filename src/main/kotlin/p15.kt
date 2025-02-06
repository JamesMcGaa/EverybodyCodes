import Utils.Coord

fun main() {
//    println("Part A: ${run15("inputs/input15a.txt")}")
//    println("Part B: ${run15("inputs/input15b.txt")}")
    println("Part C: ${run15("inputs/input15c.txt")}")
}

data class HerbMazeState(
    val coord: Coord,
    val seen: Set<String>,
)

fun run15(filename: String): Int {
    val grid = Utils.readAsGrid(filename, null) { it }
    val letters = grid.values.filter { it !in setOf('.', '#', '~') }.map { it.toString() }.toSet()
    val start = grid.filter { entry -> entry.value == '.' && entry.key.x == 0 }.keys.first()
    val startState = HerbMazeState(start, setOf())
    println(grid.size)
    println(letters.size)
    println(letters)
    val bfs = Utils.generalizedBFSV2(
        startState,
        isLegal = { state -> state.coord in grid && grid[state.coord]!! !in setOf('#', '~') },
        neighbors = { state ->
            val neighborCoords = state.coord.manhattanNeighbors
            neighborCoords.mapNotNull { neiCoord ->
                if (grid[neiCoord] == '.') {
                    HerbMazeState(neiCoord, state.seen)
                } else if (grid[neiCoord] != null) {
                    HerbMazeState(neiCoord, state.seen + setOf(grid[neiCoord]!!.toString()))
                } else {
                    null
                }
            }.toSet()
        },
    )

    return bfs[HerbMazeState(start, letters)]!!
}