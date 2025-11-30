import Utils.Coord
import java.io.File

fun main() {
//    println("Part A: ${p3a()}")
//    println("Part B: ${p3b()}")
    println("Part C: ${p3c()}")
}

fun p3c(): Long {
    val input = File("inputs/input3c.txt").readLines()
    val emptyIdx = input.indexOfFirst { it.isBlank() }
    val dice = input.subList(0, emptyIdx).map {
        parseLine(it)
    }
    val grid = Utils.readAsGrid("inputs/input3c.txt", emptyIdx + 1 .. input.lastIndex, { it.digitToInt().toLong() })

    val signatures = dice.map { die ->
        val path = mutableListOf<Long>()
        repeat(grid.size) {
            path.add(die.roll())
        }
        path.reversed().toMutableList()
    }

    val allSeen = mutableSetOf<Coord>()

    val queue = mutableSetOf<Pair<Coord, MutableList<Long>>>()
    signatures.forEach { signature ->
        grid.filterValues { it == signature.last() }.forEach {
            queue.add(Pair(it.key, signature))
        }
    }

    while (queue.isNotEmpty()) {
        val current = queue.random()
        queue.remove(current)
        val currentPos = current.first
        allSeen.add(currentPos)

        val newSignature = current.second.toMutableList()
        newSignature.removeLast()

        val legalMoves =
            (currentPos.manhattanNeighbors union setOf(currentPos)).filter { grid.containsKey(it) && grid[it] == newSignature.last() }
        queue.addAll(legalMoves.map { Pair(it, newSignature) })
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

//        println("$rollCounter, $spin, $pulse")

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
//    println("$id, $faces, $seed")
    return Dice(id = id, faces = faces, seed = seed)
}