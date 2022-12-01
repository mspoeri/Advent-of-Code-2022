package de.p58i

import java.io.File
import java.util.LinkedList

fun main(){
    val elfs = LinkedList<MutableList<Long>>()
    elfs.add(LinkedList())
    File("./task-inputs/advent-01.input").forEachLine {
        if (it == ""){
            elfs.add(LinkedList())
        } else {
            elfs.last.add(it.toLong())
        }
    }
    println(elfs.joinToString(separator = "\n") { elf -> elf.joinToString(separator = " ") { calorie -> calorie.toString() } })
    val elfsSum = elfs.mapIndexed{ index, calories -> Pair(index, calories.sum()) }
    val maxElf = elfsSum.maxBy { it.second }
    val top3 = elfsSum.sortedByDescending { it.second }.take(3)
    val top3Sum = top3.sumOf { it.second }
    println("Elf #${maxElf.first} carries ${maxElf.second} calories")

    println("Top 3 Elves")
    top3.forEach { println("Elf #${it.first} carries ${it.second} calories") }
    println("Sum: $top3Sum")
}
