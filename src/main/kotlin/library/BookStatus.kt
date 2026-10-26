package library

enum class BookStatus(val displayName: String) {
    AVAILABLE("Доступна"), BORROWED("Выдана"), LOST("Потеряна"),
    RESERVED("Зарезервирована"), ARCHIVED("В архиве")
}
