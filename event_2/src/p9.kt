import Utils.freqCount
import java.io.File
import kotlin.math.max


fun main() {
    println("Part A: ${p9a("inputs/input9a.txt")}")
    println("Part B: ${p9b("inputs/input9b.txt")}")
    println("Part C: ${p9c("inputs/input9c.txt")}")
}

fun p9a(filename: String): Long {
    val input = File(filename).readLines().map { it.split(":").last() }
    val child = input[findChildIdx(input)!!]
    val parents = input.filter { it != child }
    return similarity(child, parents.first()) * similarity(child, parents.last())
}

fun p9b(filename: String): Long {
    val input = File(filename).readLines().map { it.split(":").last() }
    val children = mutableMapOf<String, Pair<String, String>>()

    for (a in input) {
        for (b in input) {
            for (c in input) {
                if (setOf(a, b, c).size == 3) {
                    if (isChild(listOf(a, b, c))) {
                        children[a] = Pair(b, c)
                    }
                }
            }
        }
    }
    return children.entries.sumOf {
        similarity(it.key, it.value.first) * similarity(it.key, it.value.second)
    }
}

fun p9c(filename: String): Long {
    val seqToId = mutableMapOf<String, Long>()
    val input = File(filename).readLines().map {
        val id = it.split(":").first().toLong()
        val seq = it.split(":").last()
        seqToId[seq] = id
        seq
    }

    val adj = mutableMapOf<String, MutableSet<String>>()
    for (a in input) {
        for (b in input) {
            for (c in input) {
                if (setOf(a, b, c).size == 3) {
                    if (isChild(listOf(a, b, c))) {
                        adj[a] = adj.getOrDefault(a, mutableSetOf()).apply {
                            add(b)
                            add(c)
                        }
                        adj[b] = adj.getOrDefault(b, mutableSetOf()).apply {
                            add(a)
                        }
                        adj[c] = adj.getOrDefault(c, mutableSetOf()).apply {
                            add(a)
                        }
                    }
                }
            }
        }
    }

    var maxSoFar = -1
    var largest = mutableSetOf<String>()
    val unvisited = input.toMutableSet()
    while (unvisited.isNotEmpty()) {
        val random = unvisited.random()
        val seen = mutableSetOf<String>()
        val stack = mutableListOf(random)
        while (stack.isNotEmpty()) {
            val current = stack.removeLast()
            seen.add(current)
            adj.getOrDefault(current, emptySet()).filter { !seen.contains(it)}.forEach{
                stack.add(it)
            }
        }
        unvisited.removeAll(seen)
        if (seen.size > maxSoFar) {
            largest = seen
            maxSoFar = seen.size
        }

    }


    return largest.sumOf { seqToId[it]!! }
}

fun isChild(seqs: List<String>): Boolean {
    assert(seqs.size == 3)
    assert(seqs.toSet().size == 3)

    for (i in seqs[0].indices) {
        if (seqs[0][i] !in setOf(seqs[1][i], seqs[2][i])) {
            return false
        }
    }
    return true
}


// Relies on the fact that 1 is the child, does not work past Part A
fun findChildIdx(seqs: List<String>): Int? {
    assert(seqs.size == 3)
    if (seqs.toSet().size != 3) {
        return null
    }

    val candidates = mutableSetOf(0, 1, 2)
    for (i in seqs.first().indices) {
        val col = seqs.map { it[i] }.freqCount()
        if (col.size == 2) {
            val cantBeChild = seqs.indexOfFirst { seq ->
                seq[i] == col.minBy { it.value }.key
            }
            candidates.remove(cantBeChild)
        }
    }
    if (candidates.size == 1) {
        return candidates.random()
    }

    return null
}

fun similarity(a: String, b: String): Long {
    return a.mapIndexed { idx, char -> char == b[idx] }.count { it }.toLong()
}