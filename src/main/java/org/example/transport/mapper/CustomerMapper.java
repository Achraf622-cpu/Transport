package org.example.transport.mapper;

import org.example.transport.dto.CustomerDTO;
import org.example.transport.entity.Customer;

/**
 * Mapper for Customer entity and DTO
 * V2.0
 */
public class CustomerMapper {

    public static CustomerDTO toDTO(Customer customer) {
        if (customer == null) {
            return null;
        }
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setAddress(customer.getAddress());
        dto.setLatitude(customer.getLatitude());
        dto.setLongitude(customer.getLongitude());
        dto.setPreferredTimeSlot(customer.getPreferredTimeSlot());
        dto.setPhone(customer.getPhone());
        dto.setEmail(customer.getEmail());
        dto.setActive(customer.getActive());
        return dto;
    }

    public static Customer toEntity(CustomerDTO dto) {
        if (dto == null) {
            return null;
        }
        Customer customer = new Customer();
        customer.setId(dto.getId());
        customer.setName(dto.getName());
        customer.setAddress(dto.getAddress());
        customer.setLatitude(dto.getLatitude());
        customer.setLongitude(dto.getLongitude());
        dto.setPreferredTimeSlot(dto.getPreferredTimeSlot());
        customer.setPhone(dto.getPhone());
        customer.setEmail(dto.getEmail());
        customer.setActive(dto.getActive() != null ? dto.getActive() : true);
        return customer;
    }

    public static void updateEntityFromDTO(CustomerDTO dto, Customer customer) {
        if (dto == null || customer == null) {
            return;
        }
        customer.setName(dto.getName());
        customer.setAddress(dto.getAddress());
        customer.setLatitude(dto.getLatitude());
        customer.setLongitude(dto.getLongitude());
        customer.setPreferredTimeSlot(dto.getPreferredTimeSlot());
        customer.setPhone(dto.getPhone());
        customer.setEmail(dto.getEmail());
        if (dto.getActive() != null) {
            customer.setActive(dto.getActive());
        }
    }
}
