import Utils.Coord
import java.io.File

fun main() {
    println("Part A: ${run19("inputs/input19a.txt", 1)}")
    println("Part B: ${run19("inputs/input19b.txt", 100)}")
    println("Part C: ${run19c("inputs/input19c.txt", 1048576000)}")
}

fun run19c(inputFilename: String, times: Int) {
    val input = File(inputFilename).readLines()
    val operations = input.first().toList()
    var grid = Utils.readAsGrid(inputFilename, 2..input.size - 1) { it }
    var transformMatrix = Utils.readAsGrid(inputFilename, 2..input.size - 1) { it }.mapValues { it.key }.toMutableMap()

    val maxX = transformMatrix.keys.maxOf { it.x }
    val maxY = transformMatrix.keys.maxOf { it.y }
    var operationsPtr = 0 // Reset each time
    for (x in 1..maxX - 1) {
        for (y in 1..maxY - 1) {
            rotate(operations[operationsPtr], x, y, transformMatrix)
            operationsPtr = (operationsPtr + 1) % operations.size
        }
    }

    times.toString(2).reversed().forEach {ch ->
        if (ch == '1') {
            grid = applyTransformationMatrix(grid, transformMatrix)
        }
        transformMatrix = applyTransformationMatrix(transformMatrix, transformMatrix)
    }
    Utils.printGrid(grid)
}

fun run19(inputFilename: String, times: Int) {
    val input = File(inputFilename).readLines()
    val operations = input.first().toList()
    val grid = Utils.readAsGrid(inputFilename, 2..input.size - 1) { it }

    val maxX = grid.keys.maxOf { it.x }
    val maxY = grid.keys.maxOf { it.y }
    repeat(times) {
        var operationsPtr = 0 // Reset each time
        for (x in 1..maxX - 1) {
            for (y in 1..maxY - 1) {
                rotate(operations[operationsPtr], x, y, grid)
                operationsPtr = (operationsPtr + 1) % operations.size
            }
        }
    }
    Utils.printGrid(grid)
}

fun <T> applyTransformationMatrix(grid: MutableMap<Coord, T>, transform: Map<Coord, Coord>): MutableMap<Coord, T> {
    val new = mutableMapOf<Coord, T>()
    transform.forEach { originalCoord, newCoord ->
        new[originalCoord] = grid[newCoord]!!
    }
    return new
}

fun <T> rotate(op: Char, x: Int, y: Int, grid: MutableMap<Coord, T>) {
    val rotations = when (op) {
        'R' -> 1
        'L' -> 7
        else -> throw Exception()
    }
    repeat(rotations) {
        val new = mutableMapOf<Coord, T>()
        val buffer = listOf(
            Coord(x - 1, y - 1),
            Coord(x - 1, y),
            Coord(x - 1, y + 1),
            Coord(x, y + 1),
            Coord(x + 1, y + 1),
            Coord(x + 1, y),
            Coord(x + 1, y - 1),
            Coord(x, y - 1)
        )
        for (i in buffer.indices) {
            new[buffer[(i + 1) % buffer.size]] = grid[buffer[i]]!!
        }
        new[Coord(x, y)] = grid[Coord(x, y)]!!
        grid.putAll(new)
    }
}



