import Utils.ZCoord
import java.io.File

fun main() {
    println("Part A: ${run14("inputs/input14a.txt", Part.A)}")
    println("Part B: ${run14("inputs/input14b.txt", Part.B)}")
    println("Part C: ${run14("inputs/input14c.txt", Part.C)}")
}

fun run14(filename: String, part: Part): Int {
    val commands = File(filename).readLines().map { it.split(",") }
    val tree = mutableSetOf<ZCoord>()
    val leaves = mutableSetOf<ZCoord>()
    var tail = ZCoord(0, 0, 0)

    commands.forEach { command ->
        tail = ZCoord(0, 0, 0)
        command.forEach {
            val dir = it.first()
            val amount = it.removePrefix(dir.toString()).toInt()
            val dirCoord = when (dir) {
                'U' -> ZCoord.UP_COORD
                'D' -> ZCoord.DOWN_COORD
                'L' -> ZCoord.LEFT_COORD
                'R' -> ZCoord.RIGHT_COORD
                'F' -> ZCoord.FORWARD_COORD
                'B' -> ZCoord.BACKWARD_COORD
                else -> throw Exception()
            }
            repeat(amount) {
                tail += dirCoord
                tree.add(tail)
            }
        }
        leaves.add(tail)
    }

    val mainTrunk = tree.filter { it.y == 0 && it.z == 0 }

    val partC = mainTrunk.minOf { mainTrunkSegment ->
        val dists = Utils.generalizedBFS<ZCoord, Int>(
            tree,
            mainTrunkSegment,
            isLegal = { coord: ZCoord, grid: Set<ZCoord> -> coord in grid },
            neighbors = { coord: ZCoord, grid: Set<ZCoord> -> coord.manhattanNeighbors },
        )
        leaves.sumOf { dists[it]!! }
    }


    return when (part) {
        Part.A -> -1 * tree.minOf { it.x }
        Part.B -> tree.size
        Part.C -> partC
    }
}