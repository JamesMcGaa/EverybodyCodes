import java.io.File

fun main() {
    println("Part A: ${p3a()}")
    println("Part B: ${p3bb("inputs/input3b.txt")}")
    println("Part C: ${p3bb("inputs/input3c.txt")}")
}

fun readInput(filename: String): List<List<Long>> {
    return File(filename).readLines().map { line ->
        line.split(" ").map { it.split("=").last().toLong() }
    }
}

fun p3a(): Long {
    val pairs = readInput("inputs/input3a.txt")

    var counter = 0L
    pairs.forEach { pair ->
        val modularity = pair.sum() - 1
        val endingX = Math.floorMod(((pair.first() - 1) + 100), modularity) + 1
        val endingY = Math.floorMod(((pair.last() - 1) - 100), modularity) + 1
        counter += endingX + 100 * endingY
    }
    return counter
}

fun p3bb(filename: String): Long {
    val pairs = readInput(filename)

    val crtCoefficients = mutableListOf<Pair<Long, Long>>()
    pairs.forEach { pair ->
        val modularity = pair.sum() - 1
        // y_literal - time = 1 mod modularity
        // time = y_literal - 1 mod modularity
        crtCoefficients.add(Pair(pair.last() - 1, modularity))
    }
    return crtSolve(crtCoefficients)
}

fun crtSolve(equations: List<Pair<Long, Long>>): Long {
    val N = equations.map { it.second }.reduce { acc, i -> acc * i }
    return equations.sumOf { pair ->
        val a_i = pair.first
        val n_i = pair.second
        val y_i = N / n_i
        val z_i = modInverse(y_i, n_i)
        a_i * y_i * z_i!! % N
    } % N
}

fun extendedGcd(a: Long, b: Long): Triple<Long, Long, Long> {
    if (b == 0L) return Triple(a, 1L, 0L)
    val (g, x1, y1) = extendedGcd(b, a % b)
    val x = y1
    val y = x1 - (a / b) * y1
    return Triple(g, x, y)
}

fun modInverse(a: Long, m: Long): Long? {
    val (g, x, _) = extendedGcd(a, m)
    if (g != 1L) return null
    return Math.floorMod(x, m)
}