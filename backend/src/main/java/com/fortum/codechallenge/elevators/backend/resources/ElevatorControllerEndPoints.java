package com.fortum.codechallenge.elevators.backend.resources;

import com.fortum.codechallenge.elevators.backend.api.ElevatorController;
import com.fortum.codechallenge.elevators.backend.api.ElevatorState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest Resource.
 */
@RestController
@RequestMapping("/rest/v1")
public final class ElevatorControllerEndPoints {

    private final ElevatorController elevatorController;

    @Autowired
    public ElevatorControllerEndPoints(ElevatorController elevatorController) {
        this.elevatorController = elevatorController;
    }

    /**
     * Probes the controller for the current states of the elevators
     *
     * @return list of elevator states, sorted by id
     */
    @GetMapping(value = "/state")
    public List<ElevatorState> state() {
        return elevatorController.currentState();
    }

    /**
     * Orders the specific elevator to ride to specific floor, from inside of an elevator
     *
     * @param floor floor on which the elevator should stop.
     * @param elevatorId id of the elevator from which the request was issued.
     */
    @GetMapping(value = "/ride", params = {"floor", "elevatorId"})
    public void ride(@RequestParam("floor") int floor, @RequestParam("elevatorId") int elevatorId) {
        elevatorController.rideElevator(floor, elevatorId);
    }

    /**
     * Request an elevator to the specified floor, from outside of an elevator.
     *
     * @param floor floor from which the request was issued.
     */
    @GetMapping(value = "/call", params = {"floor"})
    public void call(@RequestParam("floor") int floor) {
        elevatorController.callElevator(floor);
    }

    /**
     * Ping service to test if we are alive.
     *
     * @return String pong
     */
    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public String ping() {
        return "pong";
    }
}
