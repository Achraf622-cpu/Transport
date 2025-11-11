package org.example.transport.service;

import org.example.transport.dto.CustomerDTO;
import org.example.transport.entity.Customer;
import org.example.transport.exception.ResourceNotFoundException;
import org.example.transport.mapper.CustomerMapper;
import org.example.transport.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for Customer management
 * V2.0 - New service with pagination support
 */
@Service
@Transactional
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private CustomerRepository customerRepository;

    public List<CustomerDTO> getAllCustomers() {
        logger.info("Fetching all customers");
        return customerRepository.findAll().stream()
                .map(CustomerMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Page<CustomerDTO> getAllCustomersPaginated(Pageable pageable) {
        logger.info("Fetching customers with pagination: page={}, size={}", 
                   pageable.getPageNumber(), pageable.getPageSize());
        return customerRepository.findAll(pageable)
                .map(CustomerMapper::toDTO);
    }

    public CustomerDTO getCustomerById(Long id) {
        logger.info("Fetching customer with id: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return CustomerMapper.toDTO(customer);
    }

    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        logger.info("Creating new customer: {}", customerDTO.getName());
        Customer customer = CustomerMapper.toEntity(customerDTO);
        customer.setActive(true);
        Customer savedCustomer = customerRepository.save(customer);
        logger.info("Customer created successfully with id: {}", savedCustomer.getId());
        return CustomerMapper.toDTO(savedCustomer);
    }

    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        logger.info("Updating customer with id: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        
        CustomerMapper.updateEntityFromDTO(customerDTO, customer);
        Customer updatedCustomer = customerRepository.save(customer);
        logger.info("Customer updated successfully");
        return CustomerMapper.toDTO(updatedCustomer);
    }

    public void deleteCustomer(Long id) {
        logger.info("Deactivating customer with id: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        customer.setActive(false);
        customerRepository.save(customer);
        logger.info("Customer deactivated successfully");
    }

    public List<CustomerDTO> getActiveCustomers() {
        logger.info("Fetching active customers");
        return customerRepository.findByActiveTrue().stream()
                .map(CustomerMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Page<CustomerDTO> searchCustomers(String searchTerm, Pageable pageable) {
        logger.info("Searching customers with term: {}", searchTerm);
        return customerRepository.searchCustomers(searchTerm, pageable)
                .map(CustomerMapper::toDTO);
    }
}
