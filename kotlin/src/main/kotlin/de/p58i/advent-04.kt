package de.p58i

import java.io.File
import java.util.LinkedList

class ElfCleaningPair(
        val firstElfRange: IntRange,
        val secondElfRange: IntRange,
) {
    val fullIntersect: Boolean = firstElfRange.toList().containsAll(secondElfRange.toList())
            || secondElfRange.toList().containsAll(firstElfRange.toList())

    val partIntersect: Boolean = !firstElfRange.none { secondElfRange.contains(it) }
}

fun main() {
    val pairings = LinkedList<ElfCleaningPair>()
    File("./task-inputs/advent-04.input").forEachLine { inputLine ->
        val elfRanges = inputLine.split(",")
        assert(elfRanges.size == 2)

        val firstRangeBounds = elfRanges.first().split("-").map { it.toInt() }
        assert(firstRangeBounds.size == 2)
        val secondRangeBounds = elfRanges.last().split("-").map { it.toInt() }
        assert(secondRangeBounds.size == 2)
        pairings.add(ElfCleaningPair(
                firstRangeBounds.first()..firstRangeBounds.last(),
                secondRangeBounds.first()..secondRangeBounds.last())
        )
    }
    println(
            "Full Intersect Pairs: ${pairings.filter { it.fullIntersect }.map {
                println("${it.firstElfRange}|${it.secondElfRange}")
                it
            }.count()}"
    )

    println(
            "Part Intersect Pairs: ${pairings.filter { it.partIntersect }.map {
                println("${it.firstElfRange}|${it.secondElfRange}")
                it
            }.count()}"
    )
}
