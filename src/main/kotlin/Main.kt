fun printBothFields(player: Player, enemy: Player) = printBothFields(player.charField(), enemy.charField())

fun main() {
    val player = Player("Вы")
    val enemy = Player("Компьютер")
    for (col in 2..5) player.placeShip(3, col)
    for (row in 5..7) enemy.placeShip(row, 7)
    enemy.markShot(5, 7, true)
    enemy.markShot(0, 0, false)
    printBothFields(player, enemy)
    println("\nСостояния клетки:")
    for (state in CellState.entries) println("${state.name}: ${state.toChar()}")
    println("\nТипы кораблей:")
    for (type in ShipType.entries) println("${type.name}: ${type.size} палуб, большой: ${type.isBig()}")
    println("\nСостояния игры:")
    for (state in GameState.entries) println("${state.name}: ${state.describe()}")
}
