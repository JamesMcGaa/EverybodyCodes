fun main() {
    println("Part A: ${p2a(166,52)}")
    println("Part B: ${p2bc(-79785,15616, 10L)}")
    println("Part C: ${p2bc(-79785,15616, 1L)}")
}

fun p2bc(startX: Long, startY: Long, spacingFactor: Long): Long {
    var counter = 0L

    for (x in startX..startX + 1000 step spacingFactor) {
        inner@ for (y in startY..startY + 1000 step spacingFactor) {
            var result = ComplexNumber(0, 0)
            repeat(100) {
                result *= result
                result /= ComplexNumber(100000, 100000)
                result += ComplexNumber(x, y)
                if (!(result.x in -1000000..1000000
                   && result.y in -1000000..1000000)
                ) {
                    continue@inner
                }
            }
            counter ++
        }
    }

    return counter
}

fun p2a(startX: Long, startY: Long): ComplexNumber {
    val a = ComplexNumber(startX, startY)
    var result = ComplexNumber(0, 0)
    repeat(3) {
        result *= result
        result /= ComplexNumber(10, 10)
        result += a
    }
    return result
}

data class ComplexNumber(var x: Long, var y: Long) {
    operator fun plus(other: ComplexNumber): ComplexNumber {
        return ComplexNumber(x + other.x, y + other.y)
    }

    operator fun times(other: ComplexNumber): ComplexNumber {
        return ComplexNumber(x * other.x - y * other.y, x * other.y + y * other.x)
    }

    operator fun div(other: ComplexNumber): ComplexNumber {
        return ComplexNumber(x / other.x, y / other.y)
    }

    override fun toString(): String {
        return "[$x,$y]"
    }
}