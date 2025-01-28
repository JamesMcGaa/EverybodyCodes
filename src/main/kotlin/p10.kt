import Utils.Coord

fun main() {
    println("Part A: ${getRunicWord(Utils.readAsGrid("inputs/input10a.txt", null) { it })}")
    println("Part B: ${run10b()}")
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