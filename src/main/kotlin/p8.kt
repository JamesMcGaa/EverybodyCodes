import kotlin.math.max

fun main() {
    println("Part A: ${run8a()}")
    println("Part B: ${run8b()}")
    println("Part C: ${run8c()}")
}

fun run8c(): Long {
    var thickness = 1L
    var priests = 965917L // Input
    val acolytes = 10L
    val givenC = 202400000L
    var width = 1L
    val heights = ArrayDeque<Long>(listOf(1L))
    val minHeights = ArrayDeque<Long>(listOf(1L))
    while (true) {
        width += 2L
        heights.addFirst(0L)
        heights.addLast(0L)
        thickness = (thickness * priests) % acolytes + acolytes
        for (index in heights.indices) {
            heights[index] += thickness
        }
        minHeights.addFirst(thickness)
        minHeights.addLast(thickness)

        var totalUsed = 0L
        for (index in heights.indices) {
            val minHeight = if (index in setOf(
                    minHeights.indices.first,
                    minHeights.indices.last
                )
            ) minHeights[index] else minHeights[index] + 1
            var removal = (priests * width * heights[index]) % acolytes
            totalUsed += max(minHeight, heights[index] - removal)
        }
        if (totalUsed > givenC) {
            return totalUsed - givenC
        }
    }
}

fun run8a(): Long {
    val givenA = 4098675L // Input
    var width = 1L
    while (pyramidSum(width) < givenA) {
        width += 2L
    }
    return width * (pyramidSum(width) - givenA)
}


fun run8b(): Long {
    var thickness = 1L
    var priests = 375L // Input
    val acolytes = 1111
    val givenB = 20240000L
    var width = 1L
    var soFar = 1L
    while (soFar < givenB) {
        width += 2L
        thickness = (thickness * priests) % acolytes
        soFar += width * thickness
    }
    val needed = soFar - givenB
    return needed * width
}

fun pyramidSum(width: Long): Long {
    return sumOfConseq(width) - 2L * sumOfConseq((width - 1L) / 2L)
}

fun sumOfConseq(limit: Long): Long {
    return limit * (limit + 1L) / 2L
}