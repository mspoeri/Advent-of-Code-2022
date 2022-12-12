package de.p58i

import java.io.File

data class Tree(
        val height: Int
) {
    var visible: Boolean = false
    var scenicScore: Int = 0
}

val lowTree = Tree(-1)
fun main() {
    val forest = ArrayList<Array<Tree>>()
    File("./task-inputs/advent-08.input").forEachLine {
        forest.add(
                it.map { treeInput ->
                    Tree(Integer.parseInt(treeInput.toString()))
                }.toTypedArray()
        )
    }

    lookFromLeft(forest)
    printForest(forest)
    println()
    lookFromRight(forest)
    printForest(forest)
    println()
    lookFromTop(forest)
    printForest(forest)
    println()
    lookFromBottom(forest)

    printForest(forest)
    println()

    calcScenicScores(forest)
    printScenicScores(forest)

    val mostScenicTrees = forest.flatMap { it.asList() }.filter { it.visible }

    println("""
        Marker Count: ${forest.flatMap { it.asList() }.count { it.visible }}
        Max Scenic Score: ${mostScenicTrees.maxBy { it.scenicScore }.scenicScore}
    """.trimIndent())
}

private fun printForest(forest: ArrayList<Array<Tree>>) {
    println(forest.joinToString(separator = "\n") { forrestRow ->
        forrestRow.joinToString(separator = "") {
            if (it.visible) {
                "v"
            } else {
                it.height.toString()
            }
        }
    })
}

private fun printScenicScores(forest: ArrayList<Array<Tree>>) {
    println(forest.joinToString(separator = "\n") { forrestRow ->
        forrestRow.joinToString(separator = " ") {
            if (it.visible) {
                String.format("%06d", it.scenicScore)
            } else {
                String.format("%06d", it.scenicScore)
            }
        }
    })
}

fun lookFromLeft(forest: ArrayList<Array<Tree>>) {
    for (row in 0 until forest.size) {
        var highestTree = lowTree
        for (column in (0 until forest[row].size)) {
            val tree = forest[row][column]
            if (tree.height > highestTree.height) {
                highestTree = tree
                forest[row][column].visible = true
            }
        }
    }
}

fun lookFromRight(forest: ArrayList<Array<Tree>>) {
    for (row in 0 until forest.size) {
        var highestTree = lowTree
        for (column in (0 until forest[row].size).reversed()) {
            val tree = forest[row][column]
            if (tree.height > highestTree.height) {
                highestTree = tree
                forest[row][column].visible = true
            }
        }
    }
}

fun lookFromTop(forest: ArrayList<Array<Tree>>) {
    val columns = forest.first().size
    for (column in 0 until columns) {
        var highestTree = lowTree
        for (row in 0 until forest.size) {
            val tree = forest[row][column]
            if (tree.height > highestTree.height) {
                highestTree = tree
                forest[row][column].visible = true
            }
        }
    }
}

fun lookFromBottom(forest: ArrayList<Array<Tree>>) {
    val columns = forest.first().size
    for (column in 0 until columns) {
        var highestTree = lowTree
        for (row in (0 until forest.size).reversed()) {
            val tree = forest[row][column]
            if (tree.height > highestTree.height) {
                highestTree = tree
                forest[row][column].visible = true
            }
        }
    }
}

fun calcScenicScores(forest: ArrayList<Array<Tree>>) {
    forest.forEachIndexed { row, trees ->
        trees.forEachIndexed { column, tree ->
            tree.calcScenicScore(forest, row, column)
        }
    }
}

fun Tree.calcScenicScore(forest: ArrayList<Array<Tree>>, row: Int, column: Int) {

    val upTrees = (column - 1 downTo 0).map { forest[row][it] }
    val downTrees = (column + 1 until forest[row].size).map { forest[row][it] }

    val leftTrees = (row - 1 downTo 0).map { forest[it][column] }
    val rightTrees = (row + 1 until forest.size).map { forest[it][column] }

    val upScore = calcScenicForTreeRange(upTrees)
    val downScore = calcScenicForTreeRange(downTrees)
    val leftScore = calcScenicForTreeRange(leftTrees)
    val rightScore = calcScenicForTreeRange(rightTrees)

    this.scenicScore = upScore * downScore * leftScore * rightScore
}

private fun Tree.calcScenicForTreeRange(upTrees: List<Tree>): Int {
    var scenicScore = 0
    for (currentTree in upTrees) {
        if (currentTree.height < this.height) {
            scenicScore += 1
        }
        if (currentTree.height >= this.height) {
            scenicScore += 1
            break
        }
    }
    return scenicScore
}

