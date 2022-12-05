package de.p58i

import java.io.File
import java.util.LinkedList

data class CrateMove(
        val crateCount: Int,
        val sourceStackIndex: Int,
        val targetStackIndex: Int,
)

const val debug = true
fun main() {
    val stacks = ArrayList<LinkedList<Char>>()
    val moves = LinkedList<CrateMove>()
    File("./task-inputs/advent-05.input").forEachLine { inputLine ->
        if(inputLine.startsWith("[")){
            "$inputLine ".chunked(4).forEachIndexed { index, crateSlot ->
                if (stacks.getOrNull(index) == null) {
                    stacks.add(index, LinkedList())
                }
                if (crateSlot.trim().isNotEmpty()){
                    stacks[index].addFirst(crateSlot[1])
                }
            }
        } else if (inputLine.startsWith("move")){
            val moveNumbers = inputLine.replace("move ", "")
                    .replace(" from ","#")
                    .replace(" to ", "#")
                    .split("#")
            assert(moveNumbers.size == 3)
            moves.add(CrateMove(moveNumbers[0].toInt(), moveNumbers[1].toInt() -1,moveNumbers[2].toInt() -1))
        } else {
            // Skip
        }
    }

    println(stackToString(stacks))
    moveWithCrateMover9001(moves, stacks)
    println(
            stackToString(stacks)
    )
    println(
            stacks.map { it.last }.joinToString(separator = "") { "$it" }
    )
}

private fun movesToString(moves: LinkedList<CrateMove>) =
        moves.joinToString("\n") { moveToString(it) }

private fun moveToString(it: CrateMove) = "move ${it.crateCount} from ${it.sourceStackIndex} to ${it.targetStackIndex}"

private fun moveWithCrateMover9000(moves: LinkedList<CrateMove>, stacks: ArrayList<LinkedList<Char>>) {
    moves.forEach { crateMove ->
        repeat(crateMove.crateCount) {
            val crateToMove = stacks[crateMove.sourceStackIndex].removeLast()
            stacks[crateMove.targetStackIndex].addLast(crateToMove)

        }
    }
}

private fun moveWithCrateMover9001(moves: LinkedList<CrateMove>, stacks: ArrayList<LinkedList<Char>>) {
    moves.forEach { crateMove ->
        val cratesToMove = stacks[crateMove.sourceStackIndex].takeLast(crateMove.crateCount)
        cratesToMove.forEach {
            stacks[crateMove.sourceStackIndex].removeLast()
            stacks[crateMove.targetStackIndex].addLast(it)
        }
        if(debug){
            println( moveToString(crateMove))
            println( stackToString(stacks))
        }
    }
}

private fun stackToString(stacks: ArrayList<LinkedList<Char>>) =
        stacks.joinToString(separator = "\n") { stack ->
            """
                ${stack.joinToString(separator = " ") { "$it" }}
            """.trimIndent()
        }
