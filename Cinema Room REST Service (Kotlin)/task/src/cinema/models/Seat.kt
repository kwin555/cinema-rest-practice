package cinema.models

import java.util.UUID.randomUUID

data class Seat (
    var row: Int,
    var column: Int,
    var price: Int,
    var isAvailable: Boolean = true,
    var token: String,
)