import java.io.File
import kotlin.math.absoluteValue
import kotlin.math.roundToInt


fun main() {
    val part4a = File("inputs/input4a.txt").readLines().map { it.toInt() }
    val p4a = part4a.sum() - part4a.min() * part4a.size

    val part4b = File("inputs/input4b.txt").readLines().map { it.toInt() }
    val p4b = part4b.sum() - part4b.min() * part4b.size

    val part4c = File("inputs/input4c.txt").readLines().map { it.toLong() }
    val average = part4c.median().roundToInt()
    val p4c = part4c.sumOf { (it - average).absoluteValue }

    println("Part 4A: $p4a")
    println("Part 4B: $p4b")
    println("Part 4C: $p4c")
}

fun Collection<Long>.median(): Double {
    val sorted = this.sorted()
    return when {
        sorted.size % 2 == 0 -> {
            val middle1 = sorted[sorted.size / 2 - 1]
            val middle2 = sorted[sorted.size / 2]
            (middle1 + middle2) / 2.0
        }
        else -> sorted[sorted.size / 2].toDouble()
    }
}