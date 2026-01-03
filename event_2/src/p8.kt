import java.io.File
import kotlin.math.abs
import Utils.Coord
import kotlin.math.max
import kotlin.math.min

fun main() {
    println("Part A: ${p8a("inputs/input8a.txt")}")
    println("Part B: ${p8bc("inputs/input8b.txt")}")
    println("Part C: ${p8bc("inputs/input8c.txt", isPartC = true)}")
}

fun p8a(filename: String): Long {
    val input = File(filename).readLines().first()
        .split(",").map { it.toLong() }
    var counter = 0L
    for (i in 0..input.size - 2) {
        if (abs(input[i] - input[i + 1]) == 16L) {
            counter += 1
        }
    }
    return (0..input.size - 2).count { idx ->
        abs(input[idx] - input[idx + 1]) == 16L
    }.toLong()
}

fun p8bc(filename: String, isPartC: Boolean = false): Int {
    val input = File(filename).readLines().first()
        .split(",").map { it.toInt() }
    val coords = input.windowed(size = 2).map { Coord(it.first(), it.last()) }
    val pastCoords = mutableListOf<Coord>()
    val intersections = coords.sumOf { coord ->
        pastCoords.add(coord)
        pastCoords.count { it.intersects(coord) }
    }

    if (isPartC) {
        val candidates = mutableListOf<Coord>()
        for (a in 1..256) {
            for (b in 1..256) {
                candidates.add(Coord(a, b))
            }
        }
        return candidates.maxOf { candidate ->
            coords.count { existingCoord ->
                existingCoord.intersects(candidate)
            }
        }
    } else {
        return intersections
    }
}

fun Coord.intersects(other: Coord): Boolean {
    if (setOf(this.x, this.y, other.x, other.y).size != 4) {
        return false
    }
    val bottom = min(this.x, this.y)
    val top = max(this.x, this.y)
    val range = bottom + 1..<top

    return setOf(other.x in range, other.y in range).size == 2
}