import Utils.Coord

fun main() {
    val p3a = runP3("inputs/input3a.txt")
    val p3b = runP3("inputs/input3b.txt")
    val p3c = runP3("inputs/input3c.txt", partCRules = true)
    println("Part 3A: $p3a")
    println("Part 3B: $p3b")
    println("Part 3C: $p3c")
}

fun runP3(filename: String, partCRules: Boolean = false): Int {
    val grid = Utils.readAsGrid(filename, null) { it }

    if (partCRules) {
        val maxX = grid.keys.maxOf { it.x }
        val maxY = grid.keys.maxOf { it.y }
        for (y in -1 .. maxY + 1) {
            grid[Coord(-1, y)] = '.'
            grid[Coord(maxX + 1, y)] = '.'
        }
        for (x in -1 .. maxX + 1) {
            grid[Coord(x, -1)] = '.'
            grid[Coord(x, maxY + 1)] = '.'
        }
    }

    var counter = 0
    var depth = 1
    var startSet = grid.filterValues {it == '.'}.keys
    while (true) {
        val bfs = Utils.generalizedBFS(grid, startSet,
            isLegal = { coord, bfsGrid -> coord in bfsGrid },
            neighbors = { coord, bfsGrid ->
                if (bfsGrid[coord] == '#') {
                    setOf()
                } else {
                    if (partCRules) {
                        coord.fullNeighbors.toSet()
                    } else {
                        coord.manhattanNeighbors.toSet()
                    }
                }
            })

        val removedCoords = (bfs).filterKeys { key -> grid[key] == '#' }
        removedCoords.keys.forEach {
            grid[it] = '.'
        }
        counter += removedCoords.size * depth
        depth++
        if (removedCoords.isEmpty()) {
            break
        }
    }
    return counter
}