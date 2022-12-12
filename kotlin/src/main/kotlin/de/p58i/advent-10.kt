package de.p58i

import java.io.File
import java.util.LinkedList

fun main() {
    val cycles = LinkedList<Int>()
    var x = 1
    cycles.add(x)
    File("./task-inputs/advent-10.input").forEachLine {
        if (it == "noop") {
            cycles.add(x)
        }
        if (it.startsWith("addx")) {
            cycles.add(x)
            x += it.split(" ")[1].toInt()
            cycles.add(x)
        }
    }
    println("""
        020th cycle: ${20 * cycles[20]} (${cycles[20]})
        060th cycle: ${60 * cycles[60]} (${cycles[60]})
        100th cycle: ${100 * cycles[100]} (${cycles[100]})
        140th cycle: ${140 * cycles[140]} (${cycles[140]})
        180th cycle: ${180 * cycles[180]} (${cycles[180]})
        220th cycle: ${220 * cycles[220]} (${cycles[220]})
        
        Sum:${
        20 * cycles[20]
                + 60 * cycles[60]
                + 100 * cycles[100]
                + 140 * cycles[140]
                + 180 * cycles[180]
                + 220 * cycles[220]
    }
    """.trimIndent())

    val crt = LinkedList<String>()
    for (cycleIndex in 0 until cycles.size - 1){
        val spriteIndex = cycleIndex % 40
        val spriteRange = spriteIndex - 1.. spriteIndex + 1
        if (spriteRange.contains(cycles[cycleIndex])){
            crt.add("#")
        } else {
            crt.add(".")
        }
    }

    println(
            crt.chunked(40).joinToString("\n") {line ->
                line.joinToString(separator = "") { it }
            }
    )
}
