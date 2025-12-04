import Utils.freqCount
import java.io.File

fun main() {
    println("Part A: ${p3a("inputs/input3a.txt")}")
    println("Part B: ${p3b("inputs/input3b.txt")}")
    println("Part C: ${p3c("inputs/input3c.txt")}")
}

fun p3a(filename: String): Long {
    val input = File(filename).readLines().first()
        .split(",").map { it.toLong() }
    return input.toSet().sum()
}

fun p3b(filename: String): Long {
    val input = File(filename).readLines().first()
        .split(",").map { it.toLong() }
    return input.toSet().sorted().subList(0, 20).sum()
}

fun p3c(filename: String): Long {
    val input = File(filename).readLines().first()
        .split(",").map { it.toLong() }
    return input.freqCount().values.max().toLong()
}