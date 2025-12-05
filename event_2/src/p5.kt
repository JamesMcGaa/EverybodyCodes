import java.io.File
import kotlin.collections.first

fun main() {
    println("Part A: ${p5a("inputs/input5a.txt")}")
    println("Part B: ${p5b("inputs/input5b.txt")}")
    println("Part C: ${p5c("inputs/input5c.txt")}")
}

fun p5a(filename: String): String {
    val inputLine = File(filename).readLines().first()
    val base = lineToWishbone(inputLine)
    return base.quality()
}

fun p5b(filename: String): Long {
    val qualities = File(filename).readLines().map { lineToWishbone(it).quality().toLong() }
    return qualities.max() - qualities.min()
}

fun p5c(filename: String): Long {
    val wishbones = File(filename).readLines().map { lineToWishbone(it) }
    var counter = 0L
    wishbones.sortedWith(Wishbone.comparator).forEachIndexed { idx, wishbone ->
        counter += (idx + 1) * wishbone.identifier!!
    }
    return counter
}

fun lineToWishbone(input: String): Wishbone {
    val identifier = input.split(":").first().toLong()
    val amounts = input.split(":").last().split(",").map { it.toLong() }
    val base = Wishbone(amount = amounts.first(), identifier = identifier)
    amounts.subList(1, amounts.size).forEach {
        base.add(it)
    }
    return base
}


data class Wishbone(
    val amount: Long,
    var left: Wishbone? = null,
    var right: Wishbone? = null,
    var descendant: Wishbone? = null,
    val identifier: Long? = null,
) {

    companion object {
        val comparator: Comparator<Wishbone>
            get() {
                val level = Comparator<Wishbone> { o1, o2 ->
                    var a = o1
                    var b = o2
                    while (a != null && b != null) {
                        if (a.levelRead() > b.levelRead()) {
                            return@Comparator -1
                        } else if (a.levelRead() < b.levelRead()) {
                            return@Comparator 1
                        } else {
                            a = a.descendant
                            b = b.descendant
                        }
                    }
                    return@Comparator 0
                }
                return compareBy<Wishbone> { -1 * it.quality().toLong() }.then(level).thenBy { -it.identifier!! }
            }
    }


    fun add(newAmount: Long) {
        if (newAmount < amount && left == null) {
            left = Wishbone(newAmount)
        } else if (newAmount > amount && right == null) {
            right = Wishbone(newAmount)
        } else if (descendant == null) {
            descendant = Wishbone(newAmount)
        } else {
            descendant!!.add(newAmount)
        }
    }

    fun levelRead(): Long {
        var level = ""
        left?.let { level += it.amount }
        level += amount
        right?.let { level += it.amount }
        return level.toLong()
    }

    fun quality(): String {
        return if (descendant == null) {
            this.amount.toString()
        } else {
            this.amount.toString() + descendant!!.quality()
        }
    }
}