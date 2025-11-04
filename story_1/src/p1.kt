@file:Suppress("LocalVariableName")

import java.io.File


fun main() {
    println("Part A: ${runForEni("inputs/input1a.txt", ::eniA)}")
    println("Part B: ${runForEni("inputs/input1b.txt", ::eniB)}")
    println("Part C: ${runForEni("inputs/input1c.txt", ::eniC)}")
}

private fun runForEni(filename: String, eniFun: (Long, Long, Long) -> Long): Long {
    val input = File(filename).readLines().map { line ->
        line.split(" ").map { token -> token.split("=").last().toLong() }
    }

    val results = input.map { line ->
        val A = line[0]
        val B = line[1]
        val C = line[2]
        val X = line[3]
        val Y = line[4]
        val Z = line[5]
        val M = line[6]
        eniFun(A, X, M) + eniFun(B, Y, M) + eniFun(C, Z, M)
    }

    return results.max()
}

private fun eniA(n: Long, exp: Long, mod: Long): Long {
    val result = mutableListOf<String>()
    var current = 1L
    repeat(exp.toInt()) {
        current = (current * n) % mod
        result.add(current.toString())
    }
    return result.reversed().joinToString("").toLong()
}

private fun eniB(n: Long, exp: Long, mod: Long): Long {
    var result = mutableListOf<String>()
    val resultsToTimes = mutableMapOf<List<String>, Long>()
    val timesToResults = mutableMapOf<Long, List<String>>()
    var current = 1L
    for(times in 0 until exp) {
        current = (current * n) % mod
        result.add(current.toString())
        result = result.takeLast(5).toMutableList()
        if (resultsToTimes.containsKey(result.toList())) {
            val start = resultsToTimes[result.toList()]!!
            val delta = times - start
            val remainingCycles = exp - times - 1
            val progress = remainingCycles % delta

            return timesToResults[progress + start]!!.reversed().joinToString("").toLong()
        } else {
            resultsToTimes[result.toList()] = times
            timesToResults[times] = result.toList()
        }
    }
    return result.reversed().joinToString("").toLong()
}

private fun eniC(n: Long, exp: Long, mod: Long): Long {
    var counter = 0L
    val currentsToTimes = mutableMapOf<Long, Long>()
    val timesToCurrents = mutableMapOf<Long, Long>()
    var current = 1L
    for(times in 0 until exp) {
        current = (current * n) % mod
        counter += current

        if (currentsToTimes.containsKey(current)) {
            val start = currentsToTimes[current]!!
            val delta = times - start
            val remainingCycles = exp - times - 1
            val progress = remainingCycles % delta
            val fullLoopsToComplete = remainingCycles / delta

            var partialLoopSum = 0L
            for (i in 1 .. progress) {
                partialLoopSum += timesToCurrents[start + i]!!
            }
            var fullLoopSum = 0L
            for (i in start until times) {
                fullLoopSum += timesToCurrents[i]!!
            }

            return counter + partialLoopSum + fullLoopSum * fullLoopsToComplete
        } else {
            currentsToTimes[current] = times
            timesToCurrents[times] = current
        }
    }
    return counter
}