import java.io.File
import kotlin.math.max

const val MODE_A = 1
const val MODE_B = 2
const val MODE_C = 3

fun main() {
    println("Part A: ${run5(10, "inputs/input5a.txt", MODE_A)}")
    println("Part B: ${run5(null, "inputs/input5b.txt", MODE_B)}")
    println("Part C: ${run5(null, "inputs/input5c.txt", MODE_C)}")
}

fun run5(rounds: Int?, inputFilename: String, mode: Int): String {
    var columns = mutableListOf<MutableList<Int>>()
    File(inputFilename).readLines().forEach { line ->
        val nums = line.split(" ").map { it.toInt() }
        nums.forEachIndexed { index, value ->
            if (index !in columns.indices) {
                columns.add(mutableListOf())
            }
            columns[index].add(value)
        }
    }

    val numColumns = columns.size
    var c = 0

    fun executeRound() {
        val popped = columns[c].removeFirst()
        val colsize = columns[(c + 1) % numColumns].size
        val idx = (popped - 1) % (2 * colsize) // Subtract 1 because lists are 0-indexed, and claps are not
        val indices = (0..colsize - 1).toList() + (colsize downTo 1).toList()
        columns[(c + 1) % numColumns].add(indices[idx], popped)
        c = (c + 1) % numColumns
    }

    when (mode) {
        MODE_A -> {
            repeat(rounds!!) {
                executeRound()
            }
            return columns.map { it.first() }.joinToString("")
        }

        MODE_B -> {
            var rounds = 0L
            val wordToCounter = mutableMapOf<String, Int>()
            while (true) {
                rounds++
                executeRound()
                val word = columns.map { it.first() }.joinToString("")
                wordToCounter[word] = wordToCounter.getOrDefault(word, 0) + 1
                if (wordToCounter[word] == 2024) {
                    return (rounds * word.toInt()).toString()
                }
            }
        }

        MODE_C -> {
            var highestSoFar = 0L
            val seen = mutableSetOf<Int>()
            while (true) {
                executeRound()
                val hash = Pair(c, columns).hashCode()
                if (hash in seen) {
                    return highestSoFar.toString()
                }
                highestSoFar = max(highestSoFar, columns.map { it.first() }.joinToString("").toLong())
                seen.add(hash)
            }
        }
        else -> throw Exception()
    }
}