import java.io.File

fun main() {
    println("Part A: ${p7ab("inputs/input7a.txt")}")
    println("Part B: ${p7ab("inputs/input7b.txt", isPartB = true)}")
    println("Part C: ${p7c("inputs/input7c.txt")}")
}

fun p7c(filename: String): Long {
    val (names, adj) = parseInput(filename)
    var counter = 0L

    fun visit(nameSoFar: String) {
        if (nameSoFar.length > 11) {
            return
        } else if (nameSoFar.length >= 7) {
            counter += 1
        }
        adj.getOrDefault(nameSoFar.last().toString(), emptySet())
            .forEach { newEndChar ->
                visit(nameSoFar + newEndChar)
            }
    }

    names.filter { name -> isLegalName(name, adj) }
        .filter { legalName ->
            // Avoid double counting any of the form NAME + Extras
            !names.any {
                it != legalName && legalName.startsWith(it)
            }
        }.forEach { legalName ->
            visit(legalName)
        }

    return counter
}

fun p7ab(filename: String, isPartB: Boolean = false): String {
    val (names, adj) = parseInput(filename)

    var counter = 0L
    for (name in names) {
        if (isLegalName(name, adj))
            if (isPartB) {
                counter += names.indexOf(name) + 1
            } else {
                return name
            }
    }

    if (isPartB) {
        return counter.toString()
    } else {
        throw Exception("No legal name found")
    }
}

fun isLegalName(name: String, adj: MutableMap<String, MutableSet<String>>): Boolean {
    for (i in 0 until name.length - 1) {
        if (!adj.getOrDefault(name[i].toString(), emptySet()).contains(name[i + 1].toString())) {
            return false
        }
    }
    return true
}

private fun parseInput(filename: String): Pair<List<String>, MutableMap<String, MutableSet<String>>> {
    val input = File(filename).readLines()
    val names = input.first().split(",")
    val adj = mutableMapOf<String, MutableSet<String>>()
    input.subList(2, input.size).forEach { line ->
        val split = line.split(" > ")
        val start = split.first()
        val dests = split.last().split(",")
        adj[start] = dests.toMutableSet()
    }
    return Pair(names, adj)
}