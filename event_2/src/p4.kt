import java.io.File
import kotlin.math.ceil
import kotlin.math.floor

fun main() {
    println("Part A: ${p4a("inputs/input4a.txt")}")
    println("Part B: ${p4b("inputs/input4b.txt")}")
    println("Part C: ${p4c("inputs/input4c.txt")}")
}

fun p4a(filename: String): Long {
    val input = File(filename).readLines().map { it.toLong() }
    return floor(2025.0 * input.first() / input.last()).toLong()
}

fun p4b(filename: String): Long {
    val input = File(filename).readLines().map { it.toLong() }
    return ceil(10000000000000.0 * input.last() / input.first()).toLong()
}

fun p4c(filename: String): Long {
    val input = File(filename).readLines()
    var counter = 100.0
    val start = input.first().toLong()
    val end = input.last().toLong()
    val pairs = input.subList(1, input.size - 1).map { it.split("|").map { number -> number.toLong() } }
    counter *= start
    counter /= end
    pairs.forEach {
        counter /= it.first()
        counter *= it.last()
    }
    return floor(counter).toLong()
}

