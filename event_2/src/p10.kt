import Utils.Coord

fun main() {
    println("Part A: ${p10a("inputs/input10a.txt")}")
    println("Part B: ${p10b("inputs/input10b.txt")}")
    println("Part C: ${p10c("inputs/input10c.txt")}")
}

fun p10a(filename: String): Int {
    val grid = Utils.readAsGrid<Char>(filename)
    val dragon = Utils.findCoord('D', grid)
    var possible = mutableSetOf(dragon)
    val cumulative = mutableSetOf(dragon)
    repeat(4) {
        val newPossible = mutableSetOf<Coord>()
        for (pos in possible) {
            val adj = pos.knightMoves()
            newPossible.addAll(adj)
            cumulative.addAll(adj)
        }
        possible = newPossible
    }
    return grid.count { it.key in cumulative && it.value == 'S' }
}

fun p10b(filename: String): Int {
    val grid = Utils.readAsGrid<Char>(filename)
    var dragons = mutableSetOf(Utils.findCoord('D', grid))
    var sheep = Utils.findAllCoords('S', grid)
    val shelters = Utils.findAllCoords('#', grid)
    val bounds = Utils.getGridRange(grid)
    var eatenSheep = 0

    repeat(20) {
        val newDragons = mutableSetOf<Coord>()
        for (dragon in dragons) {
            val adj = dragon.knightMoves()
            newDragons.addAll(adj)
        }
        dragons = newDragons.filter { Utils.inbounds(it, grid, bounds) }.toMutableSet()

        // Dragons move to sheep
        var deadSheep = sheep.filter { it in dragons && it !in shelters }
        eatenSheep += deadSheep.size
        sheep = sheep.filter { it !in deadSheep }.toSet()

        // Sheep move to dragons, or out of bounds
        sheep = sheep.map { it.copy(x = it.x + 1) }.toSet()
        deadSheep = sheep.filter { it in dragons && it !in shelters }
        eatenSheep += deadSheep.size
        sheep = sheep.filter { it !in deadSheep && Utils.inbounds(it, grid, bounds) }.toMutableSet()

    }
    return eatenSheep
}

fun p10c(filename: String): Long {
    val grid = Utils.readAsGrid<Char>(filename)
    val initialDragon = Utils.findCoord('D', grid)
    val initialSheepCoords = Utils.findAllCoords('S', grid)
    val shelters = Utils.findAllCoords('#', grid)
    val bounds = Utils.getGridRange(grid)
    val (_, maxX, minY, maxY) = bounds
    val initialSheeps = mutableListOf<Int?>()
    for (y in minY..maxY) {
        if (Coord(0, y) in initialSheepCoords) {
            initialSheeps.add(0)
        } else {
            initialSheeps.add(null)
        }
    }
    val initialBoardState = BoardState(
        sheeps = initialSheeps,
        dragon = initialDragon,
    )

    val memo = mutableMapOf<BoardState, Long>()
    fun possible(boardState: BoardState): Long {
        if (boardState in memo) {
            return memo[boardState]!!
        }

        if (boardState.sheeps.any { it != null && it > maxX }) {
            return 0L
        }

        if (boardState.sheeps.all { it == null }) {
            return 1L
        }

        if (!Utils.inbounds(boardState.dragon, grid, bounds)) {
            return 0L
        }

        var ret = 0L
        val legalSheepIndices = boardState.sheeps.mapIndexed { y, x ->
            if (x == null) {
                null
            } else {
                val possibleMove = Coord(x + 1, y)
                if (possibleMove != boardState.dragon || possibleMove in shelters) {
                    y
                } else {
                    null
                }
            }
        }.filterNotNull()


        if (legalSheepIndices.isEmpty()) {
            for (move in boardState.dragon.knightMoves()) {
                val movedSheeps = boardState.sheeps.toMutableList()
                if (Utils.inbounds(move, grid, bounds) &&
                    move !in shelters &&
                    movedSheeps[move.y] != null &&
                    move.x == movedSheeps[move.y]
                ) {
                    movedSheeps[move.y] = null
                }
                ret += possible(
                    BoardState(
                        sheeps = movedSheeps,
                        dragon = move,
                    )
                )
            }
        } else {
            for (legalSheepIndex in legalSheepIndices) {
                for (move in boardState.dragon.knightMoves()) {
                    val movedSheeps = boardState.sheeps.toMutableList().apply {
                        this[legalSheepIndex] = this[legalSheepIndex]!! + 1
                    }
                    if (Utils.inbounds(move, grid, bounds) &&
                        move !in shelters &&
                        movedSheeps[move.y] != null &&
                        move.x == movedSheeps[move.y]
                    ) {
                        movedSheeps[move.y] = null
                    }
                    ret += possible(
                        BoardState(
                            sheeps = movedSheeps,
                            dragon = move,
                        )
                    )
                }
            }
        }

        memo[boardState] = ret
        return ret
    }

    val ret = possible(initialBoardState)
    return ret
}

data class BoardState(
    val sheeps: MutableList<Int?>,
    val dragon: Coord,
)

fun Coord.knightMoves(): Set<Coord> = setOf(
    Coord(this.x + 2, this.y + 1),
    Coord(this.x + 2, this.y - 1),
    Coord(this.x - 2, this.y + 1),
    Coord(this.x - 2, this.y - 1),
    Coord(this.x + 1, this.y + 2),
    Coord(this.x + 1, this.y - 2),
    Coord(this.x - 1, this.y + 2),
    Coord(this.x - 1, this.y - 2),
)