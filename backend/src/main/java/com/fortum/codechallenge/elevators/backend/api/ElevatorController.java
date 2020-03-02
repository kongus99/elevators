package com.fortum.codechallenge.elevators.backend.api;

import java.util.List;


/**
 * Interface for the Elevator Controller.
 */
public interface ElevatorController {

    /**
     * Request an elevator to the specified floor, from outside of an elevator.
     *
     * @param floor floor from which the request was issued.
     */
    void callElevator(int floor);

    /**
     * Orders the specific elevator to ride to specific floor, from inside of an elevator
     *
     * @param floor floor on which the elevator should stop.
     * @param elevatorId id of the elevator from which the request was issued.
     */
    void rideElevator(int floor, int elevatorId);

    /**
     * Probes the controller for the current states of the elevators
     *
     * @return list of elevator states, sorted by id
     */
    List<ElevatorState> currentState();

}
