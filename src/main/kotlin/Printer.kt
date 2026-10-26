private fun fieldLines(player: Player, title: String, hideShips: Boolean): List<String> {
    val field = player.field()
    val lines = mutableListOf("=== $title ===", "   " + (0 until FIELD_SIZE).joinToString(" "))
    for (row in 0 until FIELD_SIZE) {
        var line = "$row  "
        for (col in 0 until FIELD_SIZE) {
            val state = field[row][col]
            val shown = if (hideShips && state == CellState.SHIP) CellState.EMPTY else state
            line += "${shown.toChar()} "
        }
        lines.add(line.trimEnd())
    }
    return lines
}

fun printField(player: Player, hideShips: Boolean = false) {
    for (line in fieldLines(player, "Поле игрока ${player.name}", hideShips)) println(line)
}

fun printBothFields(player: Player, enemy: Player, revealEnemy: Boolean = false) {
    val left = fieldLines(player, "Ваше поле", false)
    val right = fieldLines(enemy, "Поле противника", !revealEnemy)
    val width = left.maxOf { it.length } + 6
    for (row in left.indices) println(left[row].padEnd(width) + right[row])
}

fun printRules() {
    println("=== МОРСКОЙ БОЙ ===")
    println("Поле 10×10; координаты от 0 до 9. Флот: 4, 3, 3, 2, 2, 2, 1, 1, 1, 1 палуб.")
    println("Корабли не пересекаются и не касаются даже углами.")
    println("Для расстановки введите строку и столбец, затем размер и направление h (вправо) / v (вниз).")
    println("При попадании ход сохраняется, при промахе переходит сопернику.")
    println(". — пусто; # — корабль; X — попадание; O — промах. Корабли врага скрыты.")
    println("quit или конец ввода — выход из игры.")
}
