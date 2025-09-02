import Utils.Coord
import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main() {
    println("Part A: ${p12("inputs/input12a.txt")}")
    println("Part B: ${p12("inputs/input12b.txt")}")
    println("Part C: ${p12c("inputs/input12c.txt")}")
}

class Shot(
    cardinality: Int,
    var pos: Coord,
    val power: Int,
    var phase: Int,
    var phaseProgress: Int = 0
) {
    val rankingValue = cardinality * power

    fun move(maxXCurrent: Int, maxYCurrent: Int): Boolean {
        val directionalCoord = when (phase) {
            0 -> {
                Coord.ORIGIN.downRight
            }
            1 -> {
                Coord.ORIGIN.down
            }
            2 -> {
                Coord.ORIGIN.downLeft
            }
            else -> throw Exception()
        }
        pos += directionalCoord
        phaseProgress += 1
        if (phase < 2 && phaseProgress == power) {
            phaseProgress = 0
            phase += 1
        }
        if (pos.y == 0 || pos.x > maxXCurrent || pos.y > maxYCurrent) {
            return true
        }
        return false
    }
}

fun p12c(filename: String): Int {
    val meteors = File(filename).readLines().map {
        val nums = it.split(" ").map { it.toInt() }
        Coord(nums[0], nums[1]) // Flipped
    }.toMutableList() // Have to use a list here since Coords are dataclasses, and we are going to be modifying them

    // Populate our min score to hit each point in a grid
    val coordToMinScore = mutableMapOf<Coord, Int>()
    val canonMap = mapOf('A' to Coord(0, 0), 'B' to Coord(0, 1), 'C' to Coord(0, 2))
    val maximumMeteorVal = meteors.maxOf { max(it.x, it.y) }
    val shots = mutableSetOf<Shot>()
    canonMap.entries.forEach { cannon ->
        val cardinality = (cannon.key - 'A' + 1)
        for (shootingPower in 1..maximumMeteorVal) {
            shots.add(Shot(cardinality, cannon.value, shootingPower, 0))
        }
    }

    var maxX = meteors.maxOf {it.x}
    var maxY = meteors.maxOf {it.y}
    var counter = 0
    while (meteors.isNotEmpty() || shots.isNotEmpty()) {
        maxX -= 1
        maxY -= 1
        val shotsToRemove = mutableSetOf<Shot>()
        shots.forEach { shot ->
            val finished = shot.move(maxX, maxY)
            if (finished) {
                shotsToRemove.add(shot)
            }
            coordToMinScore[shot.pos] = min(coordToMinScore.getOrDefault(shot.pos, Int.MAX_VALUE), shot.rankingValue)
        }
        shots.removeAll(shotsToRemove)

        val meteorsToRemove = mutableSetOf<Coord>()
        meteors.forEach { meteor ->
            meteor.x -= 1
            meteor.y -= 1
            if (meteor in coordToMinScore) {
                counter += coordToMinScore[meteor]!!
                meteorsToRemove.add(meteor)
            }
            else if (meteor.x == 0 || meteor.y == 0) {
                throw Exception("Should have shot down all meteors")
            }
        }
        meteors.removeAll(meteorsToRemove)
    }


    return counter
}

fun p12(filename: String): Int {
    val grid = Utils.readAsGrid(filename, null) { it }
    val groundLevel = grid.filterValues { it == '=' }.keys.first().x
    grid.forEach { key, value -> key.x = groundLevel - key.x }
    val cannonMap = listOf('A', 'B', 'C').associate { ch ->
        Pair(ch, grid.filterValues { it == ch }.keys.first())
    }
    val targets = grid.filterValues { it in setOf('T', 'H') }

    return targets.entries.sumOf { target ->
        val res = cannonMap.entries.sumOf { cannon ->
            shootingPower(cannon.value, target.key) * (cannon.key - 'A' + 1) * shotsToDestroy(target.value)
        }
        res
    }
}

fun shotsToDestroy(char: Char): Int {
    return when (char) {
        'H' -> 2
        'T' -> 1
        else -> throw Exception("reeee")
    }
}

fun shootingPower(cannon: Coord, target: Coord): Int {
    val horzDisplacement = target.y - cannon.y
    val excessVertical = cannon.x - target.x
    // Every extra displacement should have an extra vertical cannon height

    // TODO ensure minimum dist
    if (Math.floorMod(horzDisplacement, 3) != Math.floorMod(excessVertical, 3)) {
        return 0
    }
    return (horzDisplacement - excessVertical) / 3
}