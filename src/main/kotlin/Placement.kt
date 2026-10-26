import kotlin.random.Random

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
        val (row, col) = readCoordinate("Корабль ${index + 1}/10, $size палуб. Введите строку и столбец:") ?: return false
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

fun placeEnemyShips(enemy: Player, random: Random = Random.Default, maxResets: Int = 100) {
    require(maxResets >= 0)
    val cells = mutableListOf<Pair<Int, Int>>()
    for (row in 0 until FIELD_SIZE) for (col in 0 until FIELD_SIZE) cells.add(row to col)
    repeat(maxResets) {
        enemy.reset()
        var complete = true
        for (size in FLEET_PLAN.shuffled(random)) {
            cells.shuffle(random)
            var placed = false
            for ((row, col) in cells) {
                val firstHorizontal = random.nextBoolean()
                if (enemy.placeShip(row, col, size, firstHorizontal) ||
                    enemy.placeShip(row, col, size, !firstHorizontal)) {
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
