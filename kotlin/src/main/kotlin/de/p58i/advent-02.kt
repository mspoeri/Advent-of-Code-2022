package de.p58i

import java.io.File
import java.util.LinkedList

val permutations = setOf(
        mapOf(
                "X" to Move.ROCK,
                "Y" to Move.PAPER,
                "Z" to Move.SCISSORS,
        ),
        mapOf(
                "X" to Move.ROCK,
                "Y" to Move.SCISSORS,
                "Z" to Move.PAPER,
        ),
        mapOf(
                "X" to Move.SCISSORS,
                "Y" to Move.PAPER,
                "Z" to Move.ROCK,
        ),
        mapOf(
                "X" to Move.SCISSORS,
                "Y" to Move.ROCK,
                "Z" to Move.PAPER,
        ),
        mapOf(
                "X" to Move.PAPER,
                "Y" to Move.SCISSORS,
                "Z" to Move.ROCK,
        ),
        mapOf(
                "X" to Move.PAPER,
                "Y" to Move.ROCK,
                "Z" to Move.SCISSORS,
        ),
)

enum class Move(val value: Int) {
    ROCK(1) {
        override val beats: Move
            get() = SCISSORS
        override val beatenBy: Move
            get() = PAPER
    },
    PAPER(2) {
        override val beats: Move
            get() = ROCK
        override val beatenBy: Move
            get() = SCISSORS
    },
    SCISSORS(3) {
        override val beats: Move
            get() = PAPER
        override val beatenBy: Move
            get() = ROCK
    };

    abstract val beats: Move
    abstract val beatenBy: Move
    fun beats(other: Move): Boolean = other == beats
    fun draws(other: Move): Boolean = other == this
}

fun String.toMove(): Move = when (this) {
    "A" -> Move.ROCK
    "B" -> Move.PAPER
    "C" -> Move.SCISSORS
    else -> throw IllegalArgumentException()
}

class SeedRound(
        val opponentMove: Move,
        val ownMove: String,
)

class GameRound(
        val opponentMove: Move,
        val ownMove: Move,
) {
    val score: Int
        get() {
            return if (win) {
                6 + ownMove.value
            } else if (draw) {
                3 + ownMove.value
            } else {
                ownMove.value
            }
        }
    val win = ownMove.beats(opponentMove)
    val draw = ownMove.draws(opponentMove)
}

class Game(
        val rounds: Collection<GameRound>,
        val mappings: Map<String, Move>
) {
    val xMapping = lazy { mappings["X"]!! }
    val yMapping = lazy { mappings["Y"]!! }
    val zMapping = lazy { mappings["Z"]!! }

    val wins = rounds.count { it.win }
    val draws = rounds.count { it.draw }
    val score: Int
        get() = rounds.sumOf { it.score }
}

fun createPermutationGames(seedGame: Collection<SeedRound>): List<Game> {
    return permutations.map { mapping ->
        Game(seedGame.map { seedRound -> GameRound(seedRound.opponentMove, mapping[seedRound.ownMove]!!) }, mapping)
    }
}

fun String.toWinLoseDraw() = when (this) {
    "X" -> "LOSE"
    "Y" -> "DRAW"
    "Z" -> "WIN"
    else -> throw IllegalArgumentException()
}

fun calcCounterMove(opponentMove: Move, outcome: String): Move = when (outcome) {
    "LOSE" -> opponentMove.beats
    "DRAW" -> opponentMove
    "WIN" -> opponentMove.beatenBy
    else -> throw IllegalArgumentException()
}

fun createWinLoseStrategyGame(seedGame: Collection<SeedRound>) = Game(
        seedGame.map {
            SeedRound(it.opponentMove, it.ownMove.toWinLoseDraw())
        }.map {
            GameRound(it.opponentMove, calcCounterMove(it.opponentMove, it.ownMove))
        }, emptyMap()
)

fun main() {
    val seedGame = LinkedList<SeedRound>()
    File("./task-inputs/advent-02.input").forEachLine {
        val opponentMove = it.split(" ").first()
        val ownMove = it.split(" ").last()
        seedGame.add(SeedRound(opponentMove.toMove(), ownMove))
    }
    performPermutationStrategy(seedGame)
    val winLoseGame = createWinLoseStrategyGame(seedGame)

    println("""
        Win Lose Game Scores ${winLoseGame.score}[${winLoseGame.rounds.size}/${winLoseGame.wins}/${winLoseGame.draws}]
        """.trimIndent())
}


private fun performPermutationStrategy(seedGame: LinkedList<SeedRound>) {
    val games = createPermutationGames(seedGame)

    println(
            games.joinToString(separator = "\n") {
                """
                    Game score: ${it.score}[${it.rounds.size}/${it.wins}/${it.draws}]
                    X = ${it.xMapping}
                    Y = ${it.yMapping}
                    Z = ${it.zMapping}
                """.trimIndent()
            }
    )

    val bestGame = games.maxBy { it.score }

    println("""
        Best Game Scores ${bestGame.score}
        X = ${bestGame.xMapping}
        Y = ${bestGame.yMapping}
        Z = ${bestGame.zMapping}
        """.trimIndent())
}
