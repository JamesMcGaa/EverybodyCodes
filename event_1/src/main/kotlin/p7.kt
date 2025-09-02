import Utils.Coord
import com.google.common.collect.Collections2
import java.io.File

fun main() {
    println("Part A: ${run7ab(null, "inputs/input7a.txt", MODE_A)}")
    println("Part B: ${run7ab("inputs/input7bracetrack.txt", "inputs/input7b.txt", MODE_B)}")
    println("Part C: ${run7c()}")
}

fun run7c(): Int {
    val trackC = Utils.readAsGrid("inputs/input7cracetrack.txt", null) { it }
    val seen = mutableSetOf(Coord(0, 0), Coord(0, 1))
    var current = Coord(0, 1)
    var flatTrackC = mutableListOf(trackC[current]!!)
    while (((current.fullNeighbors.filter { it in trackC && trackC[it] != ' ' }) - seen).isNotEmpty()) {
        val next = ((current.fullNeighbors.filter { it in trackC && trackC[it] != ' ' }) - seen).first()
        seen.add(next)
        flatTrackC.add(trackC[next]!!)
        current = next
    }
    flatTrackC.add('S')

    val rivalPlan = File("inputs/input7c.txt").readLines().first().split(":").last().split(",")
    val rivalPower = totalPower(flatTrackC, rivalPlan, 2024)
    return Collections2.permutations(rivalPlan).toSet().count { totalPower(flatTrackC, it, 2024) > rivalPower }
}

fun run7ab(trackFilename: String?, planFilename: String?, mode: Int): String {
    val plans = mutableMapOf<String, List<String>>()
    File(planFilename!!).forEachLine {
        plans[it.split(":").first()] = it.split(":").last().split(",")
    }

    val flatTrack = when (mode) {
        MODE_A -> listOf('=')

        MODE_B -> {
            val track = Utils.readAsGrid(trackFilename!!, null) { it }
            val maxX = track.keys.maxOf { it.x }
            val maxY = track.keys.maxOf { it.y }
            var flattenedTrack = ((0..maxY).map { track[Coord(0, it)]!! }
                    + (1..maxX).map { track[Coord(it, maxY)]!! }
                    + (maxY - 1 downTo 0).map { track[Coord(maxX, it)]!! }
                    + (maxX - 1 downTo 1).map { track[Coord(it, 0)]!! }
                    )
            flattenedTrack.drop(1) + flattenedTrack.first()
        }

        MODE_C -> {
            val trackC = Utils.readAsGrid(trackFilename!!, null) { it }
            val seen = mutableSetOf(Coord(0, 0), Coord(0, 1))
            var current = Coord(0, 1)
            var flatTrackC = mutableListOf(trackC[current]!!)
            while (((current.fullNeighbors.filter { it in trackC && trackC[it] != ' ' }) - seen).isNotEmpty()) {
                val next = ((current.fullNeighbors.filter { it in trackC && trackC[it] != ' ' }) - seen).first()
                seen.add(next)
                flatTrackC.add(trackC[next]!!)
                current = next
            }
            flatTrackC.add('S')
            flatTrackC
        }

        else -> throw Exception()
    }

    return when (mode) {
        MODE_A -> plans.keys.sortedBy { totalPower(flatTrack, plans[it]!!, 10) }.reversed().joinToString("")
        MODE_B -> plans.keys.sortedBy { totalPower(flatTrack, plans[it]!!, 10) }.reversed().joinToString("")
        else -> throw Exception()
    }
}

fun totalPower(track: List<Char>, plan: List<String>, loops: Int): Long {
    var curr = 10
    var counter = 0L
    var trackPtr = 0
    var planPtr = 0
    repeat(track.size * loops) {
        when (track[trackPtr]) {
            '+' -> curr += 1
            '-' -> curr -= 1
            '=', 'S' ->
                when (plan[planPtr]) {
                    "+" -> curr += 1
                    "-" -> curr -= 1
                    "=" -> Unit
                    else -> throw Exception()
                }

            else -> throw Exception()
        }
        counter += curr
        trackPtr = (trackPtr + 1) % track.size
        planPtr = (planPtr + 1) % plan.size
    }
    return counter
}
