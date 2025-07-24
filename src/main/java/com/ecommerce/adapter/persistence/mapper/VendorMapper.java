package com.ecommerce.adapter.persistence.mapper;

import com.ecommerce.adapter.persistence.entity.VendorEntity;
import com.ecommerce.core.domain.vendor.entity.Address;
import com.ecommerce.core.domain.vendor.entity.BankDetails;
import com.ecommerce.core.domain.vendor.entity.Vendor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VendorMapper {
    
    @Mapping(target = "address", expression = "java(mapAddress(entity))")
    @Mapping(target = "bankDetails", expression = "java(mapBankDetails(entity))")
    Vendor toDomain(VendorEntity entity);
    
    @Mapping(target = "street", source = "address.street")
    @Mapping(target = "city", source = "address.city")
    @Mapping(target = "state", source = "address.state")
    @Mapping(target = "zipCode", source = "address.zipCode")
    @Mapping(target = "country", source = "address.country")
    @Mapping(target = "accountNumber", source = "bankDetails.accountNumber")
    @Mapping(target = "routingNumber", source = "bankDetails.routingNumber")
    @Mapping(target = "bankName", source = "bankDetails.bankName")
    @Mapping(target = "accountHolderName", source = "bankDetails.accountHolderName")
    VendorEntity toEntity(Vendor vendor);
    
    default Address mapAddress(VendorEntity entity) {
        if (entity.getStreet() == null) return null;
        return Address.builder()
                .street(entity.getStreet())
                .city(entity.getCity())
                .state(entity.getState())
                .zipCode(entity.getZipCode())
                .country(entity.getCountry())
                .build();
    }
    
    default BankDetails mapBankDetails(VendorEntity entity) {
        if (entity.getAccountNumber() == null) return null;
        return BankDetails.builder()
                .accountNumber(entity.getAccountNumber())
                .routingNumber(entity.getRoutingNumber())
                .bankName(entity.getBankName())
                .accountHolderName(entity.getAccountHolderName())
                .build();
    }
}