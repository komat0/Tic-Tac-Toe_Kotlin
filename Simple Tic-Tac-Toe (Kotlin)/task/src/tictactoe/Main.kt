package tictactoe

fun main() {

    val battleField = fillTheField()
    printGame(battleField)
    userMove(battleField)
}

const val x = 'X'
const val zero = 'O'
const val empty = '_'
var impossible = false

fun fillTheField(): MutableList<CharArray> {
    return MutableList(3) { CharArray(3) { '_' } }
}

fun rowComplete(listOfRows: MutableList<CharArray>): Char {
    var counter = 0
    var sign = empty
    for (l in listOfRows) {
        if (counter > 1) {
            impossible = true
            break
        } else if (l.all { it == x }) {
            sign = x
            counter++
        } else if (l.all { it == zero }) {
            sign = zero
            counter++
        }
    }

    return sign
}

fun columnComplete(listOfRows: MutableList<CharArray>): Char {
    var counter = 0
    var sign = empty
    for (column in listOfRows.indices) {
        val columnSymbols = mutableListOf<Char>()
        for (row in listOfRows) {
            columnSymbols.add(row[column])
        }

        if (counter > 1) {
            impossible = true
            break
        } else if (columnSymbols.all { it == x }) {
            sign = x
            counter++
        } else if (columnSymbols.all { it == zero }) {
            sign = zero
            counter++
        }
    }
    return sign
}

fun leftDiagonalComplete(listOfRows: MutableList<CharArray>): Char {
    val firstSymbol = listOfRows[0][0]
    if (firstSymbol == empty) return empty

    for (i in 1 until listOfRows.size) {
        if (listOfRows[i][i] != firstSymbol) return empty
    }
    return firstSymbol
}

fun rightDiagonalComplete(listOfRows: MutableList<CharArray>): Char {

    val lastIndex = listOfRows.size - 1
    val lastSymbol = listOfRows[0][lastIndex]
    if (lastSymbol == empty) return empty

    for (i in 1 until listOfRows.size) {
        if (listOfRows[i][lastIndex - i] != lastSymbol) return empty
    }
    return lastSymbol
}

fun checkBalance(listOfRows: MutableList<CharArray>): Boolean {
    var xCount = 0
    var zeroCount = 0

    for (row in listOfRows) {
        for (ch in row) {
            when (ch) {
                x -> xCount++
                zero -> zeroCount++
            }
        }
    }
    return when {
        xCount - zeroCount in -1..1 -> true
        else -> {
            impossible = true
            false
        }
    }
}

fun checkWin(listOfRows: MutableList<CharArray>): String {

    val rowWinner = rowComplete(listOfRows)
    val columnWinner = columnComplete(listOfRows)
    val leftDiagonalWinner = leftDiagonalComplete(listOfRows)
    val rightDiagonalWinner = rightDiagonalComplete(listOfRows)

    return when {
        (!checkBalance(listOfRows) || impossible) -> "Impossible"
        rowWinner != empty -> "$rowWinner wins"
        columnWinner != empty -> "$columnWinner wins"
        leftDiagonalWinner != empty -> "$leftDiagonalWinner wins"
        rightDiagonalWinner != empty -> "$rightDiagonalWinner wins"
        listOfRows.any { row -> row.any { it == empty } } -> "Game not finished"
        else -> "Draw"
    }
}

fun printGame(listOfRows: MutableList<CharArray>) {
    println("---------")
    println("| ${listOfRows[0].joinToString(" ")} |")
    println("| ${listOfRows[1].joinToString(" ")} |")
    println("| ${listOfRows[2].joinToString(" ")} |")
    println("---------")
}

fun userMove(listOfRows: MutableList<CharArray>) {
    var sign = x

    while (true) {
        val userCoordinates = readln()

        try {
            val (first, second) = userCoordinates.split(" ").map { it.toInt() }

            if (first !in 1..listOfRows.size || second !in 1..listOfRows.size) {
                println("Coordinates should be from 1 to ${listOfRows.size}!")
            } else if (listOfRows[first - 1][second - 1] == empty) {
                listOfRows[first - 1][second - 1] = sign
                sign = if (sign == x) zero else x

                if (checkWin(listOfRows) == "Game not finished") {
                    printGame(listOfRows)
                } else {
                    printGame(listOfRows)
                    println(checkWin(listOfRows))
                    break
                }
            } else {
                println("This cell is occupied! Choose another one!")
            }

        } catch (e: NumberFormatException) {
            println(e.message)
        } catch (e: Exception) {
            println(e.message)
        }
    }
}