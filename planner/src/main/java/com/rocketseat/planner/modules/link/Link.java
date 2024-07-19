package com.rocketseat.planner.modules.link;

import com.rocketseat.planner.modules.trip.Trip;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "links")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    public Link(LinkRequestPayload linkData, Trip trip) {
        this.title = linkData.title();
        this.url = linkData.url();
        this.trip = trip;
    }
}
