package com.socialmedia.livevibe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConcertListingResponse {

    private String id;
    private String restaurantName;
    private String bandName;
    private String singerName;
    private String location;
    private String timeSlot;
}

