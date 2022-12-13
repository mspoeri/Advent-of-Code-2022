package de.p58i

import java.io.File

data class Item(
        val worryLevel: Long,
)

const val debug11 = false

data class Monkey(
        val items: MutableList<Item>,
        val inspection: ((old: Long) -> Long),
        val testDiff: Int,
        val trueMonkeyIndex: Int,
        val falseMonkeyIndex: Int,
        var itemInspections: Long = 0,
) {
    fun inspect(item: Item): Pair<Int, Item> {
        if (debug11) {
            println("Monkey inspects an item with a worry level of ${item.worryLevel}.")
        }
        val worryLevel = inspection(item.worryLevel) % leastCommonMultiple
        if (debug11) {
            println("Worry level changed to $worryLevel.")
        }
        val nextMonkey: Int = if (worryLevel % testDiff.toLong() == 0L) {
            if (debug11) {
                println("Current worry level IS divisible by $testDiff.")
            }
            trueMonkeyIndex
        } else {
            if (debug11) {
                println("Current worry level IS NOT divisible by $testDiff.")
            }
            falseMonkeyIndex
        }

        itemInspections += 1
        if (debug11) {
            println("Item with worry level $worryLevel is thrown to monkey $nextMonkey.")
        }
        return Pair(nextMonkey, Item(worryLevel))
    }
}

val testInput = listOf(
        Monkey(
                items = mutableListOf(),
                inspection = { old -> old * 19 },
                testDiff = -1,
                trueMonkeyIndex = -1,
                falseMonkeyIndex = -1,
        ),
        Monkey(
                items = mutableListOf(),
                inspection = { old -> old + 6 },
                testDiff = -1,
                trueMonkeyIndex = -1,
                falseMonkeyIndex = -1,
        ),
        Monkey(
                items = mutableListOf(),
                inspection = { old -> old * old },
                testDiff = -1,
                trueMonkeyIndex = -1,
                falseMonkeyIndex = -1,
        ),
        Monkey(
                items = mutableListOf(),
                inspection = { old -> old + 3 },
                testDiff = -1,
                trueMonkeyIndex = -1,
                falseMonkeyIndex = -1,
        )
)

val input = listOf(
        Monkey(
                items = mutableListOf(),
                inspection = { old -> old * 7 },
                testDiff = -1,
                trueMonkeyIndex = -1,
                falseMonkeyIndex = -1,
        ),
        Monkey(
                items = mutableListOf(),
                inspection = { old -> old + 5 },
                testDiff = -1,
                trueMonkeyIndex = -1,
                falseMonkeyIndex = -1,
        ),
        Monkey(
                items = mutableListOf(),
                inspection = { old -> old * old },
                testDiff = -1,
                trueMonkeyIndex = -1,
                falseMonkeyIndex = -1,
        ),
        Monkey(
                items = mutableListOf(),
                inspection = { old -> old + 6 },
                testDiff = -1,
                trueMonkeyIndex = -1,
                falseMonkeyIndex = -1,
        ),
        Monkey(
                items = mutableListOf(),
                inspection = { old -> old * 11 },
                testDiff = -1,
                trueMonkeyIndex = -1,
                falseMonkeyIndex = -1,
        ),
        Monkey(
                items = mutableListOf(),
                inspection = { old -> old + 8 },
                testDiff = -1,
                trueMonkeyIndex = -1,
                falseMonkeyIndex = -1,
        ),
        Monkey(
                items = mutableListOf(),
                inspection = { old -> old + 1 },
                testDiff = -1,
                trueMonkeyIndex = -1,
                falseMonkeyIndex = -1,
        ),
        Monkey(
                items = mutableListOf(),
                inspection = { old -> old + 4 },
                testDiff = -1,
                trueMonkeyIndex = -1,
                falseMonkeyIndex = -1,
        ),
)

var leastCommonMultiple: Int = 1
fun main() {
    var inputMonkeyIndex = 0
    val monkeyList = input.toMutableList()
    File("./task-inputs/advent-11.input").forEachLine {
        when {
            it.trim().startsWith("Monkey ") -> inputMonkeyIndex = it.removeSuffix(":").split(" ")[1].toInt()
            it.trim().startsWith("Starting items") -> monkeyList[inputMonkeyIndex].withItems(it.split(": ")[1])
            it.trim().startsWith("Test: divisible by ") -> monkeyList[inputMonkeyIndex] = monkeyList[inputMonkeyIndex].copy(testDiff = it.split(": ")[1].removePrefix("divisible by ").toInt())
            it.trim().startsWith("If true: throw to monkey ") -> monkeyList[inputMonkeyIndex] = monkeyList[inputMonkeyIndex].copy(trueMonkeyIndex = it.split(": ")[1].removePrefix("throw to monkey ").toInt())
            it.trim().startsWith("If false: throw to monkey ") -> monkeyList[inputMonkeyIndex] = monkeyList[inputMonkeyIndex].copy(falseMonkeyIndex = it.split(": ")[1].removePrefix("throw to monkey ").toInt())
        }
    }
    leastCommonMultiple = monkeyList.map { it.testDiff }.fold(1){ acc, i -> acc*i }
    println(monkeyList.joinToString("\n") { it.toString() })

    repeat(10000) {
        monkeyList.forEachIndexed { index, monkey ->
            if (debug11) {
                println("\nMonkey $index")
            }
            monkey.items.forEach {
                val inspectionResult = monkey.inspect(it)
                monkeyList[inspectionResult.first].items.add(inspectionResult.second)
            }
            monkey.items.clear()
        }
    }
    println(
            monkeyList.joinToString(separator = "\n") { "Monkey inspected items ${it.itemInspections} times." }
    )
    println("Monkey-Business-Index: ${
        monkeyList.sortedBy { it.itemInspections }.takeLast(2).fold(1L) { acc, monkey -> acc * monkey.itemInspections }
    }")
}

fun Monkey.withItems(itemString: String) {
    itemString.trim().split(", ").map { it.toLong() }.map { Item(it) }.forEach { this.items.add(it) }
}