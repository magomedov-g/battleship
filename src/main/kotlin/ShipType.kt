enum class ShipType(val size: Int) {
    BATTLESHIP(4), CRUISER(3), DESTROYER(2), BOAT(1);

    fun isBig(): Boolean = size >= 3
}
