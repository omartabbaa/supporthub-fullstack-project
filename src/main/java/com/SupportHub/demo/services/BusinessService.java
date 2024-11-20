package com.SupportHub.demo.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SupportHub.demo.dtos.InputDTOs.BusinessInputDTO;
import com.SupportHub.demo.dtos.OutputDTOs.BusinessOutputDTO;
import com.SupportHub.demo.mappers.BusinessMapper;
import com.SupportHub.demo.models.Business;
import com.SupportHub.demo.repositories.BusinessRepository;

@Service
public class BusinessService {

    private final BusinessRepository businessRepository;
    private final BusinessMapper businessMapper;

    @Autowired
    public BusinessService(BusinessRepository businessRepository, BusinessMapper businessMapper) {
        this.businessRepository = businessRepository;
        this.businessMapper = businessMapper;
    }

    public Optional<BusinessOutputDTO> findBusinessById(Long businessId) {
        return businessRepository.findById(businessId).map(businessMapper::toDto);
    }

    public List<BusinessOutputDTO> findAllBusinesses() {
        return businessRepository.findAll().stream()
                .map(businessMapper::toDto)
                .collect(Collectors.toList());
    }

    public BusinessOutputDTO createBusiness(BusinessInputDTO businessInputDTO) {
        Business business = businessMapper.toEntity(businessInputDTO);
        Business savedBusiness = businessRepository.save(business);
        return businessMapper.toDto(savedBusiness);
    }

    public BusinessOutputDTO updateBusiness(Long businessId, BusinessInputDTO businessInputDTO) {
        return businessRepository.findById(businessId)
                .map(business -> {
                    business.setName(businessInputDTO.getName());
                    business.setDescription(businessInputDTO.getDescription());
                    business.setLogo(businessInputDTO.getLogo());
                    return businessMapper.toDto(businessRepository.save(business));
                }).orElseThrow(() -> new RuntimeException("Business not found with ID: " + businessId));
    }

    public void deleteBusinessById(Long businessId) {
        if (businessRepository.existsById(businessId)) {
            businessRepository.deleteById(businessId);
        } else {
            throw new RuntimeException("Business not found with ID: " + businessId);
        }
    }

}
