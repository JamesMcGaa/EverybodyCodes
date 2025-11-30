import Utils.Coord
import java.io.File

fun main() {
    println("Part A: ${p3a()}")
    println("Part B: ${p3b()}")
    println("Part C: ${p3c()}")
}

fun p3c(): Long {
    val input = File("inputs/input3c.txt").readLines()
    val emptyIdx = input.indexOfFirst { it.isBlank() }
    val dice = input.subList(0, emptyIdx).map {
        parseLine(it)
    }
    val grid = Utils.readAsGrid("inputs/input3c.txt", emptyIdx + 1 .. input.lastIndex, { it.digitToInt().toLong() })

    val allSeen = mutableSetOf<Coord>()

    dice.forEach { die ->
        var frontier = grid.keys.toMutableSet()
        while (frontier.isNotEmpty()) {
            val roll = die.roll()
            val newFrontier = mutableSetOf<Coord>()
            frontier.forEach { frontierNode ->
                if (frontierNode in grid.keys && grid[frontierNode] == roll) {
                    allSeen.add(frontierNode)
                    newFrontier.addAll(frontierNode.manhattanNeighbors union setOf(frontierNode))
                }
            }
            frontier = newFrontier
        }
    }

    return allSeen.size.toLong()
}


fun p3b(): String {
    val input = File("inputs/input3b.txt").readLines()
    val dice = input.subList(0, input.size - 2).map {
        parseLine(it)
    }

    val track = input.last().toCharArray().map { it.digitToInt().toLong() }
    return dice.sortedBy { it.rollsToFinish(track) }.map { it.id }.joinToString(separator = ",")
}

fun p3a(): Long {
    val dice = File("inputs/input3a.txt").readLines().map {
        parseLine(it)
    }

    var rollCtr = 0L
    var scoreCtr = 0L
    while (scoreCtr < 10000L) {
        rollCtr++
        dice.forEach {
            scoreCtr += it.roll()
        }
    }
    return rollCtr
}

class Dice(val id: Long, val faces: List<Long>, val seed: Long) {
    private var facePtr: Int = 0
    private var rollCounter: Long = 0
    var pulse: Long = seed

    val rollsToFinishMemo = mutableMapOf<List<Long>, Long>()

    fun rollsToFinish(track: List<Long>): Long {
        if (track in rollsToFinishMemo) {
            return rollsToFinishMemo[track]!!
        }
        var trackPtr = 0
        var rollCtr = 0L
        while (trackPtr in track.indices) {
            val result = roll()
            rollCtr++
            if (result == track[trackPtr]) {
                trackPtr++
            }
        }
        rollsToFinishMemo[track] = rollCtr
        return rollCtr
    }

    fun roll(): Long {
        rollCounter++
        val spin = rollCounter * pulse
        facePtr = Math.floorMod(facePtr + spin, faces.size)

        pulse += spin
        pulse %= seed
        pulse += 1 + rollCounter + seed

        return faces[facePtr]
    }
}

fun parseLine(input: String): Dice {
    // Courtesy Gemini
    val regex = "(.*?): faces=\\[(.*?)]\\s+seed=(\\d+)".toRegex()
    val matchResult = regex.find(input)!!
    val id = matchResult.groupValues[1].toLong()
    val faces = matchResult.groupValues[2].split(',').map { it.toLong() }
    val seed = matchResult.groupValues[3].toLong()

    return Dice(id = id, faces = faces, seed = seed)
}