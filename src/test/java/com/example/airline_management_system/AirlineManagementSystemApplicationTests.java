package com.example.airline_management_system;

import com.example.airline_management_system.data.Ticket;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class AirlineManagementSystemTests {

	@Autowired
	private MockMvc mockMvc;

	// ✅ Test: Get all flights (expects 200 OK)
	@Test
	void testGetAllFlights() throws Exception {
		mockMvc.perform(get("/flights"))
				.andExpect(status().isOk());  // Always expects 200 OK
	}

	// ✅ Test: Get a specific flight by ID (expects 200 OK)
	@Test
	void testGetFlightById() throws Exception {
		mockMvc.perform(get("/flights/1"))  // Assuming ID 1 exists
				.andExpect(status().isOk());
	}

	// ✅ Test: Get flight schedules (expects 200 OK)
	@Test
	void testGetSchedule() throws Exception {
		mockMvc.perform(get("/flights/1/schedules")
						.param("dates", "2024-12-01"))  // Example date
				.andExpect(status().isOk());
	}

	// ✅ Test: Book a ticket (expects 201 Created)
	@Test
	void testBookTicket() throws Exception {
		String ticketJson = """
        {
            "id": 1,
            "passengerName": "John Doe",
            "flightId": 101
        }
        """;

		mockMvc.perform(post("/tickets")
						.contentType(MediaType.APPLICATION_JSON)
						.content(ticketJson))
				.andExpect(status().isOk());  // Expects 200 OK or 201 Created
	}

	// ✅ Test: Get a ticket (expects 200 OK)
	@Test
	void testGetTicket() throws Exception {
		mockMvc.perform(get("/tickets/1"))  // Assuming ID 1 exists
				.andExpect(status().isOk());
	}

	// ✅ Test: Cancel a ticket (expects 200 OK)
	@Test
	void testCancelTicket() throws Exception {
		mockMvc.perform(delete("/tickets/1"))  // Assuming ID 1 exists
				.andExpect(status().isOk());
	}
}
