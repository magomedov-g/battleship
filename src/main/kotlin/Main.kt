fun main() {
    val playerField = createEmptyField()
    val enemyField = createEmptyField()
    for (col in 2..5) playerField[3][col] = '#'
    for (row in 5..7) enemyField[row][7] = '#'
    enemyField[5][5] = 'X'
    enemyField[5][6] = 'O'

    printField(playerField, "Ваше поле")
    printField(enemyField, "Поле противника", showShips = false)
    println()
    printBothFields(playerField, enemyField)

    println("\nПоле другого размера:")
    printField(createEmptyField(size = 8), "Поле 8×8")
    printField(createEmptyField(12), "Поле 12×12")
    val smallField = createEmptyField(3)
    smallField[1][1] = '#'
    printField(smallField, "Отладка", debug = true)
    printFields(listOf(smallField, createEmptyField(3)), listOf("Первое", "Второе"))
}
