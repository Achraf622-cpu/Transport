package org.example.transport.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a customer
 * V2.0 - New entity for customer management
 */
@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"deliveries"})
@EqualsAndHashCode(exclude = {"deliveries"})
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Customer name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Customer address is required")
    @Column(nullable = false, length = 500)
    private String address;

    @NotNull(message = "Latitude is required")
    @Column(nullable = false)
    private Double latitude;

    @NotNull(message = "Longitude is required")
    @Column(nullable = false)
    private Double longitude;

    @Column(length = 50)
    private String preferredTimeSlot; // Format: "09:00-11:00"

    @Column
    private String phone;

    @Column
    private String email;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Delivery> deliveries = new ArrayList<>();

    @Column
    private Boolean active = true;
}
