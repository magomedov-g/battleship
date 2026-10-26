enum class CellState {
    EMPTY, SHIP, HIT, MISS;

    fun toChar(): Char = when (this) {
        EMPTY -> '.'
        SHIP -> '#'
        HIT -> 'X'
        MISS -> 'O'
    }
}
