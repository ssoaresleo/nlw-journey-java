package com.rocketseat.planner.modules.participants;

import com.rocketseat.planner.modules.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    public void registerParticipantsToTrip(List<String> participantsToInvite, Trip trip) {
        List<Participant> participants = participantsToInvite.stream().map(email -> new Participant(email, trip)).toList();

        this.participantRepository.saveAll(participants);
    }

    public void triggerConfirmationEmailToParticipants(UUID tripId) {}

    public Optional<Participant> findParticipantById(UUID participantId) {
        return this.participantRepository.findById(participantId);
    }

    public void confirmParticipant(Participant rawParticipant) {
        this.participantRepository.save(rawParticipant);
    }

    public ParticipantResponse registerParticipantInviteTrip(String email, Trip trip) {
        Participant newParticipant = new Participant(email, trip);
        this.participantRepository.save(newParticipant);

        return new ParticipantResponse(newParticipant.getId());
    }

    public void triggerConfirmationEmailToParticipantInvite(String email) {}

    public List<Participant> findAll(UUID tripId) {
        return this.participantRepository.getParticipantsByTripId(tripId);
    }
}
