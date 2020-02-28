package com.fortum.codechallenge.elevators.backend.api;

/**
 * Interface for an elevator object.
 */
public interface Elevator {
    /**
     * Get the Id of this elevator.
     *
     * @return primitive integer representing the elevator.
     */
    int getId();
    /**
     * Adds a stop to this elevator. It also sets the direction the elevator is moving in, if it was not set before.
     *
     * @param onFloor which floor we want to stop on.
     * @throws IncorrectFloor if the floor is outside range
     * @return current state of elevator after adding the stop
     */
    ElevatorState scheduleStop(int onFloor);
    /**
     * Calculates how long it would take for this elevator to reach the given floor.
     *
     * @param destinationFloor which floor we would like the elevator to reach.
     * @return cost of reaching a floor.
     */
    int costOfStopping(int destinationFloor);
    /**
     * This function is used to advance the time for the elevator.
     * It is done this way to simplify the problems connected with making elevators threads.
     *
     * @return state of the elevator after the time unit passes
     */
    ElevatorState advanceTime();

    /**
     * Enumeration for describing elevator's direction.
     */
    enum Direction {
        UP, DOWN, NONE
    }

    class IncorrectFloor extends RuntimeException {
        public IncorrectFloor(int floor) {
            super("Incorrect destination floor " + floor);
        }
    }
}
