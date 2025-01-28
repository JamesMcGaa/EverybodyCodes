import java.io.File
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.min

fun main() {
    println("Part A: ${run9(listOf(1L, 3L, 5L, 10L).sortedDescending(), "inputs/input9a.txt")}")
    println(
        "Part B: ${
            run9(
                listOf(1, 3, 5, 10, 15, 16, 20, 24, 25, 30).map { it.toLong() }.sortedDescending(),
                "inputs/input9b.txt"
            )
        }"
    )
    println(
        "Part C: ${
            run9(
                listOf(1, 3, 5, 10, 15, 16, 20, 24, 25, 30, 37, 38, 49, 50, 74, 75, 100, 101).map { it.toLong() }
                    .sortedDescending(),
                "inputs/input9c.txt", isPartC = true
            )
        }"
    )
}


fun run9(stamps: List<Long>, inputBrightnessesFilename: String, isPartC: Boolean = false): Long {
    val brightnesses = File(inputBrightnessesFilename).readLines().map { it.toLong() }

    val memo = mutableMapOf<Long, Long>()
    val prune = 3000000 // unused
    fun mcnuggetSolver(nuggets: List<Long>, total: Long, soFar: Long): Long {
        if (total == 0L) {
            return soFar
        }
        if (total < 0L || soFar > prune) {
            return Long.MAX_VALUE
        }
        if (total in memo) {
            return memo[total]!! + soFar
        }

        val solution = nuggets.minOf { mcnuggetSolver(nuggets, total - it, soFar + 1) }
        memo[total] = solution - soFar
        return solution
    }

    if (!isPartC) {
        return brightnesses.sumOf { mcnuggetSolver(stamps, it, 0L) }
    }

    (1..brightnesses.max()).map { mcnuggetSolver(stamps, it, 0L) }
    return brightnesses.sumOf {
        var minBeetles = Long.MAX_VALUE
        val radius = if (it % 2L == 0L) 50 else 49
        val low = floor(it / 2.0).toLong()
        val high = ceil(it / 2.0).toLong()
        for (r in 0..radius) {
            minBeetles = min(minBeetles, memo[low - r]!! + memo[high + r]!!)
        }
        minBeetles
    }
}