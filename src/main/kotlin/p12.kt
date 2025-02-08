import Utils.Coord
import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main() {
//    println("Part A: ${p12("inputs/input12a.txt")}")
//    println("Part B: ${p12("inputs/input12b.txt")}")
    println("Part C: ${p12c("inputs/input12c.txt")}")
}

fun p12c(filename: String): Int {
    val meteors = File(filename).readLines().map {
        val nums = it.split(" ").map { it.toInt() }
        Coord(nums[1], nums[0]) // Flipped
    }

    // Populate our min score to hit each point in a grid
    val coordToMinScore = mutableMapOf<Coord, Int>()
    val canonMap = mapOf('A' to Coord(0, 0), 'B' to Coord(1, 0), 'C' to Coord(2, 0))
    val maximumMeteorVal = meteors.maxOf { max(it.x, it.y) } / 5
    println(maximumMeteorVal)
    canonMap.entries.forEach { cannon ->
        println(cannon)
        for (shootingPower in 1..maximumMeteorVal) {
//            println(shootingPower)
            val rankingValue = shootingPower * (cannon.key - 'A' + 1)
            var current = cannon.value
            repeat(shootingPower) {
                current += Coord.ORIGIN.downRight
                coordToMinScore[current] = min(coordToMinScore.getOrDefault(current, Int.MAX_VALUE), rankingValue)
            }
            repeat(shootingPower) {
                current += Coord.ORIGIN.right
                coordToMinScore[current] = min(coordToMinScore.getOrDefault(current, Int.MAX_VALUE), rankingValue)
            }
            repeat(shootingPower) {
                current += Coord.ORIGIN.upRight
                coordToMinScore[current] = min(coordToMinScore.getOrDefault(current, Int.MAX_VALUE), rankingValue)
            }
        }
    }
    print(coordToMinScore.size)

    val res =  meteors.map { meteor ->
        val projectionDistance = max(meteor.x, meteor.y)
        (0..projectionDistance).map { meteor + Coord.ORIGIN.upLeft * it }.minOf {
            coordToMinScore.getOrDefault(it, Int.MAX_VALUE)
        }
    }
    println(res)
    return res.sum()
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