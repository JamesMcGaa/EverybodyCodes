fun main() {
    val given = 4098675L
    var width = 1L
    while (pyramidSum(width) < given) {
        width += 2L
    }
    println("Part A: ${width * (pyramidSum(width) - given)}")
}

fun pyramidSum(width: Long): Long {
    return sumOfConseq(width) - 2L * sumOfConseq((width - 1L) / 2L)
}

fun sumOfConseq(limit: Long): Long {
    return limit * (limit + 1L) / 2L
}