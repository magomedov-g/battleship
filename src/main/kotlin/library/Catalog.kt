package library

class Catalog {
    private val books = arrayOf(
        Book("Мастер и Маргарита", "М. Булгаков", 1967, Genre.FICTION),
        Book("Преступление и наказание", "Ф. Достоевский", 1866, Genre.FICTION, BookStatus.BORROWED),
        Book("Краткая история времени", "С. Хокинг", 1988, Genre.SCIENTIFIC),
        Book("Хоббит", "Дж. Р. Р. Толкин", 1937, Genre.FANTASY),
        Book("Убийство в Восточном экспрессе", "А. Кристи", 1934, Genre.DETECTIVE, BookStatus.RESERVED),
        Book("Стив Джобс", "У. Айзексон", 2011, Genre.BIOGRAPHY, BookStatus.BORROWED),
        Book("Маленький принц", "А. де Сент-Экзюпери", 1943, Genre.CHILDREN),
        Book("Война и мир", "Л. Толстой", 1869, Genre.FICTION, BookStatus.ARCHIVED),
        Book("451 градус по Фаренгейту", "Р. Брэдбери", 1953, Genre.FANTASY, BookStatus.BORROWED),
        Book("Приключения Шерлока Холмса", "А. Конан Дойл", 1892, Genre.DETECTIVE),
        Book("Двойная спираль", "Дж. Уотсон", 1968, Genre.SCIENTIFIC, BookStatus.LOST),
        Book("Приключения Тома Сойера", "М. Твен", 1876, Genre.CHILDREN, BookStatus.RESERVED),
        Book("Дюна", "Ф. Герберт", 1965, Genre.FANTASY),
        Book("Моя жизнь", "М. Шагал", 1931, Genre.BIOGRAPHY, BookStatus.ARCHIVED),
        Book("Человек, который принял жену за шляпу", "О. Сакс", 1985, Genre.SCIENTIFIC, BookStatus.BORROWED)
    )

    val size: Int get() = books.size
    fun allBooks(): List<Book> = books.toList()
    fun bookAt(number: Int): Book? = books.getOrNull(number - 1)
    fun availableBooks(): List<Book> = books.filter { it.status == BookStatus.AVAILABLE }
    fun filterByGenre(genre: Genre): List<Book> = books.filter { it.genre == genre }
    fun publishedAfter(year: Int): List<Book> = books.filter { it.year > year }
    fun oldest(): Book = books.minBy { it.year }
    fun newest(): Book = books.maxBy { it.year }
    fun averageYear(): Double = books.map { it.year }.average()

    fun statusCounts(): Map<BookStatus, Int> {
        var available = 0; var borrowed = 0; var lost = 0; var reserved = 0; var archived = 0
        for (book in books) {
            when (book.status) {
                BookStatus.AVAILABLE -> available++
                BookStatus.BORROWED -> borrowed++
                BookStatus.LOST -> lost++
                BookStatus.RESERVED -> reserved++
                BookStatus.ARCHIVED -> archived++
            }
        }
        return linkedMapOf(BookStatus.AVAILABLE to available, BookStatus.BORROWED to borrowed,
            BookStatus.LOST to lost, BookStatus.RESERVED to reserved, BookStatus.ARCHIVED to archived)
    }

    fun genreCounts(): Map<Genre, Int> {
        var fiction = 0; var scientific = 0; var fantasy = 0
        var detective = 0; var biography = 0; var children = 0
        for (book in books) {
            when (book.genre) {
                Genre.FICTION -> fiction++
                Genre.SCIENTIFIC -> scientific++
                Genre.FANTASY -> fantasy++
                Genre.DETECTIVE -> detective++
                Genre.BIOGRAPHY -> biography++
                Genre.CHILDREN -> children++
            }
        }
        return linkedMapOf(Genre.FICTION to fiction, Genre.SCIENTIFIC to scientific,
            Genre.FANTASY to fantasy, Genre.DETECTIVE to detective,
            Genre.BIOGRAPHY to biography, Genre.CHILDREN to children)
    }

    fun printTable(selection: List<Book> = allBooks()) {
        println("№   Название                                      Автор                     Год   Жанр         Статус")
        for (book in selection) {
            val number = books.indexOf(book) + 1
            println("${number.toString().padEnd(4)}${book.title.padEnd(46)}${book.author.padEnd(26)}" +
                "${book.year.toString().padEnd(6)}${book.genre.name.padEnd(13)}${book.status.displayName}")
        }
        if (selection.isEmpty()) println("Книги не найдены")
    }

    fun printReport() {
        println("=== ОТЧЁТ ПО КАТАЛОГУ ===")
        println("Всего книг: $size")
        println("По статусам:")
        for ((status, count) in statusCounts()) {
            val percent = "%.0f".format(java.util.Locale.ROOT, count * 100.0 / size)
            println("${status.name}: $count ($percent%)")
        }
        println("По жанрам:")
        for ((genre, count) in genreCounts()) {
            val percent = "%.0f".format(java.util.Locale.ROOT, count * 100.0 / size)
            println("${genre.name}: $count ($percent%)")
        }
        println("Самая старая книга: ${oldest().title} (${oldest().year})")
        println("Самая новая книга: ${newest().title} (${newest().year})")
        println("Средний год: ${"%.1f".format(java.util.Locale.ROOT, averageYear())}")
    }
}
