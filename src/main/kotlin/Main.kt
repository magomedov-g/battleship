fun printBothFields(player: Player, enemy: Player) = printBothFields(player.field, enemy.field)

fun main() {
    val player = Player()
    player.name = "Вы"
    val enemy = Player()
    enemy.name = "Компьютер"
    for (col in 2..5) player.field[3][col] = '#'
    for (row in 5..7) enemy.field[row][7] = '#'
    player.printField()
    printBothFields(player, enemy)

    player.registerShot(true)
    player.registerShot(false)
    player.registerShot(true)
    enemy.registerShot(false)
    player.printStats()
    enemy.printStats()
    println("Живы: ${player.isAlive()}, ${enemy.isAlive()}")
    player.takeDamage()
    player.takeDamage()
    println("Палуб после двух попаданий: ${player.shipsLeft}")
    println("Жив: ${player.isAlive()}")
    repeat(18) { player.takeDamage() }
    println("После потери всех палуб: ${player.isAlive()}")
    player.reset()
    println("После сброса:")
    player.printStats()
    player.printField()
}
