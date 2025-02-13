import Utils.Coord

fun main() {
//    println("Part A: ${getRunicWord(Utils.readAsGrid("inputs/input10a.txt", null) { it })}")
//    println("Part B: ${run10b()}")
    run10c()
}

class SpaceData(
    val row: MutableList<Coord>,
    val col: MutableList<Coord>,
    val otherInRow: List<Coord>,
    val otherInCol: List<Coord>,
)

class QuestionData(
    val isCol: Boolean,
    val dependentSpaces: MutableSet<Coord>,
)

fun run10c(): Int {
    val grid = Utils.readAsGrid("inputs/input10c.txt", null) { it }

    // Setup main 3 data structures
    var todoSpaces = mutableSetOf<Coord>()
    val questionToSpaces = mutableMapOf<Coord, QuestionData>()
    // Coord to RowData
    val spaceToSpaceData = grid.filterValues { it == '.' }.keys.associate { space ->
        todoSpaces.add(space)

        // Add this to our chunkMap
        val rowIdx = (space.x - 2) / 6
        val colIdx = (space.y - 2) / 6

        val otherInRow = mutableListOf(
            Coord(6 * rowIdx + 2, space.y),
            Coord(6 * rowIdx + 3, space.y),
            Coord(6 * rowIdx + 4, space.y),
            Coord(6 * rowIdx + 5, space.y),
        )
        val otherInCol = mutableListOf(
            Coord(space.x, 6 * colIdx + 2),
            Coord(space.x, 6 * colIdx + 3),
            Coord(space.x, 6 * colIdx + 4),
            Coord(space.x, 6 * colIdx + 5),
        )

        val endsOfRow = mutableListOf(
            Coord(6 * rowIdx, space.y),
            Coord(6 * rowIdx + 1, space.y),
            Coord(6 * rowIdx + 6, space.y),
            Coord(6 * rowIdx + 7, space.y),
        )
        val endsOfCol = mutableListOf(
            Coord(space.x, 6 * colIdx),
            Coord(space.x, 6 * colIdx + 1),
            Coord(space.x, 6 * colIdx + 6),
            Coord(space.x, 6 * colIdx + 7),
        )

        endsOfCol.forEach { endCoord ->
            if (grid[endCoord]!! == '?') {
                questionToSpaces[endCoord] =
                    questionToSpaces.getOrDefault(endCoord, QuestionData(isCol = true, mutableSetOf()))
                        .apply { this.dependentSpaces.add(space) }
            }
        }
        endsOfRow.forEach { endCoord ->
            if (grid[endCoord]!! == '?') {
                questionToSpaces[endCoord] =
                    questionToSpaces.getOrDefault(endCoord, QuestionData(isCol = false, mutableSetOf()))
                        .apply { this.dependentSpaces.add(space) }
            }
        }



        Pair(
            space, SpaceData(
                row = endsOfRow,
                col = endsOfCol,
                otherInCol = otherInCol,
                otherInRow = otherInRow,
            )
        )
    }

    while (true) {
        println("loop")
        // Solve Loop - try all then return if successful
        var solved = false
        var solvedSet = mutableSetOf<Coord>()
        for (coord in todoSpaces) {
            val rowColData = spaceToSpaceData[coord]!!
            val intersection = rowColData.row.map { grid[it]!! } intersect rowColData.col.map { grid[it]!! }
            if (intersection.size > 1) {
                solved = true // For now
                grid[coord] = intersection.first()
                solvedSet.add(coord)
//                println(intersection)
//                println(coord)
                grid[coord] = '&'
//                Utils.printGrid(grid)
//                throw Exception()
            }
            if (intersection.size == 1) {
                solved = true
                grid[coord] = intersection.first()
                solvedSet.add(coord)
            }
        }

        if (solved) {
            println("solved 1")
            todoSpaces.removeAll(solvedSet)
            continue
        }

        // Inference Loop - continue immediately to see if we can start solving again
        solvedSet = mutableSetOf()
        for (question in questionToSpaces) {
            val questionCoord = question.key
            val questionData = question.value
            val questionDeps = (if (questionData.isCol) {
                questionData.dependentSpaces.filter { grid[it]!! == '.' }
                    .map { spaceToSpaceData[it]!!.row.filter { grid[it]!! != '?' }.map { grid[it]!! } }
            } else {
                questionData.dependentSpaces.filter { grid[it]!! == '.' }
                    .map { spaceToSpaceData[it]!!.col.filter { grid[it]!! != '?' }.map { grid[it]!! } }
            })
            println(questionDeps)
            if (questionDeps.size <= 1) {
                continue
            }

            val intersection = questionDeps[0].toSet() intersect questionDeps[1].toSet()
            if (intersection.size == 1) {
                grid[questionCoord] = intersection.first()
                solvedSet.add(questionCoord)
                solved = true
            }
        }

        if (solved) {
            println("solved 2")
            solvedSet.forEach { questionToSpaces.remove(it) }
            continue
        }

        println("done")
        break // restart if nothing worked
    }

    Utils.printGrid(grid)
    return 0
}


/////////////////////////////////////////////////

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