import java.io.File
import kotlin.properties.Delegates

fun main() {
    println("Part A: ${p2("inputs/input2a.txt")}")
    TreeNode.resetRoot()
    println("Part B: ${p2("inputs/input2b.txt")}")
    TreeNode.resetRoot()
    println("Part C: ${p2("inputs/input2c.txt", shouldSwapFullSubtree = true)}")
}

private class TreeNode(
    val id: Int,
    var symbol: String,
    var rank: Int,
    var shadowSymbol: String,
    var shadowRank: Int,
) {

    lateinit var path: String
    lateinit var ancestor: TreeNode
    var isLeftOfAncestor by Delegates.notNull<Boolean>()

    companion object {
        // Give them a root to allow the top nodes to swap
        val ROOT = TreeNode(
            id = Int.MAX_VALUE,
            symbol = "ROOT",
            rank = Int.MAX_VALUE,
            shadowSymbol = "ROOT",
            shadowRank = Int.MAX_VALUE,
        )

        fun resetRoot() {
            ROOT.left = null
            ROOT.right = null
        }
    }

    var left: TreeNode? = null
    var right: TreeNode? = null

    fun getPathToNodeMapping(
        dict: MutableMap<String, TreeNode> = mutableMapOf(),
        pathToThisNode: String = "",
    ): MutableMap<String, TreeNode> {
        this.path = pathToThisNode
        dict[pathToThisNode] = this
        this.left?.getPathToNodeMapping(dict, pathToThisNode + "L")
        this.right?.getPathToNodeMapping(dict, pathToThisNode + "R")
        return dict
    }

    fun swap() {
        val tempSymbol = symbol
        val tempRank = rank
        symbol = shadowSymbol
        rank = shadowRank
        shadowSymbol = tempSymbol
        shadowRank = tempRank
    }

    fun addNode(node: TreeNode) {
        if (node.rank < this.rank) { // move left is smaller
            if (left == null) {
                this.left = node
                node.isLeftOfAncestor = true
                node.ancestor = this
            } else {
                this.left!!.addNode(node)
            }
        } else {
            if (right == null) {
                this.right = node
                node.isLeftOfAncestor = false
                node.ancestor = this
            } else {
                this.right!!.addNode(node)
            }
        }
    }

    fun maximalLevelString(): String {
        val customComparator: Comparator<Map.Entry<Int, List<MutableMap.MutableEntry<String, TreeNode>>>> =
            compareBy<Map.Entry<Int, List<MutableMap.MutableEntry<String, TreeNode>>>> { it.value.size }.thenByDescending { it.key }
        return this.getPathToNodeMapping().entries.groupBy { it.key.length }
            .maxWith(customComparator)
            .value.map { it.value }
            .sortedBy { it.path }.joinToString(separator = "") { it.symbol }
    }
}

fun p2(
    filename: String,
    shouldSwapFullSubtree: Boolean = false,
): String {
    var isInitialized = false

    File(filename).readLines().forEach { line ->
        if (line.split(" ").first() == "ADD") {
            val values = handleAdd(line)
            val id = values[0].toInt()
            val leftRank = values[1].toInt()
            val leftSymbol = values[2]
            val rightRank = values[3].toInt()
            val rightSymbol = values[4]

            val leftNode = TreeNode(
                id = id,
                symbol = leftSymbol,
                rank = leftRank,
                shadowSymbol = rightSymbol,
                shadowRank = rightRank,
            )
            val rightNode = TreeNode(
                id = id,
                symbol = rightSymbol,
                rank = rightRank,
                shadowSymbol = leftSymbol,
                shadowRank = leftRank,
            )

            if (!isInitialized) {
                leftNode.isLeftOfAncestor = true
                leftNode.ancestor = TreeNode.ROOT
                TreeNode.ROOT.left = leftNode

                rightNode.isLeftOfAncestor = false
                rightNode.ancestor = TreeNode.ROOT
                TreeNode.ROOT.right = rightNode

                isInitialized = true
            } else {
                TreeNode.ROOT.left!!.addNode(leftNode)
                TreeNode.ROOT.right!!.addNode(rightNode)
            }
        } else {
            val swapId = line.split(" ")[1].toInt()

            if (shouldSwapFullSubtree) {
                val nodesToSwap = TreeNode.ROOT.getPathToNodeMapping().values.filter { it.id == swapId }
                assert(nodesToSwap.size == 2)
                val a = nodesToSwap.first()
                val b = nodesToSwap.last()

                if (a.isLeftOfAncestor) {
                    a.ancestor.left = b
                } else {
                    a.ancestor.right = b
                }

                if (b.isLeftOfAncestor) {
                    b.ancestor.left = a
                } else {
                    b.ancestor.right = a
                }
                val bAncestorTemp = b.ancestor
                b.ancestor = a.ancestor
                a.ancestor = bAncestorTemp

                val aIsLeftOfAncestorTemp = a.isLeftOfAncestor
                a.isLeftOfAncestor = b.isLeftOfAncestor
                b.isLeftOfAncestor = aIsLeftOfAncestorTemp
            } else {
                TreeNode.ROOT.getPathToNodeMapping().values.filter { it.id == swapId }.forEach { it.swap() }
            }
        }
    }

    return TreeNode.ROOT.left!!.maximalLevelString() + TreeNode.ROOT.right!!.maximalLevelString()
}

fun handleAdd(line: String): List<String> {
    // Regex credit to ChatGPT
    val regex = Regex("""id=(\d+)\s+left=\[(\d+),([^,\]]+)]\s+right=\[(\d+),([^,\]]+)]""")
    val match = regex.find(line)

    if (match != null) {
        val (id, leftNum, leftChar, rightNum, rightChar) = match.destructured
        return listOf(id, leftNum, leftChar, rightNum, rightChar)
    }
    throw Exception("Input parse error")
}