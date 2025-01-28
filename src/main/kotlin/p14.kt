import Utils.Coord
import java.io.File
import kotlin.math.absoluteValue
import kotlin.math.max


fun main() {
    val commands = File("inputs/input14a.txt").readLines().first().split(",")
    val grid = mutableSetOf<Coord>()
    var current = Coord(0, 0)
    var currentHeight = 0
    var maxHeight = 0
    commands.forEach {
        val dir = it.first()
        val amount = it.removePrefix(dir.toString()).toInt()
        val dirCoord = when (dir) {
            'U' -> Coord.UP_COORD
            'D' -> Coord.DOWN_COORD
            'L' -> Coord.LEFT_COORD
            'R' -> Coord.RIGHT_COORD
            'F' -> Unit
            'B' -> Unit
            else -> throw Exception()
        }

        if (dir == 'U') {
            currentHeight += amount
            maxHeight = max(maxHeight, currentHeight)
        } else if (dir == 'D') {
            currentHeight -= amount
        }

//        repeat(amount) {
//            current += dirCoord
//            grid.add(current)
//        }
    }
//    println("Part A: ${grid.minOf { it.x }.absoluteValue}")
    println("Part A: $maxHeight")
}