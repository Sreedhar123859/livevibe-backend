package com.socialmedia.livevibe.service;

import com.socialmedia.livevibe.dto.ConcertListingRequest;
import com.socialmedia.livevibe.dto.ConcertListingResponse;
import com.socialmedia.livevibe.entity.ConcertListing;
import com.socialmedia.livevibe.repository.ConcertListingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConcertListingService {

    private final ConcertListingRepository concertListingRepository;

    public ConcertListingService(ConcertListingRepository concertListingRepository) {
        this.concertListingRepository = concertListingRepository;
    }

    public List<ConcertListingResponse> getAllConcertListings() {
        return concertListingRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public ConcertListingResponse addConcertListing(ConcertListingRequest request) {
        ConcertListing concertListing = new ConcertListing(
                null,
                request.getRestaurantName(),
                request.getBandName(),
                request.getSingerName(),
                request.getLocation(),
                request.getTimeSlot()
        );

        ConcertListing savedConcertListing = concertListingRepository.save(concertListing);
        return toResponse(savedConcertListing);
    }

    private ConcertListingResponse toResponse(ConcertListing concertListing) {
        return new ConcertListingResponse(
                concertListing.getId(),
                concertListing.getRestaurantName(),
                concertListing.getBandName(),
                concertListing.getSingerName(),
                concertListing.getLocation(),
                concertListing.getTimeSlot()
        );
    }
}

