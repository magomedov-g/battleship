fun isGameOver(player: Player, enemy: Player): Boolean = !player.hasShips() || !enemy.hasShips()

fun main(args: Array<String>) {
    do {
        printRules()
        val difficulty = readDifficulty()
        if (difficulty == null) { println("Вы вышли из игры"); return }
        val player = Player("Вы")
        val enemy = Player("Компьютер")
        if (!placePlayerShips(player)) { println("Вы вышли из игры"); return }
        if ("--template" in args) placeTemplateShips(enemy) else placeEnemyShips(enemy)
        val ai = EnemyAI(player, difficulty)
        var playerTurn = true
        var turns = 0
        while (!isGameOver(player, enemy)) {
            printBothFields(player, enemy)
            if (playerTurn) {
                val shot = readCoordinate("Ваш ход. Введите строку и столбец:")
                if (shot == null) {
                    println("Вы вышли из игры")
                    println("Игра длилась $turns ходов")
                    ai.reset()
                    return
                }
                val (row, col) = shot
                if (!enemy.canShootAt(row, col)) {
                    println("Вы уже стреляли в эту клетку. Ход сохраняется.")
                    continue
                }
                val hit = enemy.shoot(row, col)
                player.registerShot(hit)
                turns++
                println(if (hit) "Попадание! Стреляйте ещё." else "Промах")
                if (!hit) playerTurn = false
            } else {
                val (row, col) = ai.chooseShot()
                val hit = player.shoot(row, col)
                enemy.registerShot(hit)
                ai.onShotResult(row, col, hit)
                turns++
                println("Компьютер стреляет: $row $col — ${if (hit) "попадание" else "промах"}")
                if (!hit) playerTurn = true
            }
        }
        printBothFields(player, enemy, revealEnemy = true)
        println(if (enemy.hasShips()) "Победил компьютер" else "Вы победили!")
        println("Игра длилась $turns ходов")
        println("Вы потопили ${enemy.countSunkShips()} кораблей")
        val sunkByComputer = if (difficulty == Difficulty.EASY) player.countSunkShips() else ai.countSunkShips()
        println("Компьютер потопил $sunkByComputer кораблей")
        player.printStats()
        enemy.printStats()
        ai.reset()
    } while (askPlayAgain())
}
