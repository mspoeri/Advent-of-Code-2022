package de.p58i

import de.p58i.RopeMove.Companion.toRopeMove
import java.io.File
import java.util.LinkedList
import kotlin.math.abs
import kotlin.math.sign

enum class RopeMove {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    companion object {
        fun Char.toRopeMove(): RopeMove =
                when (this.lowercaseChar()) {
                    'r' -> RIGHT
                    'l' -> LEFT
                    'u' -> UP
                    'd' -> DOWN
                    else -> throw IllegalArgumentException("Unable to convert [$this]")
                }
    }
}

data class Position(
        val x: Int,
        val y: Int,
) {
    fun move(ropeMove: RopeMove) =
        when (ropeMove) {
            RopeMove.UP -> Position(x, y + 1)
            RopeMove.DOWN -> Position(x, y - 1)
            RopeMove.LEFT -> Position(x - 1, y)
            RopeMove.RIGHT -> Position(x + 1, y)
        }

    fun drag(otherPosition: Position): Position {
        val xOffset = otherPosition.x - x
        val yOffset = otherPosition.y - y

        return if (abs(xOffset) < 2 && abs(yOffset) < 2) {
            this
        } else if ((xOffset == 0) || (yOffset == 0)) {
            Position(x + (xOffset/2), y + (yOffset/2))
        } else {
            Position(x + (1 * xOffset.sign),y+ (1 * yOffset.sign))
        }
    }
}

fun main() {
    val ropeMoves = LinkedList<RopeMove>()
    File("./task-inputs/advent-09.input").forEachLine {
        val moveDirection = it.split(" ")[0].toCharArray().first().toRopeMove()
        val moveTimes = it.split(" ")[1].toInt()

        repeat(moveTimes) {
            ropeMoves.add(moveDirection)
        }
    }
    headTailMove(ropeMoves)
    multiKnotMove(ropeMoves)
}

const val knotCount = 10
fun multiKnotMove(ropeMoves: List<RopeMove>) {
    val knots = ArrayList<LinkedList<Position>>()
    repeat(knotCount){
        knots.add(LinkedList(listOf(Position(0,0))))
    }
    ropeMoves.forEach { move ->
        knots.first().add(knots.first().last.move(move))
        for (knotIndex in 1 until knots.size){
            val knotPositions = knots[knotIndex]
            knotPositions.add(knotPositions.last.drag(knots[knotIndex - 1].last))
        }
    }
    println("Unique tail positions: ${knots.last().toSet().count()}")
}

private fun headTailMove(ropeMoves: LinkedList<RopeMove>) {
    val headPositions = LinkedList<Position>()
    val tailPositions = LinkedList<Position>()

    headPositions.add(Position(0, 0))
    tailPositions.add(Position(0, 0))
    ropeMoves.forEach {
        val currentHeadPosition = headPositions.last
        val currentTailPosition = tailPositions.last

        val nextHeadPosition = currentHeadPosition.move(it)
        val nextTailPosition = currentTailPosition.drag(nextHeadPosition)

        headPositions.add(nextHeadPosition)
        tailPositions.add(nextTailPosition)
    }

    println("Unique tail positions: ${tailPositions.toSet().count()}")
}
