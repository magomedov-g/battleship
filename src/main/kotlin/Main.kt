fun printBothFields(player: Player, enemy: Player) = printBothFields(player.field(), enemy.field())

fun main() {
    val player = Player("Вы")
    val enemy = Player("Компьютер")
    for (col in 2..5) player.placeShip(3, col)
    for (row in 5..7) enemy.placeShip(row, 7)
    player.markShot(3, 3, true)
    player.markShot(4, 4, false)
    printBothFields(player, enemy)
    player.registerShot(true)
    player.registerShot(false)
    player.registerShot(true)
    player.printStats()
    println("Выстрелов: ${player.shots}, попаданий: ${player.hits}")
    player.takeDamage()
    println("Жив: ${player.isAlive()}")
    player.reset()
    println("После сброса: ${player.status()}")

}
