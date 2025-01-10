import Utils.Coord

fun main() {
    val grid = Utils.readAsGrid("inputs/input3a.txt", null) { it }

    var counter = 0
    var depth = 1
    Utils.printGrid(grid)
    while (true) {
        val bfs = Utils.generalizedBFS(grid, Coord(0, 0),
            isLegal = { coord, bfsGrid -> coord in bfsGrid },
            neighbors = { coord, bfsGrid ->
                if (bfsGrid[coord] == '#') {
                    setOf()
                } else {
                    coord.manhattanNeighbors.toSet()
                }
            })

        val bfs2 = Utils.generalizedBFS(grid, Coord(17, 35),
            isLegal = { coord, bfsGrid -> coord in bfsGrid },
            neighbors = { coord, bfsGrid ->
                if (bfsGrid[coord] == '#') {
                    setOf()
                } else {
                    coord.manhattanNeighbors.toSet()
                }
            })
        val removedCoords = (bfs + bfs2).filterKeys { key -> grid[key] == '#' }
        removedCoords.keys.forEach {
            grid[it] = '.'
        }
        Utils.printGrid(grid)
        counter += removedCoords.size * depth
        depth++
        if (removedCoords.isEmpty()) {
            break
        }
    }
    println(counter)
}