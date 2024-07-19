package com.rocketseat.planner.modules.trip;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "trips")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Trip {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String destination;

    @Column(name = "starts_at", nullable = false)
    private LocalDateTime startsAt;

    @Column(name = "ends_at", nullable = false)
    private LocalDateTime endsAt;

    @Column(name = "is_confirmed", nullable = false)
    private Boolean isConfirmed;

    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @Column(name = "owner_email", nullable = false)
    private String ownerEmail;

    public Trip(TripRequestPayload tripData) {
        this.destination = tripData.destination();
        this.startsAt = LocalDateTime.parse(tripData.starts_at(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.endsAt = LocalDateTime.parse(tripData.ends_at(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.isConfirmed = false;
        this.ownerName = tripData.owner_name();
        this.ownerEmail = tripData.owner_email();
    }
}
