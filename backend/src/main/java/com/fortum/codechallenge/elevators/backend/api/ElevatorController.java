package com.fortum.codechallenge.elevators.backend.api;

import com.fortum.codechallenge.elevators.backend.api.Elevator.Direction;

import java.util.List;


/**
 * Interface for the Elevator Controller.
 */
public interface ElevatorController {

    void requestElevator(int floor);

    void goToFloor(int floor);

    List<ElevatorState> currentState();

}
