package com.SupportHub.demo.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import com.SupportHub.demo.dtos.InputDTOs.BusinessInputDTO;
import com.SupportHub.demo.dtos.OutputDTOs.BusinessOutputDTO;
import com.SupportHub.demo.mappers.BusinessMapper;
import com.SupportHub.demo.models.Admin;
import com.SupportHub.demo.models.Business;
import com.SupportHub.demo.models.User;
import com.SupportHub.demo.repositories.AdminRepository;
import com.SupportHub.demo.repositories.BusinessRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class BusinessService {

    private final BusinessRepository businessRepository;
    private final BusinessMapper businessMapper;
    private final AdminRepository adminRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public BusinessService(BusinessRepository businessRepository,
                           BusinessMapper businessMapper,
                           AdminRepository adminRepository) {
        this.businessRepository = businessRepository;
        this.businessMapper = businessMapper;
        this.adminRepository = adminRepository;
    }

    @Transactional
    public void createBusinessWithAdmin(BusinessInputDTO businessInputDTO, User user) {
        try {
            System.out.println("Starting business creation for user: " + user.getEmail());
            
            // Create and save business
            Business business = businessMapper.toEntity(businessInputDTO);
            System.out.println("Mapped business entity: " + business.getName());
            
            Business savedBusiness = businessRepository.save(business);
            System.out.println("Saved business with ID: " + savedBusiness.getBusinessId());
            
            // Create and save admin
            Admin admin = new Admin();
            admin.setUser(user);
            admin.setBusiness(savedBusiness);
            Admin savedAdmin = adminRepository.save(admin);
            System.out.println("Saved admin with ID: " + savedAdmin.getAdminId());
            
            // Ensure changes are persisted
            entityManager.flush();
            System.out.println("Successfully completed business and admin creation");
        } catch (Exception e) {
            System.err.println("Error in createBusinessWithAdmin: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create business with admin: " + e.getMessage());
        }
    }

    // Other methods remain the same
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
