import Node.Companion.ALL_NODES
import java.io.File
import kotlin.properties.Delegates

data class Node(
    val identifier: String,
    val adj: List<String>,
) {
    var isFruit by Delegates.notNull<Boolean>()

    init {
        isFruit = identifier == "@"
        if (!isFruit) {
            ALL_NODES[identifier] = this
        }
    }

    companion object {
        val ALL_NODES = mutableMapOf<String, Node>()
    }
}

fun main() {
    println("Part A: ${p6("inputs/input6a.txt")}")
    println("Part B: ${p6("inputs/input6b.txt", true)}")
    println("Part C: ${p6("inputs/input6c.txt", true)}")
}

const val BUG = "BUG"
const val ANT = "ANT"
val BAD_BRANCHES = setOf(BUG, ANT)

fun p6(inputFilename: String, isPartB: Boolean = false): String {
    File(inputFilename).forEachLine {
        val identifier = it.split(":").first()
        val adj = it.split(":").last().split(",")
        if (identifier !in BAD_BRANCHES) {
            Node(identifier, adj.filter { it !in BAD_BRANCHES })
        }
    }

    val depthToPath = mutableMapOf<Int, MutableList<String>>()

    fun visit(node: Node, pathSoFar: String, depth: Int) {
        if (node.isFruit) {
            if (depth in depthToPath) {
                depthToPath[depth]!!.add(pathSoFar)
            } else {
                depthToPath[depth] = mutableListOf(pathSoFar)
            }
            return
        }

        for (adj in node.adj) {
            val adjNode = if (adj == "@") {
                Node("@", listOf())
            } else {
                // Not all branches have adj in these graphs
                ALL_NODES[adj] ?: continue
            }
            val additional = if (isPartB) adjNode.identifier.first() else adjNode.identifier
            visit(adjNode, pathSoFar + additional, depth + 1)
        }
    }
    visit(ALL_NODES["RR"]!!, if (isPartB) "R" else "RR", 0)
    ALL_NODES.clear() // Important
    return depthToPath.filterValues { it.size == 1 }.values.first().first()
}
