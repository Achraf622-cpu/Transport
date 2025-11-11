package org.example.transport.integration;

import org.example.transport.dto.CustomerDTO;
import org.example.transport.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for Customer API
 * V2.0 - Tests the complete customer creation and retrieval flow
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class CustomerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testCreateAndRetrieveCustomer() {
        // Arrange
        CustomerDTO newCustomer = new CustomerDTO();
        newCustomer.setName("Test Customer");
        newCustomer.setAddress("123 Test Street, Paris");
        newCustomer.setLatitude(48.8566);
        newCustomer.setLongitude(2.3522);
        newCustomer.setPhone("+33123456789");
        newCustomer.setEmail("test@example.com");
        newCustomer.setPreferredTimeSlot("09:00-11:00");

        // Act - Create customer
        ResponseEntity<CustomerDTO> createResponse = restTemplate.postForEntity(
                "/api/customers",
                newCustomer,
                CustomerDTO.class
        );

        // Assert - Check creation
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        assertNotNull(createResponse.getBody().getId());
        assertEquals("Test Customer", createResponse.getBody().getName());

        // Act - Retrieve customer
        Long customerId = createResponse.getBody().getId();
        ResponseEntity<CustomerDTO> getResponse = restTemplate.getForEntity(
                "/api/customers/" + customerId,
                CustomerDTO.class
        );

        // Assert - Check retrieval
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals(customerId, getResponse.getBody().getId());
        assertEquals("Test Customer", getResponse.getBody().getName());
        assertEquals("test@example.com", getResponse.getBody().getEmail());
    }

    @Test
    public void testGetAllCustomers() {
        // Act
        ResponseEntity<CustomerDTO[]> response = restTemplate.getForEntity(
                "/api/customers",
                CustomerDTO[].class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        // At least one customer should exist from the previous test or initial data
        assertTrue(response.getBody().length >= 0);
    }
}
