import java.io.File

fun main() {
    val inputA = File("inputs/input1.txt").readLines()[0].chunked(1)
    val inputB = File("inputs/input1.txt").readLines()[1].chunked(2)
    val inputC = File("inputs/input1.txt").readLines()[2].chunked(3)
    println("Part A: ${inputA.sumOf { it.chunkedPotionCount() }}")
    println("Part B: ${inputB.sumOf { it.chunkedPotionCount() }}")
    println("Part C: ${inputC.sumOf { it.chunkedPotionCount() }}")
}

fun String.chunkedPotionCount(): Int {
    val xCount = this.count { it == 'x' }
    val nonX = this.length - xCount
    return nonX * (nonX - 1) + this.sumOf { it.toPotions() }
}

fun Char.toPotions(): Int {
    return when (this) {
        'A' -> 0
        'B' -> 1
        'C' -> 3
        'D' -> 5
        'x' -> 0
        else -> throw Exception()
    }
}