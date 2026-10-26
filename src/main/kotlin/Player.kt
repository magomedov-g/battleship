class Player {
    var name: String = ""
    var field: Array<CharArray> = createEmptyField()
    var shots: Int = 0
    var hits: Int = 0
    var shipsLeft: Int = 20

    fun isAlive(): Boolean = shipsLeft > 0
    fun accuracy(): Double = if (shots == 0) 0.0 else hits * 100.0 / shots

    fun status(): String {
        val percent = "%.1f".format(java.util.Locale.ROOT, accuracy())
        return "Игрок $name: палуб осталось $shipsLeft, выстрелов $shots, попаданий $hits, точность $percent%"
    }

    fun printField() = printFields(listOf(field), listOf("Поле игрока $name"))
    fun printStats() = println(status())

    fun takeDamage(): Boolean {
        if (shipsLeft > 0) shipsLeft--
        return isAlive()
    }

    fun registerShot(hit: Boolean) {
        shots++
        if (hit) hits++
    }

    fun reset() {
        shots = 0
        hits = 0
        shipsLeft = 20
        for (row in field) row.fill('.')
    }
}
