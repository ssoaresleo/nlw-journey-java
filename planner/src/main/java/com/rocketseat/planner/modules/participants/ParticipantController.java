package com.rocketseat.planner.modules.participants;

import com.rocketseat.planner.modules.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/participants")
public class ParticipantController {

    @Autowired
    private ParticipantService participantService;

    @PostMapping("/{id}/confirm")
    public ResponseEntity<Void> createParticipants(@PathVariable("id") UUID id, @RequestBody ParticipantRequestPayload participantData) {
        var participant = this.participantService.findParticipantById(id);

        if(participant.isPresent()) {
            Participant rawParticipant = participant.get();

            rawParticipant.setIsConfirmed(true);
            rawParticipant.setName(participantData.name());

            this.participantService.confirmParticipant(rawParticipant);

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}
