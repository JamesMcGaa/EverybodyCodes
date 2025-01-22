fun main() {
    val grid = Utils.readAsGrid("inputs/input10a.txt", null, {it})
    Utils.printGrid(grid)
    val unfilled = grid.filterValues { it == '.' }.keys
    var partA = ""
    unfilled.forEach { coord ->
        val vertical = grid.filter {it.key.x == coord.x && it.value != '.'}.values
        val horizontal = grid.filter {it.key.y == coord.y && it.value != '.'}.values
        val intersection = vertical intersect horizontal

        assert(intersection.size == 1)
        grid[coord] = intersection.first()
        // Ordering of iteration is the same as desired for the problem
        partA += grid[coord]
    }
    println("Part A: $partA")
    Utils.printGrid(grid)

}