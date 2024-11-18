package com.example.demo.mappers;

import org.springframework.stereotype.Component;
import com.example.demo.models.Business;
import com.example.demo.dtos.OutputDTOs.BusinessOutputDTO;
import com.example.demo.dtos.InputDTOs.BusinessInputDTO;

@Component
public class BusinessMapper {

    // Convert Business Entity to BusinessOutputDTO
    public BusinessOutputDTO toDto(Business business) {
        BusinessOutputDTO dto = new BusinessOutputDTO();
        dto.setBusinessId(business.getBusinessId());
        dto.setName(business.getName());
        dto.setDescription(business.getDescription());
        dto.setLogo(business.getLogo());
        return dto;
    }

    // Convert BusinessInputDTO to Business Entity
    public Business toEntity(BusinessInputDTO businessInputDTO) {
        Business business = new Business();
        business.setName(businessInputDTO.getName());
        business.setDescription(businessInputDTO.getDescription());
        business.setLogo(businessInputDTO.getLogo());
        return business;
    }
}
