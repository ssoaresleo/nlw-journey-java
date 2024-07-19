package com.rocketseat.planner.modules.link;

import com.rocketseat.planner.modules.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LinkService {
    @Autowired
    private LinkRepository linkRepository;

    public LinkResponse createLink(LinkRequestPayload linkData, Trip trip) {
        Link newLink = new Link(linkData, trip);

        this.linkRepository.save(newLink);

        return new LinkResponse(newLink.getId());
    }

    public List<LinkData> findAll(UUID tripId) {
       return this.linkRepository.findByTripId(tripId).stream().map((link) -> new LinkData(link.getId(), link.getTitle(), link.getUrl())).toList();
    }
}
