package com.socialmedia.livevibe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceRequirementResponse {

    private String id;
    private String restaurantName;
    private String timeSlot;
}

