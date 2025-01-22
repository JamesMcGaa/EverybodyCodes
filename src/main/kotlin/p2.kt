import Utils.Coord
import Utils.Coord.Companion.DOWN_COORD
import Utils.Coord.Companion.LEFT_COORD
import Utils.Coord.Companion.RIGHT_COORD
import Utils.Coord.Companion.UP_COORD
import java.io.File

fun main() {
    val input2a = File("inputs/input2a.txt").readLines()
    val runesA = input2a[0].removePrefix("WORDS:").split(",")
    val targetA = input2a[2]
    val partA = runesA.sumOf { rune ->
        val regex = Regex(rune)
        regex.findAll(targetA).toList().size
    }
    println("Part A: $partA")

    val input2b = File("inputs/input2b.txt").readLines()
    val runesB = input2b[0].removePrefix("WORDS:").split(",")
    val targetsB = input2b.subList(2, input2b.size)
    val partB = targetsB.sumOf { target ->
        val shadowTarget = target.reversed()
        val bannedIndices = mutableSetOf<Int>()
        runesB.forEach { rune ->
            val regex = Regex(rune)
            regex.findAll(target).toList().forEach { matchResult ->
                bannedIndices.addAll(matchResult.range)
            }
            regex.findAll(shadowTarget).toList().forEach { matchResult ->
                bannedIndices.addAll(
                    mirrorIndex(
                        shadowTarget,
                        matchResult.range.endInclusive
                    )..mirrorIndex(shadowTarget, matchResult.range.start)
                )
            }
        }
        bannedIndices.size
    }
    println("Part B: $partB")

    val input2c = File("inputs/input2c.txt").readLines()
    val runesC = input2c[0].removePrefix("WORDS:").split(",")
    val targetsC = Utils.readAsGrid("inputs/input2c.txt", 2 until input2c.size) { it }
    val maxY = targetsC.keys.maxOf { it.y }

    val markedCoords = mutableSetOf<Coord>()
    runesC.forEach { rune ->
        val runeLen = rune.length
        targetsC.forEach { coord, _ ->
            listOf(LEFT_COORD, RIGHT_COORD, DOWN_COORD, UP_COORD).forEach direction@{ direction ->
                var str = ""
                var current = coord
                val coordSet = mutableSetOf<Coord>()
                repeat(runeLen) {
                    if (current !in targetsC) {
                        return@direction
                    }
                    str += targetsC[current]
                    coordSet.add(current)
                    current += direction
                    if (direction in listOf(LEFT_COORD, RIGHT_COORD)) {
                        current.y = (current.y + (maxY + 1)) % (maxY + 1) // Loop round horizontally
                    }
                }
                if (str == rune) {
                    markedCoords.addAll(coordSet)
                }
            }
        }
    }
    println("Part C: ${markedCoords.size}")
}

fun mirrorIndex(str: String, idx: Int): Int {
    return str.length - 1 - idx
}

