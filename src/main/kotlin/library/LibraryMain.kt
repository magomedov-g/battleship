package library

fun main() {
    val catalog = Catalog()
    catalog.printTable()
    while (true) {
        println("\n1 — каталог; 2 — отчёт; 3 — доступные книги; 4 — фильтр по жанру; 5 — фильтр по году")
        println("6 — выдать; 7 — вернуть; 8 — отметить потерю; 9 — зарезервировать; 0 — выход")
        val command = readlnOrNull()?.trim() ?: return
        when (command) {
            "0", "quit" -> return
            "1" -> catalog.printTable()
            "2" -> catalog.printReport()
            "3" -> catalog.printTable(catalog.availableBooks())
            "4" -> {
                println("Введите жанр: ${Genre.entries.joinToString { it.name }}")
                val input = readlnOrNull()?.trim() ?: return
                val genre = Genre.entries.firstOrNull { it.name.equals(input, ignoreCase = true) }
                if (genre == null) println("Неизвестный жанр") else catalog.printTable(catalog.filterByGenre(genre))
            }
            "5" -> {
                println("После какого года?")
                val input = readlnOrNull() ?: return
                val year = input.trim().toIntOrNull()
                if (year == null) println("Введите целое число") else catalog.printTable(catalog.publishedAfter(year))
            }
            "6", "7", "8", "9" -> {
                println("Введите номер книги (1–${catalog.size}):")
                val input = readlnOrNull() ?: return
                val number = input.trim().toIntOrNull()
                val book = if (number == null) null else catalog.bookAt(number)
                if (book == null) {
                    println("Книги с таким номером нет")
                    continue
                }
                val message = when (command) {
                    "6" -> book.borrow()
                    "7" -> book.returnBook()
                    "8" -> book.markLost()
                    else -> book.reserve()
                }
                println(message)
            }
            else -> println("Неизвестная команда")
        }
    }
}
