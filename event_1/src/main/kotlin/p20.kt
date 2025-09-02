import Utils.Direction
import Utils.Coord
import kotlin.math.max

fun main() {
    println("Part A: ${runP20a("inputs/input20a.txt")}")
    println("Part B: ${runP20b("inputs/input20b.txt")}")
    println("Part C: ${runP20c()}")
}

data class P20AState(
    val depth: Int,
    val pos: Coord,
    val direction: Direction,
)

data class P20BState(
    val depth: Int,
    val pos: Coord,
    val direction: Direction,
    val todo: String,
)

/**
 * Via inspection of the input
 */
fun runP20c(): Int {
    var altitude = 384400 - 2
    var amount = listOf(-1,1,-1,-1,-1,1,-1,-1,-1,1,-1,-1)
    var amountCtr = 0
    var distSouth = 0
    while (altitude > 0) {
        amountCtr = (amountCtr + 1) % amount.size
        altitude += amount[amountCtr]
        distSouth += 1
    }
    return distSouth
}

fun runP20b(filename: String): Int {
    val grid = Utils.readAsGrid(filename, null) { it }
    val start = Utils.findCoord('S', grid)
    var memo = mutableMapOf(P20BState(0, start, Direction.DOWN, "ABCS") to 10000)

    for(iterations in 1..1000000) {
        var newMemo = mutableMapOf<P20BState, Int>()
        memo.forEach {
                oldMemo ->
            forloop@for (direction in Direction.entries) {
                if (direction != oldMemo.key.direction.ccw().ccw()) {
                    val projCoord = oldMemo.key.pos.moveDir(direction)
                    if (grid[projCoord] !in setOf(null, '#') ) {
                        if (grid[projCoord] == 'S') {
                            if (oldMemo.value > 10000 && oldMemo.key.todo == "S") {
                                return iterations
                            } else {
                                continue@forloop
                            }
                        }
                        val match = oldMemo.key.todo.first() == grid[projCoord]
                        val remaining = if (match) oldMemo.key.todo.substring(1) else oldMemo.key.todo
                        val newState = P20BState(oldMemo.key.depth + 1, projCoord, direction, remaining)
                        newMemo[newState] = max(
                            newMemo.getOrDefault(newState, 0), oldMemo.value + elevationScore(grid[projCoord]!!)
                        )
                    }
                }
            }
        }
        memo = newMemo
    }
    return -1
}

fun runP20a(filename: String): Int {
    val grid = Utils.readAsGrid(filename, null) { it }
    val start = Utils.findCoord('S', grid)
    var memo = mutableMapOf(P20AState(0, start, Direction.DOWN) to 1000)

    repeat(100) {
        var newMemo = mutableMapOf<P20AState, Int>()
        memo.forEach {
            oldMemo ->
            for (direction in Direction.entries) {
                if (direction != oldMemo.key.direction.ccw().ccw()) {
                    val projCoord = oldMemo.key.pos.moveDir(direction)
                    if (grid[projCoord] !in setOf(null, '#', 'S') ) {
                        val newState = P20AState(oldMemo.key.depth + 1, projCoord, direction)
                        newMemo[newState] = max(
                            newMemo.getOrDefault(newState, 0), oldMemo.value + elevationScore(grid[projCoord]!!)
                        )
                    }
                }
            }
        }
        memo = newMemo
    }

    return memo.values.max()
}

fun elevationScore(char: Char): Int {
    return when(char) {
        '-' -> -2
        '.', 'A', 'B', 'C', 'S' -> -1 // S here?
        '+' -> 1
        else -> throw Exception()
    }
}