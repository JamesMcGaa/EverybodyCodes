fun main() {
    println("Part A: ${run18("inputs/input18a.txt", Part.A)}")
    println("Part B: ${run18("inputs/input18b.txt", Part.B)}")
    println("Part C: ${run18("inputs/input18c.txt", Part.C)}")
}

fun run18(filename: String, part: Part): Int {
    val grid = Utils.readAsGrid(filename, null) { it }
    val palmTrees = grid.filterValues { it == 'P' }.keys
    val wells = grid.filterValues { it == '.' }.keys

    if (part in setOf(Part.A, Part.B)) {
        val start = grid.filter { it.key.y in setOf(0, grid.keys.maxOf { it.y }) && it.value == '.' }.keys
        val bfs = Utils.generalizedBFS(
            grid, start,
            isLegal = { coord, grid -> coord in grid && grid[coord]!! != '#' },
            neighbors = { coord, grid -> coord.manhattanNeighbors },
        )
        return palmTrees.maxOf { bfs[it]!! }
    } else {
        return wells.minOf {
            well ->
            val bfs = Utils.generalizedBFS(
                grid, well,
                isLegal = { coord, grid -> coord in grid && grid[coord]!! != '#' },
                neighbors = { coord, grid -> coord.manhattanNeighbors },
            )
            palmTrees.sumOf { bfs[it]!! }
        }
    }
}