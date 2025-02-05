import Utils.freqCount
import java.io.File
import kotlin.math.max

enum class Part {
    A, B, C
}

fun main() {
    run16("inputs/input16a.txt", Part.A)
    run16("inputs/input16b.txt", Part.B)
    run16("inputs/input16c.txt", Part.C)
}

fun run16(filename: String, part: Part) {
    // Generate input datastructures
    val input = File(filename).readLines()
    val amounts = input.first().split(",").map { it.toInt() }
    val slots = mutableListOf<MutableList<String>>()
    repeat(amounts.size) { slots.add(mutableListOf()) }
    input.subList(2, input.size).forEach { line ->
        for (i in 0 until amounts.size) {
            val start = line.getOrNull(4 * i)
            if (start != null && start != ' ') {
                slots[i].add(line.substring(4 * i, 4 * i + 3))
            }
        }
    }
    val positions = MutableList(amounts.size) { 0 }

    // Spin
    if (part in setOf(Part.A, Part.B)) {
        var scoreCounter = 0
        var iterations = 0
        val stateToIdx = mutableMapOf(positions to iterations)
        val idxToCumulative = mutableMapOf(iterations to scoreCounter)
        while (true) {
            var line = ""
            for (idx in amounts.indices) {
                positions[idx] = (positions[idx] + amounts[idx]) % slots[idx].size
                line += slots[idx][positions[idx]]
            }
            scoreCounter += scoreLine(line, part)
            iterations++

            if (part == Part.A && iterations == 100) {
                println("Part A: ${line.chunked(3).joinToString(" ")}")
                return
            }

            if (part == Part.B && positions in stateToIdx) {
                val partBCycleLen = 202420242024L
                val cycleLen = iterations - stateToIdx[positions]!!
                val cycleAmount = scoreCounter - idxToCumulative[stateToIdx[positions]]!!
                val partB =
                    (partBCycleLen / cycleLen) * cycleAmount + idxToCumulative[(partBCycleLen % cycleLen).toInt()]!!
                println("Part B: $partB")
                return
            }

            stateToIdx[positions] = iterations
            idxToCumulative[iterations] = scoreCounter
        }
    }

    // DP (part C)
    fun bestPossibleDP(
        depth: Int,
        pos: List<Int>,
        isMax: Boolean,
        DP: MutableMap<Pair<Int, List<Int>>, Int> = mutableMapOf<Pair<Int, List<Int>>, Int>()
    ): Int {
        val key = Pair(depth, pos)
        if (key in DP) {
            return DP[key]!!
        }

        if (depth == 0) {
            return 0
        }

        val lower = mutableListOf<Int>()
        val upper = mutableListOf<Int>()
        val same = mutableListOf<Int>()
        pos.forEachIndexed { index, i ->
            lower.add((i + slots[index].size - 1 + amounts[index]) % slots[index].size)
            upper.add((i + slots[index].size + 1 + amounts[index]) % slots[index].size)
            same.add((i + slots[index].size + amounts[index]) % slots[index].size)
        }

        val best = if (isMax) {
            listOf(lower, upper, same).maxOf { newPos ->
                var line = ""
                newPos.forEachIndexed { index, i -> line += slots[index][newPos[index]] }
                bestPossibleDP(depth - 1, newPos, isMax, DP) + scoreLine(line, part)
            }
        } else {
            listOf(lower, upper, same).minOf { newPos ->
                var line = ""
                newPos.forEachIndexed { index, i -> line += slots[index][newPos[index]] }
                bestPossibleDP(depth - 1, newPos, isMax, DP) + scoreLine(line, part)
            }
        }

        DP[key] = best
        return best
    }
    println("Part C: ${bestPossibleDP(256, positions, true)}, ${bestPossibleDP(256, positions, false)}")

}

fun scoreLine(line: String, part: Part): Int {
    var newLine = ""
    if (part == Part.A) {
        newLine += line
    } else {
        line.forEachIndexed { index, ch ->
            if (index % 3 != 1) {
                newLine += ch
            }
        }
    }
    val freqCounts = newLine.toList().freqCount()
    return freqCounts.values.sumOf { max(it - 2, 0) }
}