package com.ecommerce.adapter.persistence.entity;

import com.ecommerce.core.domain.vendor.entity.VendorStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "vendors")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class VendorEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(unique = true, nullable = false)
    private String slug;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    private String phone;
    private String website;
    
    @Enumerated(EnumType.STRING)
    private VendorStatus status;
    
    @Column(precision = 5, scale = 4)
    private BigDecimal commissionRate;
    
    private String businessLicense;
    private String taxId;
    
    // Address fields
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    
    // Bank details
    private String accountNumber;
    private String routingNumber;
    private String bankName;
    private String accountHolderName;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    private LocalDateTime approvedAt;
}