import Utils.freqCount
import java.io.File
import kotlin.math.max


fun main() {

    // Generate input datastructures
    val input = File("inputs/input16a.txt").readLines()
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
    var scoreCounter = 0
    repeat(100) {
        var line = ""
        for (idx in amounts.indices) {
            positions[idx] = (positions[idx] + amounts[idx]) % slots[idx].size
            line += slots[idx][positions[idx]]
            scoreCounter += scoreLine(line)
        }
        if (it == 99) {
            println("Part A: $line")
        }
    }
    println("Part B?: $scoreCounter")
}

fun scoreLine(line: String): Int {
    val freqCounts = line.toList().freqCount()
    return freqCounts.values.sumOf { max(it - 2, 0) }
}