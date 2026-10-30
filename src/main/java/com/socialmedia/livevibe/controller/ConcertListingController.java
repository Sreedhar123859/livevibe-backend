package com.socialmedia.livevibe.controller;

import com.socialmedia.livevibe.dto.ConcertListingRequest;
import com.socialmedia.livevibe.dto.ConcertListingResponse;
import com.socialmedia.livevibe.service.ConcertListingService;
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
@RequestMapping("/api/concert-listings")
@CrossOrigin(origins = "*")
public class ConcertListingController {

    private final ConcertListingService concertListingService;

    public ConcertListingController(ConcertListingService concertListingService) {
        this.concertListingService = concertListingService;
    }

    @GetMapping
    public ResponseEntity<List<ConcertListingResponse>> getAllConcertListings() {
        return ResponseEntity.ok(concertListingService.getAllConcertListings());
    }

    @PostMapping
    public ResponseEntity<ConcertListingResponse> addConcertListing(@Valid @RequestBody ConcertListingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(concertListingService.addConcertListing(request));
    }
}

