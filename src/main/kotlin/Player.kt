const val FIELD_SIZE = 10

class Player(val name: String) {
    private val _field = Array(FIELD_SIZE) { Array(FIELD_SIZE) { CellState.EMPTY } }
    private var _shots = 0
    private var _hits = 0

    val shots: Int get() = _shots
    val hits: Int get() = _hits
    val shipsLeft: Int get() = _field.sumOf { row -> row.count { it == CellState.SHIP } }

    fun field(): Array<Array<CellState>> = Array(FIELD_SIZE) { _field[it].copyOf() }
    fun isInside(row: Int, col: Int): Boolean = row in 0 until FIELD_SIZE && col in 0 until FIELD_SIZE
    fun hasShips(): Boolean = shipsLeft > 0
    fun isAlive(): Boolean = hasShips()
    fun accuracy(): Double = if (_shots == 0) 0.0 else _hits * 100.0 / _shots

    fun registerShot(hit: Boolean) {
        _shots++
        if (hit) _hits++
    }

    fun canPlaceShip(startRow: Int, startCol: Int, size: Int, horizontal: Boolean): Boolean {
        if (size !in 1..4 || !isInside(startRow, startCol)) return false
        for (i in 0 until size) {
            val row = startRow + if (horizontal) 0 else i
            val col = startCol + if (horizontal) i else 0
            if (!isInside(row, col) || _field[row][col] != CellState.EMPTY) return false
            for (dr in -1..1) {
                for (dc in -1..1) {
                    val nr = row + dr
                    val nc = col + dc
                    if (isInside(nr, nc) && (_field[nr][nc] == CellState.SHIP || _field[nr][nc] == CellState.HIT)) {
                        return false
                    }
                }
            }
        }
        return true
    }

    fun placeShip(startRow: Int, startCol: Int, size: Int, horizontal: Boolean): Boolean {
        if (!canPlaceShip(startRow, startCol, size, horizontal)) return false
        for (i in 0 until size) {
            val row = startRow + if (horizontal) 0 else i
            val col = startCol + if (horizontal) i else 0
            _field[row][col] = CellState.SHIP
        }
        return true
    }

    fun shoot(row: Int, col: Int): Boolean {
        if (!isInside(row, col)) return false
        val current = _field[row][col]
        if (current == CellState.HIT || current == CellState.MISS) return false
        val hit = current == CellState.SHIP
        _field[row][col] = if (hit) CellState.HIT else CellState.MISS
        return hit
    }

    private fun isDeck(row: Int, col: Int): Boolean {
        if (!isInside(row, col)) return false
        return _field[row][col] == CellState.SHIP || _field[row][col] == CellState.HIT
    }

    private fun shipCounts(): Pair<Int, Int> {
        var alive = 0
        var sunk = 0
        for (row in 0 until FIELD_SIZE) {
            for (col in 0 until FIELD_SIZE) {
                if (!isDeck(row, col) || isDeck(row - 1, col) || isDeck(row, col - 1)) continue
                val horizontal = isDeck(row, col + 1)
                var r = row
                var c = col
                var hasIntactDeck = false
                while (isDeck(r, c)) {
                    if (_field[r][c] == CellState.SHIP) hasIntactDeck = true
                    if (horizontal) c++ else r++
                }
                if (hasIntactDeck) alive++ else sunk++
            }
        }
        return alive to sunk
    }

    fun countShips(): Int = shipCounts().first
    fun countSunkShips(): Int = shipCounts().second

    fun reset() {
        _shots = 0
        _hits = 0
        for (row in _field) row.fill(CellState.EMPTY)
    }

    fun status(): String {
        val percent = "%.1f".format(java.util.Locale.ROOT, accuracy())
        return "Игрок $name: кораблей ${countShips()}, палуб $shipsLeft, выстрелов $_shots, попаданий $_hits, точность $percent%"
    }

    fun printStats() = println(status())
}
