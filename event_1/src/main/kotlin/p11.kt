import Utils.freqCount
import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main() {
    println("Part A: ${run11("A", "inputs/input11a.txt", 4)}")
    println("Part B: ${run11("Z", "inputs/input11b.txt", 10)}")
    val tokens = File("inputs/input11c.txt").readLines().map { it.split(":").first() }
    var minTermites = Long.MAX_VALUE
    var maxTermites = Long.MIN_VALUE
    tokens.forEach {
        val result = run11(it, "inputs/input11c.txt", 20)
        minTermites = min(result, minTermites)
        maxTermites = max(result, maxTermites)
    }
    println("Part C: ${maxTermites - minTermites}")
}

fun run11(start: String, inputFilename: String, cycles: Int): Long {
    var current = mutableMapOf(start to 1L)
    val recipe = mutableMapOf<String, Map<String, Int>>()
    File(inputFilename).forEachLine {
        val start = it.split(":").first()
        val ends = it.split(":").last().split(",").freqCount()
        recipe[start] = ends
    }
    repeat(cycles) {
        val next = mutableMapOf<String, Long>()
        current.forEach { k, v ->
            recipe[k]!!.forEach { k2, v2 ->
                next[k2] = next.getOrDefault(k2, 0) + v * v2
            }
        }
        current = next
    }
    return current.values.sum()
}