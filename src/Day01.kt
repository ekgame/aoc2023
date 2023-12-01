import kotlin.math.min

fun main() {
    fun parseLine(line: String): String =
        Regex("""\d""")
        .findAll(line)
        .map { it.value }
        .joinToString("")

    fun part1(input: List<String>): Int {
        return input
            .map { parseLine(it) }
            .sumOf { (it.first().toString() + it.last()).toInt() }
    }

    fun parseLine2(line: String): String {
        var index = 0
        var result = ""

        fun peekEquals(expected: String): Boolean {
            return line.substring(index, min(index + expected.length, line.length)) == expected
        }

        while (index < line.length) {
            when {
                peekEquals("1") || peekEquals("one") -> result += "1"
                peekEquals("2") || peekEquals("two") -> result += "2"
                peekEquals("3") || peekEquals("three") -> result += "3"
                peekEquals("4") || peekEquals("four") -> result += "4"
                peekEquals("5") || peekEquals("five") -> result += "5"
                peekEquals("6") || peekEquals("six") -> result += "6"
                peekEquals("7") || peekEquals("seven") -> result += "7"
                peekEquals("8") || peekEquals("eight") -> result += "8"
                peekEquals("9") || peekEquals("nine") -> result += "9"
                peekEquals("0") || peekEquals("zero") -> result += "0"
                else -> {}
            }

            index++
        }

        return result
    }

    fun part2(input: List<String>): Int {
        return input
            .map { parseLine2(it) }
            .sumOf { (it.first().toString() + it.last()).toInt() }
    }

    val testInput1 = readInput("Day01.test")
    val testInput2 = readInput("Day01.test2")

    check(part1(testInput1) == 142)
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

