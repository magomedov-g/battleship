import kotlin.random.Random


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

val FLEET_PLAN = listOf(
    ShipType.BATTLESHIP, ShipType.CRUISER, ShipType.CRUISER,
    ShipType.DESTROYER, ShipType.DESTROYER, ShipType.DESTROYER,
    ShipType.BOAT, ShipType.BOAT, ShipType.BOAT, ShipType.BOAT
).map { it.size }

fun placePlayerShips(player: Player): Boolean {
    var index = 0
    while (index < FLEET_PLAN.size) {
        printField(player)
        val size = FLEET_PLAN[index]
        println("Корабль ${index + 1}/10, $size палуб. Введите строку и столбец:")
        val input = readlnOrNull()?.trim() ?: return false
        if (input.equals("quit", ignoreCase = true)) return false
        val parts = input.split(Regex("\\s+"))
        val row = parts.getOrNull(0)?.toIntOrNull()
        val col = parts.getOrNull(1)?.toIntOrNull()
        if (parts.size != 2 || row == null || col == null) {
            println("Нужны два целых числа через пробел")
            continue
        }
        println("Введите размер корабля ($size):")
        val sizeInput = readlnOrNull()?.trim() ?: return false
        if (sizeInput.equals("quit", ignoreCase = true)) return false
        if (sizeInput.toIntOrNull() != size) {
            println("По плану нужен корабль из $size палуб")
            continue
        }
        println("Направление h/v:")
        val direction = readlnOrNull()?.trim()?.lowercase() ?: return false
        if (direction == "quit") return false
        if (direction != "h" && direction != "v") {
            println("Нужно h или v")
            continue
        }
        if (player.placeShip(row, col, size, direction == "h")) index++
        else println("Корабль не помещается, пересекается или касается другого")
    }
    return true
}

fun placeTemplateShips(player: Player) {
    player.reset()
    val starts = listOf(0 to 0, 2 to 0, 4 to 0, 6 to 0, 8 to 0,
        0 to 6, 2 to 6, 4 to 6, 6 to 6, 8 to 6)
    for (index in FLEET_PLAN.indices) {
        val (row, col) = starts[index]
        check(player.placeShip(row, col, FLEET_PLAN[index], horizontal = true))
    }
}

fun placeEnemyShips(enemy: Player, random: Random = Random.Default) {
    repeat(100) {
        enemy.reset()
        var complete = true
        for (size in FLEET_PLAN) {
            var placed = false
            for (attempt in 1..1000) {
                if (enemy.placeShip(random.nextInt(FIELD_SIZE), random.nextInt(FIELD_SIZE), size, random.nextBoolean())) {
                    placed = true
                    break
                }
            }
            if (!placed) { complete = false; break }
        }
        if (complete) return
    }
    placeTemplateShips(enemy)
}

fun main(args: Array<String>) {
    val player = Player("Вы")
    val enemy = Player("Компьютер")
    printRules()
    if (!placePlayerShips(player)) {
        println("Вы вышли из игры")
        return
    }
    if ("--template" in args) placeTemplateShips(enemy) else placeEnemyShips(enemy)
    var playerTurn = true
    var turns = 0
    while (player.hasShips() && enemy.hasShips()) {
        printBothFields(player, enemy)
        if (playerTurn) {
            println("Ваш ход. Введите строку и столбец:")
            val input = readlnOrNull()?.trim()
            if (input == null || input.equals("quit", ignoreCase = true)) {
                println("Вы вышли из игры")
                println("Игра длилась $turns ходов")
                return
            }
            val parts = input.split(Regex("\\s+"))
            val row = parts.getOrNull(0)?.toIntOrNull()
            val col = parts.getOrNull(1)?.toIntOrNull()
            if (parts.size != 2 || row == null || col == null || !enemy.isInside(row, col)) {
                println("Введите два числа от 0 до 9. Ход сохраняется.")
                continue
            }
            val state = enemy.field()[row][col]
            if (state == CellState.HIT || state == CellState.MISS) {
                println("Вы уже стреляли в эту клетку. Ход сохраняется.")
                continue
            }
            val hit = enemy.shoot(row, col)
            player.registerShot(hit)
            turns++
            println(if (hit) "Попадание! Стреляйте ещё." else "Промах")
            if (!hit) playerTurn = false
        } else {
            val candidates = mutableListOf<Pair<Int, Int>>()
            val field = player.field()
            for (row in 0 until FIELD_SIZE) for (col in 0 until FIELD_SIZE) {
                if (field[row][col] != CellState.HIT && field[row][col] != CellState.MISS) candidates.add(row to col)
            }
            val (row, col) = candidates.random()
            val hit = player.shoot(row, col)
            enemy.registerShot(hit)
            turns++
            println("Компьютер стреляет: $row $col — ${if (hit) "попадание" else "промах"}")
            if (!hit) playerTurn = true
        }
    }
    printBothFields(player, enemy, revealEnemy = true)
    println(if (enemy.hasShips()) "Победил компьютер" else "Вы победили!")
    println("Игра длилась $turns ходов")
    println("Вы потопили ${enemy.countSunkShips()} кораблей")
    println("Компьютер потопил ${player.countSunkShips()} кораблей")
    player.printStats()
    enemy.printStats()
}
