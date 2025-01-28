fun main() {
//    val given = 4098675L // Input
//    var width = 1L
//    while (pyramidSum(width) < given) {
//        width += 2L
//    }
//    println("Part A: ${width * (pyramidSum(width) - given)}")

    var thickness = 1L
    var priests = 375 // Input
    val acolytes = 1111
    val givenB = 20240000L
    var widthB = 1L
    var soFar = 1L
    while (soFar < givenB) {
        widthB += 2L
        thickness = (thickness * priests) % acolytes
        soFar += widthB * thickness
    }
    val needed = soFar - givenB
    println(needed * widthB)
}

fun pyramidSum(width: Long): Long {
    return sumOfConseq(width) - 2L * sumOfConseq((width - 1L) / 2L)
}

fun sumOfConseq(limit: Long): Long {
    return limit * (limit + 1L) / 2L
}