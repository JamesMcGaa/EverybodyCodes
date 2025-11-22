import java.io.File

fun main() {
    println("Part A: ${p3a()}")
    println("Part B: ${p3bc("inputs/input3b.txt")}")
    println("Part C: ${p3bc("inputs/input3c.txt")}")
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

fun p3bc(filename: String): Long {
    val pairs = readInput(filename)

    val crtCoefficients = mutableListOf<Pair<Long, Long>>()
    pairs.forEach { pair ->
        val modularity = pair.sum() - 1
        // y_literal - time = 1 mod modularity
        // time = y_literal - 1 mod modularity
        crtCoefficients.add(Pair(pair.last() - 1, modularity))
    }
    return Utils.crtSolve(crtCoefficients)
}
