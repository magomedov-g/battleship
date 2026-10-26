fun createEmptyField(size: Int = 10): Array<CharArray> {
    require(size > 0) { "Размер поля должен быть положительным" }
    return Array(size) { CharArray(size) { '.' } }
}

fun renderCell(cell: Char, showShips: Boolean = true): Char =
    if (!showShips && cell == '#') '.' else cell

fun fieldLines(field: Array<CharArray>, title: String,
               showShips: Boolean = true, debug: Boolean = false): List<String> {
    require(field.isNotEmpty() && field.all { it.size == field.size }) {
        "Поле должно быть непустым и квадратным"
    }
    val width = (field.size - 1).toString().length
    val lines = mutableListOf("=== $title ===")
    val prefix = " ".repeat(width + 1)
    lines.add(prefix + field.indices.joinToString(" ") { it.toString().padStart(width) })
    for (row in field.indices) {
        val cells = field[row].indices.joinToString(" ") { col ->
            val cell = renderCell(field[row][col], showShips)
            if (debug) "($row,$col):$cell" else cell.toString().padStart(width)
        }
        lines.add(row.toString().padStart(width) + " " + cells)
    }
    return lines
}

fun printField(field: Array<CharArray>, title: String,
               showShips: Boolean = true, debug: Boolean = false) {
    fieldLines(field, title, showShips, debug).forEach { println(it) }
}

fun printBothFields(playerField: Array<CharArray>, enemyField: Array<CharArray>) {
    require(playerField.size == enemyField.size) { "Размеры полей должны совпадать" }
    val left = fieldLines(playerField, "Ваше поле")
    val right = fieldLines(enemyField, "Поле противника", showShips = false)
    val width = left.maxOf { it.length } + 6
    for (i in left.indices) println(left[i].padEnd(width) + right[i])
}

fun printFields(fields: List<Array<CharArray>>, titles: List<String>, showShips: Boolean = true) {
    require(fields.isNotEmpty() && fields.size == titles.size) { "Для каждого поля нужен заголовок" }
    require(fields.all { it.size == fields.first().size }) { "Размеры полей должны совпадать" }
    val boards = fields.indices.map { fieldLines(fields[it], titles[it], showShips) }
    val widths = boards.map { lines -> lines.maxOf { it.length } + 6 }
    for (row in boards.first().indices) {
        println(boards.indices.joinToString("") { board -> boards[board][row].padEnd(widths[board]) }.trimEnd())
    }
}
