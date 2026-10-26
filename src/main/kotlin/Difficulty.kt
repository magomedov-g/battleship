enum class Difficulty {
    EASY, MEDIUM, HARD;

    fun describe(): String = when (this) {
        EASY -> "Случайные выстрелы"
        MEDIUM -> "Поиск и добивание без отдельной памяти направлений"
        HARD -> "Поиск и добивание с памятью проверенных направлений"
    }
}
