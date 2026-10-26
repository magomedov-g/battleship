class Player(val name: String) {
    private val _field: Array<CharArray> = createEmptyField()
    private var _shots = 0
    private var _hits = 0
    private var _shipsLeft = 20

    val shots: Int get() = _shots
    val hits: Int get() = _hits
    val shipsLeft: Int get() = _shipsLeft

    fun field(): Array<CharArray> = Array(_field.size) { _field[it].copyOf() }
    fun isAlive(): Boolean = _shipsLeft > 0
    fun accuracy(): Double = if (_shots == 0) 0.0 else _hits * 100.0 / _shots

    fun registerShot(hit: Boolean) {
        _shots++
        if (hit) _hits++
    }

    fun takeDamage(): Boolean {
        if (_shipsLeft > 0) _shipsLeft--
        return isAlive()
    }

    fun placeShip(row: Int, col: Int) {
        if (row in _field.indices && col in _field.indices) _field[row][col] = '#'
    }

    fun markShot(row: Int, col: Int, hit: Boolean) {
        if (row in _field.indices && col in _field.indices) _field[row][col] = if (hit) 'X' else 'O'
    }

    fun reset() {
        _shots = 0
        _hits = 0
        _shipsLeft = 20
        for (row in _field) row.fill('.')
    }

    fun status(): String {
        val percent = "%.1f".format(java.util.Locale.ROOT, accuracy())
        return "Игрок $name: палуб осталось $_shipsLeft, выстрелов $_shots, попаданий $_hits, точность $percent%"
    }

    fun printStats() = println(status())
    fun printField() = printFields(listOf(field()), listOf("Поле игрока $name"))
}
