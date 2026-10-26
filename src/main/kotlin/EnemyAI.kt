import kotlin.random.Random

enum class Direction(val dr: Int, val dc: Int) {
    LEFT(0, -1), RIGHT(0, 1), UP(-1, 0), DOWN(1, 0)
}

class EnemyAI(private val target: Player, private val difficulty: Difficulty = Difficulty.HARD,
              private val random: Random = Random.Default) {
    private val hits = mutableListOf<Pair<Int, Int>>()
    private var hunting = false
    private val triedDirections = mutableSetOf<Direction>()
    private var lastDirection: Direction? = null
    private var completedHunts = 0

    fun chooseShot(): Pair<Int, Int> {
        lastDirection = null
        if (difficulty != Difficulty.EASY && hunting) {
            val shot = chooseHuntShot()
            if (shot != null) return shot
            resetHunt()
        }
        return chooseSearchShot()
    }

    private fun chooseSearchShot(): Pair<Int, Int> {
        val candidates = mutableListOf<Pair<Int, Int>>()
        for (row in 0 until FIELD_SIZE) for (col in 0 until FIELD_SIZE) {
            if (target.canShootAt(row, col)) candidates.add(row to col)
        }
        check(candidates.isNotEmpty()) { "На поле нет клеток для выстрела" }
        return candidates.random(random)
    }

    private fun chooseHuntShot(): Pair<Int, Int>? {
        if (hits.isEmpty()) return null
        if (hits.size == 1) {
            val (row, col) = hits.first()
            val directions = if (difficulty == Difficulty.MEDIUM) Direction.entries.shuffled(random) else Direction.entries
            for (direction in directions) {
                val nr = row + direction.dr
                val nc = col + direction.dc
                if (difficulty == Difficulty.HARD && direction in triedDirections) continue
                if (target.canShootAt(nr, nc)) {
                    lastDirection = direction
                    return nr to nc
                }
            }
            return null
        }

        val horizontal = hits[0].first == hits[1].first
        val candidates = mutableListOf<Pair<Direction, Pair<Int, Int>>>()
        if (horizontal) {
            val row = hits.first().first
            val minCol = hits.minOf { it.second }
            val maxCol = hits.maxOf { it.second }
            candidates.add(Direction.RIGHT to (row to maxCol + 1))
            candidates.add(Direction.LEFT to (row to minCol - 1))
        } else {
            val col = hits.first().second
            val minRow = hits.minOf { it.first }
            val maxRow = hits.maxOf { it.first }
            candidates.add(Direction.DOWN to (maxRow + 1 to col))
            candidates.add(Direction.UP to (minRow - 1 to col))
        }
        if (difficulty == Difficulty.MEDIUM) candidates.shuffle(random)
        for ((direction, coordinates) in candidates) {
            if (difficulty == Difficulty.HARD && direction in triedDirections) continue
            if (target.canShootAt(coordinates.first, coordinates.second)) {
                lastDirection = direction
                return coordinates
            }
        }
        return null
    }

    fun onShotResult(row: Int, col: Int, hit: Boolean) {
        if (difficulty == Difficulty.EASY) return
        if (hit) {
            if (!hunting) triedDirections.clear()
            if ((row to col) !in hits) hits.add(row to col)
            hunting = true
        } else if (hunting && difficulty == Difficulty.HARD) {
            val direction = lastDirection
            if (direction != null) triedDirections.add(direction)
        }
        lastDirection = null
    }

    private fun resetHunt() {
        if (hits.isNotEmpty()) completedHunts++
        hits.clear()
        triedDirections.clear()
        hunting = false
        lastDirection = null
    }

    fun countSunkShips(): Int = completedHunts + if (hunting && !target.hasShips()) 1 else 0

    fun reset() {
        hits.clear()
        triedDirections.clear()
        hunting = false
        lastDirection = null
        completedHunts = 0
    }
}
