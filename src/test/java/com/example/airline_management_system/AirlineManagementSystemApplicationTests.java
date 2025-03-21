package com.example.airline_management_system;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FlightControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testGetAllFlights() throws Exception {
		mockMvc.perform(get("/flights"))
				.andExpect(status().isOk());  // Always expects 200 OK
	}
}
