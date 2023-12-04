import kotlin.math.min
import kotlin.math.pow

fun main() {
    fun parseCard(line: String): Day04.Card {
        val regex = """Card\h+(\d+): (.*?) \| (.*)""".toRegex()
        val numberRegex = """\d+""".toRegex()
        val (id, winningNumbers, haveNumbers) = regex.matchEntire(line)!!.destructured
        val winningNumbersSet = numberRegex.findAll(winningNumbers).map { it.value.toInt() }.toSet()
        val haveNumbersSet = numberRegex.findAll(haveNumbers).map { it.value.toInt() }.toSet()
        return Day04.Card(id.toInt(), winningNumbersSet, haveNumbersSet)
    }

    fun part1(input: List<String>): Int {
        return input.sumOf {
            val card = parseCard(it)
            val num = card.haveNumbers.count { num -> card.winningNumbers.contains(num) }
            when (num) {
                0 -> 0
                else -> 2.0.pow(num.toDouble() - 1).toInt()
            }
        }
    }

    fun part2(input: List<String>): Int {
        val numCards = mutableMapOf<Int, Int>()

        fun increment(id: Int) {
            val currentValue = numCards[id] ?: 0
            numCards[id] = currentValue + 1
        }

        input
            .map { parseCard(it) }
            .forEach { card ->
                increment(card.id)
                repeat(numCards[card.id]!!) {
                    val num = card.haveNumbers.count { num -> card.winningNumbers.contains(num) }
                    (1..num).forEach {
                        increment(card.id + it)
                    }
                }
            }

        return numCards.values.sum()
    }

    val testInput = readInput("Day04.test")

    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}

class Day04 {
    data class Card(
        val id: Int,
        val winningNumbers: Set<Int>,
        val haveNumbers: Set<Int>
    )
}
