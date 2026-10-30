package com.socialmedia.livevibe.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConcertListingRequest {

    @NotBlank
    private String restaurantName;

    @NotBlank
    private String bandName;

    @NotBlank
    private String singerName;

    @NotBlank
    private String location;

    @NotBlank
    private String timeSlot;
}

