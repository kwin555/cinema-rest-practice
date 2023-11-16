package cinema.dtos

import cinema.models.Seat
import cinema.responseBody.SeatResponse

data class  CinemaDto (val total_rows: Int, val total_columns: Int, val available_seats: List<SeatResponse>)