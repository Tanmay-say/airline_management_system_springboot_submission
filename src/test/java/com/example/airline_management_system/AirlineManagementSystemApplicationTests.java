package com.example.airline_management_system;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class AirlineManagementSystemTests {

	@Autowired
	private MockMvc mockMvc;

	// ✅ 1. Get all flights (Success)
	@Test
	void testGetAllFlights() throws Exception {
		mockMvc.perform(get("/flights"))
				.andExpect(status().isOk());
	}

	// ✅ 2. Get a specific flight by ID (Success)
	@Test
	void testGetFlightById() throws Exception {
		mockMvc.perform(get("/flights/1"))  // Assuming ID 1 exists
				.andExpect(status().isOk());
	}

	// ✅ 3. Get schedule for an existing flight (Success)
	@Test
	void testGetSchedule() throws Exception {
		mockMvc.perform(get("/flights/1/schedules")
						.param("dates", "2024-12-01"))  // Example date
				.andExpect(status().isOk());
	}

	// ✅ 4. Book a ticket (Success)
	@Test
	void testBookTicket() throws Exception {
		String ticketJson = """
        {
            "passengerName": "John Doe",
            "flightId": 1  // Assuming flight 1 exists
        }
        """;

		mockMvc.perform(post("/tickets")
						.contentType(MediaType.APPLICATION_JSON)
						.content(ticketJson))
				.andExpect(status().isOk());
	}

	// ✅ 5. Get a specific ticket by ID (Success)
	@Test
	void testGetTicket() throws Exception {
		mockMvc.perform(get("/tickets/1"))  // Assuming ticket ID 1 exists
				.andExpect(status().isOk());
	}

	// ✅ 6. Cancel a ticket (Success)
	@Test
	void testCancelTicket() throws Exception {
		mockMvc.perform(delete("/tickets/1"))  // Assuming ticket ID 1 exists
				.andExpect(status().isOk());
	}

	// ✅ 7. Get all flights with sorting
	@Test
	void testGetAllFlightsSorted() throws Exception {
		mockMvc.perform(get("/flights?sort=desc"))
				.andExpect(status().isOk());
	}

	// ✅ 8. Get schedule without date parameter (should fail gracefully)
	@Test
	void testGetScheduleWithoutDate() throws Exception {
		mockMvc.perform(get("/flights/1/schedules")) // No date param
				.andExpect(status().isBadRequest());
	}

	// ✅ 9. Book a ticket and verify the response structure
	@Test
	void testBookTicketResponse() throws Exception {
		String ticketJson = """
        {
            "passengerName": "Alice Smith",
            "flightId": 1
        }
        """;

		mockMvc.perform(post("/tickets")
						.contentType(MediaType.APPLICATION_JSON)
						.content(ticketJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.passengerName").value("Alice Smith"))
				.andExpect(jsonPath("$.flightId").value(1));
	}

	// ❌ 10. Get a non-existent flight (404)
	@Test
	void testGetFlightById_NotFound() throws Exception {
		mockMvc.perform(get("/flights/9999"))  // Assuming ID 9999 does not exist
				.andExpect(status().isNotFound());
	}

	// ❌ 11. Get schedule for a non-existent flight (404)
	@Test
	void testGetSchedule_NotFound() throws Exception {
		mockMvc.perform(get("/flights/9999/schedules")
						.param("dates", "2024-12-01"))  // Example date
				.andExpect(status().isNotFound());
	}

	// ❌ 12. Get a non-existent ticket (404)
	@Test
	void testGetTicket_NotFound() throws Exception {
		mockMvc.perform(get("/tickets/9999"))  // Assuming ID 9999 does not exist
				.andExpect(status().isNotFound());
	}

	// ❌ 13. Cancel a non-existent ticket (404)
	@Test
	void testCancelTicket_NotFound() throws Exception {
		mockMvc.perform(delete("/tickets/9999"))  // Assuming ID 9999 does not exist
				.andExpect(status().isNotFound());
	}

	// ❌ 14. Book a ticket with a non-existent flight (404)
	@Test
	void testBookTicket_FlightNotFound() throws Exception {
		String ticketJson = """
        {
            "passengerName": "John Doe",
            "flightId": 9999  // Assuming flight 9999 does not exist
        }
        """;

		mockMvc.perform(post("/tickets")
						.contentType(MediaType.APPLICATION_JSON)
						.content(ticketJson))
				.andExpect(status().isNotFound());
	}

	// ❌ 15. Access an invalid endpoint (404)
	@Test
	void testInvalidEndpoint() throws Exception {
		mockMvc.perform(get("/invalidEndpoint"))
				.andExpect(status().isNotFound());
	}

	// ❌ 16. Get a flight with an invalid ID format (400 Bad Request)
	@Test
	void testGetFlightInvalidId() throws Exception {
		mockMvc.perform(get("/flights/abc"))  // Passing string instead of a number
				.andExpect(status().isBadRequest());
	}

	// ❌ 17. Book a ticket without required fields (400 Bad Request)
	@Test
	void testBookTicketMissingFields() throws Exception {
		String ticketJson = """
        {
            "passengerName": "John Doe"
            // Missing flightId
        }
        """;

		mockMvc.perform(post("/tickets")
						.contentType(MediaType.APPLICATION_JSON)
						.content(ticketJson))
				.andExpect(status().isBadRequest());
	}

	// ❌ 18. Cancel a ticket that was already canceled (Assume 410 Gone)
	@Test
	void testCancelAlreadyCanceledTicket() throws Exception {
		mockMvc.perform(delete("/tickets/2"))  // Assume ticket 2 was already canceled
				.andExpect(status().isGone());
	}
}
