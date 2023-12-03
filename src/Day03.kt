import kotlin.math.min

fun main() {

    fun parseGrid(input: List<String>): Day3.Grid {
        return Day3.Grid(Array(input.size) { input[it].toCharArray() })
    }

    fun part1(input: List<String>): Int {
        val grid = parseGrid(input)
        val gridWithNumbersRemoved = grid.removeNumbersNextToSymbols()
        val diff = grid.getDiff(gridWithNumbersRemoved)
        return diff.getNumbers().sum()
    }

    fun part2(input: List<String>): Int {
        val grid = parseGrid(input)
        return grid.getGearRatios().sum()
    }

    val testInput = readInput("Day03.test")

    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

class Day3 {
    data class GridEntry(val x: Int, val y: Int, val value: Char)

    class Grid(val values: Array<CharArray>) {

        fun get(x: Int, y: Int): Char? {
            return values.getOrNull(y)?.getOrNull(x)
        }

        fun set(x: Int, y: Int, value: Char) {
            values[y][x] = value
        }

        fun removeNumber(x: Int, y: Int): String? {
            if (get(x, y)?.isDigit() != true) {
                return null
            }
            val digit = get(x, y)!!
            set(x, y, '.')
            var string = removeNumber(x - 1, y) ?: ""
            string += digit
            string += removeNumber(x + 1, y) ?: ""


            return string
        }

        fun removeNumbersNextToSymbols(): Grid {
            val grid = Grid(values.map { it.copyOf() }.toTypedArray())

            val sequence = sequence {
                for (y in grid.values.indices) {
                    for (x in grid.values[y].indices) {
                        yield(GridEntry(x, y, values[y][x]))
                    }
                }
            }

            sequence.forEach { entry ->
                if (!entry.value.isDigit() && entry.value != '.') {
                    val x = entry.x
                    val y = entry.y

                    grid.removeNumber(x + 1, y + 1)
                    grid.removeNumber(x + 1, y)
                    grid.removeNumber(x + 1, y - 1)
                    grid.removeNumber(x, y + 1)
                    grid.removeNumber(x, y - 1)
                    grid.removeNumber(x - 1, y + 1)
                    grid.removeNumber(x - 1, y)
                    grid.removeNumber(x - 1, y - 1)
                }
            }

            return grid
        }

        fun getGearRatios(): List<Int> {
            val grid = Grid(values.map { it.copyOf() }.toTypedArray())

            val sequence = sequence {
                for (y in values.indices) {
                    for (x in values[y].indices) {
                        yield(GridEntry(x, y, values[y][x]))
                    }
                }
            }

            val results = mutableListOf<Int>()

            sequence.forEach { entry ->
                if (entry.value == '*') {
                    val x = entry.x
                    val y = entry.y
                    val numbers = listOfNotNull(
                        grid.removeNumber(x + 1, y + 1),
                        grid.removeNumber(x + 1, y),
                        grid.removeNumber(x + 1, y - 1),
                        grid.removeNumber(x, y + 1),
                        grid.removeNumber(x, y - 1),
                        grid.removeNumber(x - 1, y + 1),
                        grid.removeNumber(x - 1, y),
                        grid.removeNumber(x - 1, y - 1),
                    )
                    if (numbers.size == 2) {
                        val (a, b) = numbers
                        val ratio = a.toInt() * b.toInt()
                        results.add(ratio)
                    }
                }
            }

            return results
        }

        fun getNumbers() = sequence {
            for (y in values.indices) {
                val row = values[y].concatToString()
                val numbers = """\d+""".toRegex().findAll(row).map { it.value.toInt() }.toList()
                yieldAll(numbers)
            }
        }

        fun getDiff(other: Grid): Grid {
            return Grid(values.zip(other.values).map { (row1, row2) -> rowDiff(row1, row2) }.toTypedArray())
        }

        companion object {
            fun rowDiff(row1: CharArray, row2: CharArray): CharArray {
                return row1.zip(row2).map { (c1, c2) -> if (c1 == c2) '.' else c1 }.toCharArray()
            }
        }
    }
}