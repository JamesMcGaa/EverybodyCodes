import java.io.File

fun main() {
    println("Part A: ${p1a("inputs/input1a.txt")}")
    println("Part B: ${p1b("inputs/input1b.txt")}")
    println("Part C: ${p1c("inputs/input1c.txt")}")
}

fun p1c(filename: String): String {
    val (names, instructions) = getInput(filename)
    for (instruction in instructions) {
        val amount = instruction.substring(1).toInt()
        val sign = if (instruction.first() == 'L') {
            -1
        } else {
            1
        }
        val namesPtr = Math.floorMod(sign * amount, names.size)

        val temp = names[0]
        names[0] = names[namesPtr]
        names[namesPtr] = temp
    }

    return names[0]
}

fun p1b(filename: String): String {
    val (names, instructions) = getInput(filename)
    var namesPtr = 0
    for (instruction in instructions) {
        val amount = instruction.substring(1).toInt()
        val sign = if (instruction.first() == 'L') {
            -1
        } else {
            1
        }
        namesPtr = Math.floorMod(namesPtr + sign * amount, names.size)
    }

    return names[namesPtr]
}

fun p1a(filename: String): String {
    val (names, instructions) = getInput(filename)

    var namesPtr = 0
    for (instruction in instructions) {
        val amount = instruction.substring(1).toInt()
        val sign = if (instruction.first() == 'L') {
            -1
        } else {
            1
        }
        namesPtr = (namesPtr + sign * amount).coerceIn(names.indices)
    }

    return names[namesPtr]
}

fun getInput(filename: String): Pair<MutableList<String>, List<String>> {
    val input = File(filename).readLines()
    val names = input.first().split(",").toMutableList()
    val instructions = input.last().split(",")
    return Pair(names, instructions)
}