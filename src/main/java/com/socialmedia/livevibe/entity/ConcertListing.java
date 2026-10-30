package com.socialmedia.livevibe.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "concert_listings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConcertListing {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String restaurantName;

    @Column(nullable = false)
    private String bandName;

    @Column(nullable = false)
    private String singerName;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String timeSlot;
}

