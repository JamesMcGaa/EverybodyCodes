import java.io.File

fun main() {
    println("Part A: ${p6ab("inputs/input6a.txt")}")
    println("Part B: ${p6ab("inputs/input6b.txt", isPartB = true)}")
    println("Part C: ${p6c("inputs/input6c.txt", repeats = 1000, distanceLimit = 1000)}")
}

fun p6ab(filename: String, isPartB: Boolean = false): Long {
    val chars = File(filename).readLines().first()
    val prevCounts = mutableMapOf<Char, Int>()
    var counter = 0L
    for (char in chars) {
        if (char.isUpperCase()) {
            prevCounts[char] = prevCounts.getOrDefault(char, 0) + 1
        } else {
            if (char == 'a' || isPartB) {
                counter += prevCounts.getOrDefault(char.uppercaseChar(), 0)
            }
        }
    }
    return counter
}


fun p6c(filename: String, repeats: Int, distanceLimit: Int): Long {
    val chars = File(filename).readLines().first()
    val prevCounts = mutableMapOf<Char, Int>()
    var counter = 0L
    repeat(repeats) { outer ->
        chars.forEachIndexed { idx, char ->
            if (outer * chars.length + idx > distanceLimit) {
                val prevChar = chars[Math.floorMod(idx - distanceLimit - 1, chars.length)]
                prevCounts[prevChar] = prevCounts.getOrDefault(prevChar, 0) - 1
            }

            counter += if (char.isUpperCase()) {
                prevCounts.getOrDefault(char.lowercaseChar(), 0)
            } else {
                prevCounts.getOrDefault(char.uppercaseChar(), 0)
            }
            prevCounts[char] = prevCounts.getOrDefault(char, 0) + 1
        }
    }

    return counter
}