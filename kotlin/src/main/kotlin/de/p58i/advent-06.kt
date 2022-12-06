package de.p58i

import java.io.File
import java.util.LinkedList

fun main() {
    val markerIndex = File("./task-inputs/advent-06.input").readBytes()
            .map { it.toInt().toChar() }
            .windowed(4)
            .indexOfFirst { it.toSet().size == 4 }
    val messageIndex = File("./task-inputs/advent-06.input").readBytes()
            .map { it.toInt().toChar() }
            .windowed(14)
            .indexOfFirst { it.toSet().size == 14 }

    println("Marker chars to process: ${markerIndex + 4}")
    println("Message chars to process: ${messageIndex + 14}")
}
