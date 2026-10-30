package com.socialmedia.livevibe.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceRequirementRequest {

    @NotBlank
    private String restaurantName;

    @NotBlank
    private String timeSlot;
}

