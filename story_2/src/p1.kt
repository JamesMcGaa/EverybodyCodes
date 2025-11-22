import Utils.Coord
import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main() {
    println("Part A: ${run1("inputs/input1a.txt")}")
    println("Part B: ${run1("inputs/input1b.txt", shouldTakeMax = true)}")
    val ptC = run1c("inputs/input1c.txt")
    println("Part C: ${ptC.first} ${ptC.second}")
}

fun run1(inputPath: String, shouldTakeMax: Boolean = false): Int {
    val input = File(inputPath).readLines()
    val splitIdx = input.indexOfFirst { it.isBlank() }
    val coins = input.subList(splitIdx + 1, input.size)
    val grid = Utils.readAsGrid(inputPath, 0 until splitIdx, { it })

    var counter = 0

    coins.forEachIndexed { idx, coin ->
        if (shouldTakeMax) {
            val maxIdx = grid.keys.maxOf { it.y } / 2
            counter += (0..maxIdx).maxOf { idxToTry ->
                runCoin(
                    grid = grid, coin = coin, startPos = idxToTry * 2,
                )
            }
        } else {
            counter += runCoin(
                grid = grid, coin = coin, startPos = idx * 2,
            )
        }
    }

    return counter
}

fun run1c(inputPath: String): Pair<Int, Int> {
    val input = File(inputPath).readLines()
    val splitIdx = input.indexOfFirst { it.isBlank() }
    val coins = input.subList(splitIdx + 1, input.size)
    val grid = Utils.readAsGrid(inputPath, 0 until splitIdx, { it })
    // (Coin, start idx) -> value
    val memo = mutableMapOf<Pair<String, Int>, Int>()


    val maxIdx = grid.keys.maxOf { it.y } / 2
    val startIndices = (0..maxIdx).map { it * 2 }
    var globalMin = Int.MAX_VALUE
    var globalMax = Int.MIN_VALUE
    Utils.orderedSubsets(startIndices, 6).forEach { startPositions ->
        var totalForThisCombo = 0
        coins.forEachIndexed { idx, coin ->
            totalForThisCombo +=
                runCoin(grid, coin, startPositions[idx], memo)
        }
        globalMin = min(globalMin, totalForThisCombo)
        globalMax = max(globalMax, totalForThisCombo)
    }

    return Pair(globalMin, globalMax)

}

fun runCoin(
    grid: Map<Coord, Char>, coin: String, startPos: Int, memo: MutableMap<Pair<String, Int>, Int>? = null
): Int {
    val lookup = memo?.get(Pair(coin, startPos))
    if (lookup != null) {
        return lookup
    }

    val rightWall = grid.keys.maxOf { it.y }
    val bottomX = grid.keys.maxOf { it.x }
    var orderIdx = 0
    val pos = Coord(-1, startPos)
    while (pos.x < bottomX) {
        pos.x += 1
        if (grid[pos]!! == '*') {
            val shouldGoLeft = (coin[orderIdx] == 'L' && pos.y != 0) || pos.y == rightWall
            orderIdx += 1
            if (shouldGoLeft) {
                pos.y -= 1
            } else {
                pos.y += 1
            }
        }
    }

    val result = (2 * (pos.y / 2 + 1) - (startPos / 2 + 1)).coerceAtLeast(0)
    memo?.set(Pair(coin, startPos), result)
    return result
}

