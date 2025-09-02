import Utils.Coord
import kotlin.math.min

fun main() {
    println("Part A: ${p17("inputs/input17a.txt")}")
    println("Part B: ${p17("inputs/input17b.txt")}")
    println("Part C: ${p17("inputs/input17c.txt", true)}")
}

fun p17(filename: String, maxEdge: Boolean = false): Long {
    val grid = Utils.readAsGrid(filename, null) { it }
    val nodes = grid.filterValues { it == '*' }.keys
    val notTree = nodes.toMutableSet()
    val dists = mutableListOf<Int>()

    while (notTree.isNotEmpty()) {
        val arbitraryFirstNode = notTree.first()
        notTree.remove(arbitraryFirstNode)
        val tree = mutableSetOf(arbitraryFirstNode)

        var dist = 0
        inner@ while (notTree.isNotEmpty()) {
            val allPairs = mutableListOf<Pair<Coord, Coord>>()
            for (a in tree) {
                for (b in notTree) {
                    allPairs.add(Pair(a, b))
                }
            }
            val lowest = allPairs.filter {
                val dist = (it.first - it.second).manhattanDist
                !maxEdge || dist < 6
            }.minByOrNull {
                (it.first - it.second).manhattanDist
            }
            lowest ?: break@inner
            tree.add(lowest.second)
            notTree.remove(lowest.second)
            dist += (lowest.first - lowest.second).manhattanDist
        }
        dists.add(dist + tree.size)
    }

    return productOfList(dists.map { it.toLong() }.sortedDescending().subList(0, min(3, dists.size)))
}

fun productOfList(list: List<Long>): Long {
    return list.fold(1L) { acc, num -> acc * num }
}

