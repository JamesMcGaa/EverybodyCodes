fun main() {
    val grid = Utils.readAsGrid("inputs/input15a.txt", null) { it }
    val target = grid.filter { entry -> entry.value == '.' && entry.key.x == 0 }.keys.first()
    val startSet = grid.filterValues { it == 'H' }.keys

    val bfs = Utils.generalizedBFS(
        grid, startSet,
        isLegal = { coord, grid -> coord in grid && grid[coord]!! != '#' },
        neighbors = { coord, grid -> coord.manhattanNeighbors },
    )

    println(bfs[target]!! * 2)
}