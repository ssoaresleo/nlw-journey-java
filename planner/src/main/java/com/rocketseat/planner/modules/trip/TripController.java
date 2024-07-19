package com.rocketseat.planner.modules.trip;

import com.rocketseat.planner.modules.activities.Activity;
import com.rocketseat.planner.modules.activities.ActivityRequestPayload;
import com.rocketseat.planner.modules.activities.ActivityResponse;
import com.rocketseat.planner.modules.activities.ActivityService;
import com.rocketseat.planner.modules.link.*;
import com.rocketseat.planner.modules.participants.Participant;
import com.rocketseat.planner.modules.participants.ParticipantResponse;
import com.rocketseat.planner.modules.participants.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private TripService tripService;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private LinkService linkService;

    @GetMapping("/{tripId}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable("tripId") UUID tripId) {
        var trip = this.tripService.findTripById(tripId);

        return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{tripId}/confirm")
    public ResponseEntity<Trip> confirmTrip(@PathVariable("tripId") UUID tripId) {
        var trip = this.tripService.findTripById(tripId);

        if(trip.isPresent()) {
            Trip rawTrip = trip.get();
            rawTrip.setIsConfirmed(true);
            this.tripService.updateTripDetails(rawTrip);

            this.participantService.triggerConfirmationEmailToParticipants(tripId);

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{tripId}")
    public ResponseEntity<Void> updateTripDetails(@PathVariable("tripId") UUID tripId, @RequestBody TripRequestPayload tripData) {
        var trip = this.tripService.findTripById(tripId);

        if(trip.isPresent()) {
            Trip rawTrip = trip.get();

            rawTrip.setDestination(tripData.destination());
            rawTrip.setStartsAt(LocalDateTime.parse(tripData.starts_at(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            rawTrip.setEndsAt(LocalDateTime.parse(tripData.ends_at(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));

            this.tripService.updateTripDetails(rawTrip);

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestPayload tripData) {
        Trip trip = this.tripService.createTrip(tripData);

        return ResponseEntity.status(HttpStatus.CREATED).body(new TripCreateResponse(trip.getId()));
    }

    @GetMapping("/{tripId}/participants")
    public ResponseEntity<List<Participant>> getParticipantsTrip(@PathVariable UUID tripId) {
        List<Participant> participants = this.participantService.findAll(tripId);

        return ResponseEntity.ok().body(participants);
    }

    @PostMapping("/{tripId}/invites")
    public ResponseEntity<ParticipantResponse> inviteParticipant(@PathVariable("tripId") UUID tripId, @RequestBody TripInviteToParticipantRequest inviteData) {
        var trip = this.tripService.findTripById(tripId);

        if(trip.isPresent()) {
            var participant = this.participantService.registerParticipantInviteTrip(inviteData.email(), trip.get());

            if(trip.get().getIsConfirmed()) {
                this.participantService.triggerConfirmationEmailToParticipantInvite(inviteData.email());
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(participant);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{tripId}/activities")
    public ResponseEntity<ActivityResponse> createActivity(@PathVariable("tripId") UUID tripId, @RequestBody ActivityRequestPayload activityData) {
        var trip = this.tripService.findTripById(tripId);

        System.out.println(activityData);

        if(trip.isPresent()) {
            ActivityResponse activityId = this.activityService.createActivity(activityData, trip.get());

            return ResponseEntity.status(HttpStatus.CREATED).body(activityId);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{tripId}/activities")
    public ResponseEntity<List<Activity>> getAcitivities(@PathVariable UUID tripId) {
        List<Activity> activities = this.activityService.findAll(tripId);

        return ResponseEntity.ok().body(activities);
    }

    @PostMapping("/{tripId}/links")
    public ResponseEntity<LinkResponse> createLinkTrip(@PathVariable("tripId") UUID tripId, @RequestBody LinkRequestPayload linkData) {
        var trip = this.tripService.findTripById(tripId);

        if(trip.isPresent()) {
            LinkResponse linkId = this.linkService.createLink(linkData, trip.get());

            return ResponseEntity.status(HttpStatus.CREATED).body(linkId);
        }

        return ResponseEntity.notFound().build();
    }
    @GetMapping("/{tripId}/links")
    public ResponseEntity<List<LinkData>> getAllLinks(@PathVariable UUID tripId) {
        List<LinkData> links = this.linkService.findAll(tripId);

        return ResponseEntity.ok().body(links);
    }

}
