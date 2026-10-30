package com.socialmedia.livevibe.service;

import com.socialmedia.livevibe.dto.PerformanceRequirementRequest;
import com.socialmedia.livevibe.dto.PerformanceRequirementResponse;
import com.socialmedia.livevibe.entity.PerformanceRequirement;
import com.socialmedia.livevibe.repository.PerformanceRequirementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerformanceRequirementService {

    private final PerformanceRequirementRepository performanceRequirementRepository;

    public PerformanceRequirementService(PerformanceRequirementRepository performanceRequirementRepository) {
        this.performanceRequirementRepository = performanceRequirementRepository;
    }

    public List<PerformanceRequirementResponse> getAllRequirements() {
        return performanceRequirementRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public PerformanceRequirementResponse addRequirement(PerformanceRequirementRequest request) {
        PerformanceRequirement requirement = new PerformanceRequirement(
                null,
                request.getRestaurantName(),
                request.getTimeSlot()
        );

        PerformanceRequirement savedRequirement = performanceRequirementRepository.save(requirement);
        return toResponse(savedRequirement);
    }

    private PerformanceRequirementResponse toResponse(PerformanceRequirement requirement) {
        return new PerformanceRequirementResponse(
                requirement.getId(),
                requirement.getRestaurantName(),
                requirement.getTimeSlot()
        );
    }
}

