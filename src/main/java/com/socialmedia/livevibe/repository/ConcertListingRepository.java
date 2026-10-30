package com.socialmedia.livevibe.repository;

import com.socialmedia.livevibe.entity.ConcertListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcertListingRepository extends JpaRepository<ConcertListing, String> {
}

