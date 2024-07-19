package com.rocketseat.planner.modules.activities;

import com.rocketseat.planner.modules.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {
    @Autowired
    private ActivityRepository activityRepository;

    public ActivityResponse createActivity(ActivityRequestPayload activity, Trip trip) {
        Activity newActivity = new Activity(activity, trip);
        this.activityRepository.save(newActivity);

        return new ActivityResponse(newActivity.getId());
    }

    public List<Activity> findAll(UUID tripId) {
        return this.activityRepository.findByTripId(tripId);
    }
}
