package com.socialmedia.livevibe.repository;

import com.socialmedia.livevibe.entity.PerformanceRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceRequirementRepository extends JpaRepository<PerformanceRequirement, String> {
}

