enum class GameState {
    PLACING, PLAYER_TURN, ENEMY_TURN, FINISHED;

    fun describe(): String = when (this) {
        PLACING -> "Расстановка кораблей"
        PLAYER_TURN -> "Ход игрока"
        ENEMY_TURN -> "Ход компьютера"
        FINISHED -> "Игра завершена"
    }
}
