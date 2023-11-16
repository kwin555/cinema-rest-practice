package cinema.controllers

import cinema.dtos.CinemaDto
import cinema.models.Seat
import cinema.models.Stats
import cinema.requestbodies.Ticket
import cinema.responseBody.SeatResponse
import cinema.responseBody.TicketResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class SeatsController {

    object Seats {
        val currentSeats = createSeats(9, 9)
        // build private fun that creates a 1d list of 9x9 seats, 81 seats total
        private fun createSeats(columns: Int, rows: Int): MutableList<MutableList<Seat>> {
            val seats = mutableListOf<MutableList<Seat>>()
            for (i in 1..rows) {
                // add rows
                seats.add(mutableListOf())
                for (j in 1..columns) {
                    // adding columns to each row
                    if (j <= 4) {
                        seats[i - 1].add(Seat(i, j, 10, true, UUID.randomUUID().toString()))
                    } else {
                        seats[i - 1].add(Seat(i, j, 8, true, UUID.randomUUID().toString()))
                    }

                }
            }
            println(seats)
            return seats
        }

        fun getRevenue(): Int {
            var rev = 0
            for (seat in currentSeats.flatten()) {
                if (!seat.isAvailable) {
                    rev += seat.price
                }
            }
            return rev
        }
        fun getPurchaseSeasts(): Int {
            return currentSeats.flatten().count{ !it.isAvailable }
        }
        fun getAvailableSeats(): Int {
            return currentSeats.flatten().count { it.isAvailable  }
        }
    }

    @GetMapping("/stats")
    fun getStats(@RequestParam password: String?): ResponseEntity<Any> {
        if (password == "super_secret") {
            // Calculate the statistics and return the JSON response
            val currentIncome = Seats.getRevenue()
            val numberOfAvailableSeats = Seats.getAvailableSeats()
            val numberOfPurchasedTickets = Seats.getPurchaseSeasts()

            val statistics = mapOf(
                "current_income" to currentIncome,
                "number_of_available_seats" to numberOfAvailableSeats,
                "number_of_purchased_tickets" to numberOfPurchasedTickets
            )

            return ResponseEntity.ok(statistics)
        } else {
            // Return a 401 Unauthorized response with an error message
            val errorResponse = mapOf("error" to "The password is wrong!")
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse)
        }
    }


    @GetMapping("/seats")
    fun getSeats(): ResponseEntity<CinemaDto?>? {
        return ResponseEntity.ok(CinemaDto(9, 9, Seats.currentSeats.flatten().filter { it.isAvailable }.map { it -> SeatResponse(it.column, it.row, it.price) }))
    }

    @PostMapping("/purchase")
    fun buyTicket(@RequestBody ticket: Ticket): ResponseEntity<Any> {
        if (ticket.row < 1 || ticket.row > 9 || ticket.column < 1 || ticket.column > 9) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(mapOf("error" to "The number of a row or a column is out of bounds!"))
        }
        try {
            val seat = Seats.currentSeats.getOrNull(ticket.column - 1)?.getOrNull(ticket.row - 1)
            if (seat != null && seat.isAvailable) {
                // Mark the ticket as unavailable
                markTicketAsUnavailable(seat)

                // Generate a token
                // Create a response in the required format
                val ticketResponse = TicketResponse(ticket.column, ticket.row, seat.price)
                val response = mapOf("token" to seat.token, "ticket" to ticketResponse)

                return ResponseEntity.ok(response)
            } else {
                // Ticket is not available or not found
                return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(mapOf("error" to "The ticket has been already purchased!"))
            }
        } catch (e: Exception) {
            // Handle exceptions and error responses here
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    @PostMapping("/return")
    fun returnTicket(@RequestBody requestBody: Map<String, String>): ResponseEntity<Any> {

        try {
            val token = requestBody["token"]
                ?: return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to "Token is missing!"))
            println(token)
            // Find the ticket associated with the token (assuming you have a mechanism for tracking tickets)
            val returnedTicket = findTicketByToken(token)
            println(returnedTicket)
            if (returnedTicket != null) {
                if (returnedTicket.isAvailable) {
                    return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(mapOf("error" to "Wrong token!"))
                }
                // Mark the ticket as available (you need to implement this logic)
                markTicketAsAvailable(returnedTicket)

                // Return the response in the required format
                val ticketResponse = TicketResponse(column = returnedTicket.row, row = returnedTicket.column, price = returnedTicket.price)
                val response = mapOf("returned_ticket" to ticketResponse)

                // Respond with a 200 status code
                return ResponseEntity.ok(response)
            } else {
                // Ticket not found for the given token, respond with a 400 status code
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to "Wrong token!"))
            }
        } catch (e: Exception) {
            // Handle exceptions and error responses here
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }



    // Function to find a ticket by its token
    fun findTicketByToken(token: String): Seat? {
        return Seats.currentSeats.flatten().find { it.token == token }
    }

    // calculate purchased tickets
    fun countPurchasedTickets(): Int {
        return Seats.currentSeats.flatten().count { !it.isAvailable  }
    }

    // Function to mark a ticket as available
    fun markTicketAsAvailable(seat: Seat) {
        // You can set the isAvailable flag to true to mark it as available
        seat.isAvailable = true
    }

    // Function to mark a ticket as unavailable (when purchased)
    fun markTicketAsUnavailable(seat: Seat) {
        // You can set the isAvailable flag to false and store the token to associate it with the purchased ticket
        seat.isAvailable = false
    }

}