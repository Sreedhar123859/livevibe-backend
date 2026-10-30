package com.socialmedia.livevibe.controller;

import com.socialmedia.livevibe.dto.PerformanceRequirementRequest;
import com.socialmedia.livevibe.dto.PerformanceRequirementResponse;
import com.socialmedia.livevibe.service.PerformanceRequirementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/requirements")
@CrossOrigin(origins = "*")
public class PerformanceRequirementController {

    private final PerformanceRequirementService performanceRequirementService;

    public PerformanceRequirementController(PerformanceRequirementService performanceRequirementService) {
        this.performanceRequirementService = performanceRequirementService;
    }

    @GetMapping
    public ResponseEntity<List<PerformanceRequirementResponse>> getAllRequirements() {
        return ResponseEntity.ok(performanceRequirementService.getAllRequirements());
    }

    @PostMapping
    public ResponseEntity<PerformanceRequirementResponse> addRequirement(@Valid @RequestBody PerformanceRequirementRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(performanceRequirementService.addRequirement(request));
    }
}

