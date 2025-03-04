package com.rocketseat.planner.modules.trip;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TripRepository extends JpaRepository<Trip, UUID> {
}
