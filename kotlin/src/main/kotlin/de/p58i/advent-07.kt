package de.p58i

import java.io.File
import java.util.*
import kotlin.collections.HashSet

data class ElfDir(
        val name: String
){
    var parentDir: ElfDir = this
    val dirs = HashSet<ElfDir>()
    val files = LinkedList<ElfFile>()

    fun addDir(dir: ElfDir){
        dir.parentDir = this
        dirs.add(dir)
    }
    fun getSize(): Int = dirs.sumOf { it.getSize() } + files.sumOf { it.size }

    fun getFlatSubdirs():List<ElfDir>{
        return dirs.toList() + dirs.flatMap { it.getFlatSubdirs() }
    }
    fun getTabs(): String{
        var tabs = "\t"
        var currentDir = this
        while(currentDir.parentDir != rootDir){
            tabs += "\t"
            currentDir = currentDir.parentDir
        }
        return tabs
    }
    override fun toString(): String {
        return "- $name (dir, size=${getSize()})\n" +
                "${files.joinToString(prefix = "${getTabs()}+\t", separator = ",") { "${it.name} (file, size=${it.size})" }}\n" +
                "${dirs.joinToString(prefix = "${getTabs()}", separator = "${getTabs()}") { it.toString() }}\n"
    }
}

data class ElfFile(
        val name: String,
        val size: Int
)

val rootDir = ElfDir("/")
fun main() {

    var currentDir = rootDir
    File("./task-inputs/advent-07.input").forEachLine {
        if(it.startsWith("$ cd")){
            currentDir = changeDir(it, currentDir)
        } else if (it.startsWith("dir")){
            addDir(it, currentDir)
        } else if (it[0].isDigit()){
            addFile(it, currentDir)
        }
    }
    print(rootDir)
    println("######################")
    val bigDirs = rootDir.getFlatSubdirs().filter { it.getSize() <= 100000 }
    print(bigDirs.joinToString(prefix = " - ", separator = "\n - ") { "${it.name} (${it.getSize()})" })
    println("BigDirSum=${bigDirs.sumOf { it.getSize() }}")

    println("Diskspace ${rootDir.getSize()}/70000000 (${70000000 - rootDir.getSize()})")
    val freeSpace = 70000000 - rootDir.getSize()
    val spaceNeeded = 30000000 - freeSpace

    println("Space needed: $spaceNeeded")
    val dirToDelete= rootDir.getFlatSubdirs().filter { it.getSize() >= spaceNeeded }.minBy { it.getSize() - spaceNeeded }
    println("Dir to delete: $dirToDelete")
}

fun addFile(fileLine: String, currentDir: ElfDir) {
    val fileLineSplit = fileLine.split(" ")
    assert(fileLineSplit.size == 2)
    val fileSize = fileLineSplit[0].toInt()
    val fileName= fileLineSplit[1]
    currentDir.files.add(ElfFile(fileName, fileSize))
}

fun addDir(dirLine: String, currentDir: ElfDir) {
    val dirName = dirLine.removePrefix("dir ")
    currentDir.addDir(ElfDir(dirName))
}

fun changeDir(command: String, currentDir: ElfDir): ElfDir {
    return when(val targetDir = command.removePrefix("$ cd ")){
        ".." -> currentDir.parentDir
        "/" -> rootDir
        else -> currentDir.dirs.find { it.name == targetDir } ?: throw IllegalArgumentException("Unkown target dir ($targetDir) for dir [$currentDir]")
    }
}