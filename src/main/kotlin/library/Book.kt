package library

class Book(val title: String, val author: String, val year: Int,
           val genre: Genre, initialStatus: BookStatus = BookStatus.AVAILABLE) {
    var status: BookStatus = initialStatus
        private set

    fun borrow(): String = when (status) {
        BookStatus.AVAILABLE -> {
            status = BookStatus.BORROWED
            "Книга «$title» выдана"
        }
        BookStatus.BORROWED -> "Книга уже выдана другому читателю"
        BookStatus.LOST -> "Книга потеряна"
        BookStatus.RESERVED -> "Книга зарезервирована"
        BookStatus.ARCHIVED -> "Книга находится в архиве"
    }

    fun returnBook(): String {
        if (status != BookStatus.BORROWED) return "Возврат невозможен: ${status.displayName}"
        status = BookStatus.AVAILABLE
        return "Книга «$title» возвращена"
    }

    fun markLost(): String {
        status = BookStatus.LOST
        return "Книга «$title» отмечена как потерянная"
    }

    fun reserve(): String {
        if (status != BookStatus.AVAILABLE) return "Резервирование невозможно: ${status.displayName}"
        status = BookStatus.RESERVED
        return "Книга «$title» зарезервирована"
    }
}
