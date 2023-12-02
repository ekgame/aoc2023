import kotlin.math.min

fun main() {
    fun parseBag(line: String): Day02.Bag {
        val items = line.split(",")
            .map {
                val entry = it.trim()
                val regex = """(\d+) (red|green|blue)""".toRegex()
                val match = regex.matchEntire(entry) ?: error("Invalid bag line: $line")
                val (count, color) = match.destructured
                val colorEnum = when (color) {
                    "red" -> Day02.Color.RED
                    "green" -> Day02.Color.GREEN
                    "blue" -> Day02.Color.BLUE
                    else -> error("Invalid color: $color")
                }
                colorEnum to count.toInt()
            }

        return Day02.Bag(items.toMap())
    }

    fun parseGame(line: String): Day02.Game {
        val regex = """Game (\d+): (.*)""".toRegex()
        val match = regex.matchEntire(line) ?: error("Invalid game line: $line")
        val (id, bags) = match.destructured
        return Day02.Game(id.toInt(), bags.split(";").map { parseBag(it) })
    }

    fun part1(input: List<String>): Int {
        val bag = Day02.Bag(
            mapOf(
                Day02.Color.RED to 12,
                Day02.Color.GREEN to 13,
                Day02.Color.BLUE to 14,
            )
        )
        val games = input
            .map { parseGame(it) }
            .filter { it.bags.all { bag.contains(it) } }

        return games.sumOf { it.id }
    }

    fun part2(input: List<String>): Int {
        return input
            .map { parseGame(it).minBag() }
            .sumOf { it.entries.values.reduce { acc, i -> acc*i } }
    }

    val testInput = readInput("Day02.test")

    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

class Day02 {
    enum class Color {
        RED, GREEN, BLUE
    }

    class Bag(val entries: Map<Color, Int>) {
        fun contains(bag: Bag): Boolean {
            return bag.entries.all { (color, count) ->
                (entries[color] ?: 0) >= count
            }
        }
    }

    data class Game(val id: Int, val bags: List<Bag>) {
        fun minBag(): Bag {
            val sums = Color.entries.associateWith { 0 }.toMutableMap()

            bags.forEach { bag ->
                bag.entries.forEach { (color, count) ->
                    if (sums[color]!! < count) {
                        sums[color] = count
                    }
                }
            }

            return Bag(sums)
        }
    }
}
