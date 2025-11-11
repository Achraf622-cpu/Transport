package org.example.transport.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.transport.enums.DeliveryStatus;

/**
 * Entity representing a delivery
 */
@Entity
@Table(name = "deliveries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"tour"})
@EqualsAndHashCode(exclude = {"tour"})
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // V2.0 - Customer reference (address comes from customer)
    @NotNull(message = "Customer is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // Specific delivery address (can override customer default address if needed)
    @Column(length = 500)
    private String specificAddress;

    @Column
    private Double specificLatitude;

    @Column
    private Double specificLongitude;

    @NotNull(message = "Weight is required")
    @Positive(message = "Weight must be positive")
    @Column(name = "weight_kg", nullable = false)
    private Double weightKg;

    @NotNull(message = "Volume is required")
    @Positive(message = "Volume must be positive")
    @Column(name = "volume_m3", nullable = false)
    private Double volumeM3;

    @Column
    private String preferredTimeSlot; // Format: "09:00-11:00"

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status = DeliveryStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @Column
    private Integer sequenceInTour; // Order in the tour

    /**
     * Get the effective delivery address
     * Returns specific address if set, otherwise customer's address
     */
    public String getEffectiveAddress() {
        if (specificAddress != null) {
            return specificAddress;
        }
        return customer != null ? customer.getAddress() : null;
    }

    /**
     * Get the effective latitude
     */
    public Double getEffectiveLatitude() {
        if (specificLatitude != null) {
            return specificLatitude;
        }
        return customer != null ? customer.getLatitude() : null;
    }

    /**
     * Get the effective longitude
     */
    public Double getEffectiveLongitude() {
        if (specificLongitude != null) {
            return specificLongitude;
        }
        return customer != null ? customer.getLongitude() : null;
    }
}
