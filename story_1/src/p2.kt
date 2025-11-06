import java.io.File

fun main() {
    println("Part A: ${p2("inputs/input2a.txt")}")
    TreeNode.allNodes.clear()
    println("Part B: ${p2("inputs/input2b.txt")}")
    TreeNode.allNodes.clear()
    println("Part C: ${p2("inputs/input2c.txt", shouldSwapFullSubtree = true)}")
}

private class TreeNode(
    val id: Int,
    var symbol: String,
    var rank: Int,
    var shadowSymbol: String,
    var shadowRank: Int,
    var path: String,
    var depth: Int = 1,
) {

    companion object {
        val allNodes = mutableMapOf<Int, MutableSet<TreeNode>>()

        fun registerNode(node: TreeNode) {
            if (allNodes.containsKey(node.depth)) {
                allNodes[node.depth]!!.add(node)
            } else {
                allNodes[node.depth] = mutableSetOf(node)
            }
        }
    }

    var left: TreeNode? = null
    var right: TreeNode? = null

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
            node.path += "L"
            node.depth = this.depth + 1
            if (left == null) {
                this.left = node
                registerNode(node)
            } else {
                this.left!!.addNode(node)
            }
        } else {
            node.path += "R"
            node.depth = this.depth + 1
            if (right == null) {
                this.right = node
                registerNode(node)
            } else {
                this.right!!.addNode(node)
            }
        }
    }
}

fun p2(filename: String, shouldSwapFullSubtree: Boolean = false): String {
    var leftTree: TreeNode? = null
    var rightTree: TreeNode? = null
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
                path = "L",
            )
            if (leftTree == null) {
                leftTree = leftNode
                TreeNode.registerNode(leftNode)
            } else {
                leftTree.addNode(leftNode)
            }

            val rightNode = TreeNode(
                id = id,
                symbol = rightSymbol,
                rank = rightRank,
                shadowSymbol = leftSymbol,
                shadowRank = leftRank,
                path = "R",
            )
            if (rightTree == null) {
                rightTree = rightNode
                TreeNode.registerNode(rightNode)
            } else {
                rightTree.addNode(rightNode)
            }
        } else {
            val swapId = line.split(" ")[1].toInt()

            if (shouldSwapFullSubtree) {

            }
            TreeNode.allNodes.values.flatten().filter { it.id == swapId }.forEach { it.swap() }
        }
    }

    return TreeNode.allNodes.values.maxBy { it.size }.sortedBy { it.path }.joinToString(separator = "") { it.symbol }
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