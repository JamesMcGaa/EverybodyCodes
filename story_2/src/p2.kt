fun main() {
    println("Part A: ${p2a()}")
    println(
        "Part B: ${
            p2b(
                input = "RGRRBRBRBRBGRBGRRRRBBRGRRGRRGGBBBGBBRRRBRRGGGGGGRGGRBBRGRGGRRBRRRRRBGGBGBBBBRGGGBBRRRBBGRBBBBRRRGGRRBGRBGGGGBRRBBBBBBRRGBRRRBRRRRGGGBBRGBBBGGRBGBGRGRRGBBRRBRGRGBBGRGGRGBRBBRRBGRBBGBBBGBBGGBRBGRRRGRGRBBRBBBBGBBGBRRRBGGRBBRRRRRGRBBGGGRRRRGGBRRRRRRGRRGBBRBRRR",
                repeats = 100,
            )
        }"
    )
    println(
        "Part C: ${
            p2c(
                input = "BGGGRGGBBBBRGGRBBBBRGGRRRGRBGRGGGBBGGBBGGBBGGRRGGBBGBBGRRGRGRBRRRRRBGRRGRGRBBBGGGRGGRRBGGRBRRGRBBBGBBBGRBRRRGRBGGGRRGRGRRGRGGGBBRRRRBRGBBBGBBGRBGRRRRRGBGBBRGGRRGBRRBRRBBRBRGGBBBBBGGRBRRBBBRBGRBGBBGGBRRRGRBGGGGBBBRRBRRRGGGBGBGBGGGGGGGBBBBGBRGBRBBBRBGGBGGGGG",
                repeats = 100000,
            )
        }"
    )
}

fun p2c(input: String, repeats: Int): Int {
    val ringStart =
        ArrayDeque(input.repeat(repeats / 2).toList())
    val ringEnd = ArrayDeque(input.repeat(repeats / 2).toList())
    var boltCount = 0
    val shots = "RGB"
    var shotPtr = 0
    while (ringStart.isNotEmpty()) {
        val shot = shots[shotPtr]
        if (shot == ringStart.first() && (ringStart.size + ringEnd.size) % 2 == 0) {
            ringEnd.removeFirst()
        }
        ringStart.removeFirst()
        boltCount++
        shotPtr = (shotPtr + 1) % shots.length
        if (ringStart.size < ringEnd.size) {
            ringStart.addLast(ringEnd.removeFirst())
        }
    }
    return boltCount
}

fun p2b(input: String, repeats: Int): Int {
    val ring =
        input.repeat(repeats).toMutableList()
    var boltCount = 0
    val shots = "RGB"
    var shotPtr = 0
    while (ring.isNotEmpty()) {
        val shot = shots[shotPtr]
        if (shot == ring.first() && ring.size % 2 == 0) {
            ring.removeAt(ring.size / 2)
        }
        ring.removeAt(0)
        boltCount++
        shotPtr = (shotPtr + 1) % shots.length
    }
    return boltCount
}

fun p2a(): Int {
    val input2a =
        "RRBBRBBGGGRRGGBBRGGRRRRRRGGRRGGRRGRGBGBBBGGBGGRGGGRGRRRBBBGGGGRRBRRGRRBGGGBBRGBBBRRRGBGBGGRRRRGGBBBBBBGRBGGRRRRRRRRRRRRRGGGGGGGGGGBBBBBBBBBBGBRGBRGBRGBRGBRGBRGBRGBRGBRGBRGBRGBRGBRGBRGBRGBRGBRGBRGBRGBR"
    var boltCount = 0
    var balloonPtr = 0
    val shots = "RGB"
    var shotPtr = 0
    while (balloonPtr < input2a.length) {
        boltCount++
        val shot = shots[shotPtr]
        shotPtr = (shotPtr + 1) % shots.length
        while (input2a.getOrNull(balloonPtr) == shot) {
            balloonPtr++
        }
        balloonPtr++
    }
    return boltCount
}