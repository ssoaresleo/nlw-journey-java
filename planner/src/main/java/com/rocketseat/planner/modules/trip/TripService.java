package com.rocketseat.planner.modules.trip;

import com.rocketseat.planner.modules.participants.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private ParticipantService participantService;


    public Trip createTrip(TripRequestPayload trip) {
        Trip newTrip = new Trip(trip);

        this.tripRepository.save(newTrip);

        this.participantService.registerParticipantsToTrip(trip.emails_to_invite(), newTrip);

        return newTrip;
    }

    public Optional<Trip> findTripById(UUID tripId) {
        return this.tripRepository.findById(tripId);
    }

    public void updateTripDetails(Trip rawTrip) {
        this.tripRepository.save(rawTrip);
    }
}
