fun readCoordinate(prompt: String): Pair<Int, Int>? {
    while (true) {
        println(prompt)
        val input = readlnOrNull()?.trim() ?: return null
        if (input.equals("quit", ignoreCase = true)) return null
        val parts = input.split(Regex("\\s+"))
        val row = parts.getOrNull(0)?.toIntOrNull()
        val col = parts.getOrNull(1)?.toIntOrNull()
        if (parts.size == 2 && row != null && col != null &&
            row in 0 until FIELD_SIZE && col in 0 until FIELD_SIZE) return row to col
        println("Введите два числа от 0 до 9. Ход сохраняется.")
    }
}

fun readDifficulty(): Difficulty? {
    while (true) {
        println("Сложность EASY / MEDIUM / HARD (Enter — HARD):")
        val input = readlnOrNull()?.trim() ?: return null
        if (input.equals("quit", ignoreCase = true)) return null
        if (input.isEmpty()) return Difficulty.HARD
        val result = Difficulty.entries.firstOrNull { it.name.equals(input, ignoreCase = true) }
        if (result != null) return result
        println("Неизвестный уровень сложности")
    }
}

fun askPlayAgain(): Boolean {
    while (true) {
        println("Сыграть ещё? y/n:")
        when (readlnOrNull()?.trim()?.lowercase() ?: return false) {
            "y", "yes", "да" -> return true
            "n", "no", "нет", "quit", "" -> return false
            else -> println("Введите y или n")
        }
    }
}
