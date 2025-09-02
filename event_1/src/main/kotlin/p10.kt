import Utils.Coord

fun main() {
    println("Part A: ${getRunicWord(Utils.readAsGrid("inputs/input10a.txt", null) { it })}")
    println("Part B: ${run10b()}")
    println("Part B: ${run10c()}")
}

fun getChunk(coord: Coord): Coord {
    return Coord((coord.x - 2) / 6, (coord.y - 2) / 6)
}

fun run10c(): Int {
    val grid = Utils.readAsGrid("inputs/input10c.txt", null) { it }
    val matchedCoords = mutableMapOf<Coord, MutableSet<Coord>>()
    var todoSpaces = mutableSetOf<Coord>()

    class SpaceData(
        val chunk: Coord,
        val row: Set<Coord>,
        val col: Set<Coord>,
    ) {
        val unmatchedRow
            get() = row.filter { it !in matchedCoords[chunk]!! }.toSet()
        val unmatchedCol
            get() = col.filter { it !in matchedCoords[chunk]!! }.toSet()
    }

    val spaceToSpaceData =
        grid.filterKeys { it.x % 6 in setOf(2, 3, 4, 5) && it.y % 6 in setOf(2, 3, 4, 5) }.keys.associate { space ->
            // Add this to our chunkMap
            val rowIdx = (space.x - 2) / 6
            val colIdx = (space.y - 2) / 6
            val chunk = Coord(rowIdx, colIdx)
            if (chunk !in matchedCoords) {
                matchedCoords[chunk] = mutableSetOf()
            }

            val endsOfRow = setOf(
                Coord(6 * rowIdx, space.y),
                Coord(6 * rowIdx + 1, space.y),
                Coord(6 * rowIdx + 6, space.y),
                Coord(6 * rowIdx + 7, space.y),
            )
            val endsOfCol = setOf(
                Coord(space.x, 6 * colIdx),
                Coord(space.x, 6 * colIdx + 1),
                Coord(space.x, 6 * colIdx + 6),
                Coord(space.x, 6 * colIdx + 7),
            )
            todoSpaces.add(space)
            Pair(
                space, SpaceData(
                    chunk = Coord(rowIdx, colIdx),
                    row = endsOfRow,
                    col = endsOfCol,
                )
            )
        }


    while (true) {
        var solved = false
        var solvedSet = mutableSetOf<Coord>()
        for (coord in todoSpaces) {
            val spaceData = spaceToSpaceData[coord]!!
            val intersection = spaceData.unmatchedRow
                .map { grid[it]!! } intersect spaceData.unmatchedCol.map { grid[it]!! }
            val unmatchedRow = spaceData.unmatchedRow
            val unmatchedCol = spaceData.unmatchedCol

            if (intersection.size == 1 && intersection.first() != '?') { // Single clean match
                val solution = intersection.first()
                solved = true
                grid[coord] = solution
                solvedSet.add(coord)

                matchedCoords[getChunk(coord)]!!.add(coord)
                (unmatchedRow + unmatchedCol).forEach {
                    if (solution == grid[it]!!) {
                        matchedCoords[getChunk(coord)]!!.add(it)
                    }
                }
            } else if (unmatchedRow.size == 1 && unmatchedCol.isEmpty()) { // No unpaired matches
                throw Exception()
            } else if (unmatchedRow.isEmpty() && unmatchedCol.size == 1) { // No unpaired matches
                throw Exception()
            } else if (unmatchedRow.size == 1 && unmatchedCol.size == 1) {
                matchedCoords[getChunk(coord)]!!.add(unmatchedRow.first())
                matchedCoords[getChunk(coord)]!!.add(unmatchedCol.first())
                solvedSet.add(coord)
                solved = true
                if (grid[unmatchedRow.first()] == '?') { // This question mark is determined now
                    val solution = grid[unmatchedCol.first()]!!
                    grid[coord] = solution
                    grid[unmatchedRow.first()] = solution

                } else if (grid[unmatchedCol.first()] == '?') { // This question mark is determined now
                    val solution = grid[unmatchedRow.first()]!!
                    grid[coord] = solution
                    grid[unmatchedCol.first()] = solution
                } else { // We have a mismatch - mark just for seeing an unsolvable square
                    grid[coord] = '&'
                }
            }
        }

        if (solved) {
            todoSpaces.removeAll(solvedSet)
            continue // Try again since we had changes
        }

        break // No progress so we can finish
    }

    var counter = 0
    for (chunk in spaceToSpaceData.values.map { it.chunk }.toSet()) {
        val base = Coord(6 * chunk.x + 2, 6 * chunk.y + 2)
        var str = ""
        for (i in 0..3) {
            for (j in 0..3) {
                str += grid[base + Coord(i,j)]
            }
        }
        if (str.all {it.isLetter()}) {
            counter += scoreRunicWord(str)
        }
    }
    return counter
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