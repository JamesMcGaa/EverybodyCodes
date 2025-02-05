import Utils.Coord

fun main() {
    println("Part A: ${getRunicWord(Utils.readAsGrid("inputs/input10a.txt", null) { it })}")
    println("Part B: ${run10b()}")
    run10c()
}

class RowColData(
    val row: List<Coord>,
    val col: List<Coord>,
)

fun run10c(): Int {
    val grid = Utils.readAsGrid("inputs/input10c.txt", null) { it }

    // Setup main 3 data structures
    val todo = mutableSetOf<Coord>()
    val chunkIndicesToCoords = mutableMapOf<Pair<Int, Int>, MutableList<Coord>>()
    val coordToRowColData = grid.filterValues { it == '.' }.keys.associate {
        val rowIdx = (it.x - 2) / 6
        val colIdx = (it.y - 2) / 6
        chunkIndicesToCoords[Pair(rowIdx, colIdx)] =
            chunkIndicesToCoords.getOrDefault(Pair(rowIdx, colIdx), mutableListOf()).apply { add(it) }
        todo.add(it)
        Pair(
            it, RowColData(
                row = listOf(
                    Coord(6 * rowIdx, it.y),
                    Coord(6 * rowIdx + 1, it.y),
                    Coord(6 * rowIdx + 6, it.y),
                    Coord(6 * rowIdx + 7, it.y),
                ),
                col = listOf(
                    Coord(it.x, 6 * colIdx),
                    Coord(it.x, 6 * colIdx + 1),
                    Coord(it.x, 6 * colIdx + 6),
                    Coord(it.x, 6 * colIdx + 7),
                )
            )
        )
    }

    while (true) {
        // Solve Loop - try all then return if successful
        var solved = false
        val solvedSet = mutableSetOf<Coord>()
        for (coord in todo) {
            val rowColData = coordToRowColData[coord]!!
            val intersection = rowColData.row.map { grid[it]!! } intersect rowColData.col.map { grid[it]!! }
//            if (intersection.size > 1) {
//                println(intersection)
//                println(coord)
//                grid[coord] = '&'
//                Utils.printGrid(grid)
//                throw Exception()
//            }
            if (intersection.size == 1) {
                solved = true
                grid[coord] = intersection.first()
                solvedSet.add(coord)
            }
        }

        if (solved) {
            todo.removeAll(solvedSet)
            continue
        }

        // Inference Loop - continue immediately to see if we can start solving again


        break // restart if nothing worked
    }

    Utils.printGrid(grid)
    return 0
}

fun getRunicWord(grid: MutableMap<Coord, Char>): String {
    val unfilled = grid.filterValues { it == '.' }.keys
    var partA = ""
    unfilled.forEach { coord ->
        val vertical = grid.filter { it.key.x == coord.x && it.value != '.' }.values
        val horizontal = grid.filter { it.key.y == coord.y && it.value != '.' }.values
        val intersection = vertical intersect horizontal

        assert(intersection.size == 1)
        grid[coord] = intersection.first()
        // Ordering of iteration is the same as desired for the problem
        partA += grid[coord]
    }
    return partA
}

fun run10b(): Int {
    val grid = Utils.readAsGrid("inputs/input10b.txt", null) { it }
    var counter = 0
    val r = grid.keys.maxOf { it.x } / 9
    val c = grid.keys.maxOf { it.y } / 9
    for (i in 0..r) {
        for (j in 0..c) {
            val newGrid = grid.filterKeys { it.x in 9 * i until 9 * i + 8 && it.y in 9 * j until 9 * j + 8 }
            val runicWord = getRunicWord(newGrid.toMutableMap())
            counter += scoreRunicWord(runicWord)
        }
    }
    return counter
}

fun scoreRunicWord(runicWord: String): Int {
    var counter = 0
    runicWord.forEachIndexed { index, ch -> counter += (index + 1) * (ch - 'A' + 1) }
    return counter
}