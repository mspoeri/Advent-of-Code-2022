package de.p58i

import java.io.File
import java.util.LinkedList


class Backpack(
        val items: List<Char>
) {
    val firstCompartment: List<Char>
    val secondCompartment: List<Char>

    val sharedItem: Char

    companion object{
        fun separateCompartments(items: List<Char>): Pair<List<Char>,List<Char>>{
            assert(items.size.mod(2) == 0)

            val splitIndex = items.size / 2
            val firstCompartment = items.subList(0, splitIndex)
            val secondCompartment = items.subList(splitIndex, items.size)

            assert(firstCompartment.size == secondCompartment.size)

            return Pair(firstCompartment, secondCompartment)
        }
    }
    init {
        val (first, second) = separateCompartments(items)
        firstCompartment = first
        secondCompartment = second

        val sharedList = firstCompartment.intersect(secondCompartment)
        assert(sharedList.size == 1)
        sharedItem = sharedList.first()
    }
}

class ElfGroup(
        val backpacks: List<Backpack>
) {
    val groupIdentifier: Char

    init {
        val identifier = backpacks.map{ it.items}.fold(backpacks.first().items){ acc, chars ->
            acc.intersect(chars).toList()
        }
        assert(identifier.size == 1)
        groupIdentifier = identifier.first()
    }
}

fun Char.toPriority(): Int {
  return if (this - 'a'  < 0){
      this - 'A' + 27
  } else {
      this - 'a' + 1
  }
}

fun main(){
    val backpacks = LinkedList<Backpack>()
    File("./task-inputs/advent-03.input").forEachLine {
        backpacks.add(Backpack(it.toCharArray().toList()))
    }

    backpacks.forEachIndexed { index, backpack ->
        println("#$index:\t ${backpack.sharedItem}(${backpack.sharedItem.toPriority()})")
    }
    println("shared priority sum: ${backpacks.sumOf { it.sharedItem.toPriority() }}")

    val groups = backpacks.chunked(3).map { ElfGroup(it) }

    groups.forEachIndexed { index, elfGroup ->
        println("#$index:\t ${elfGroup.groupIdentifier}(${elfGroup.groupIdentifier.toPriority()})")
    }
    println("group priority sum: ${groups.sumOf { it.groupIdentifier.toPriority() }}")
}
