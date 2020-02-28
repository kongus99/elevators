package com.fortum.codechallenge.elevators.backend.impl;

import com.fortum.codechallenge.elevators.backend.api.ElevatorState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static com.fortum.codechallenge.elevators.backend.api.Elevator.Direction;
import static com.fortum.codechallenge.elevators.backend.api.Elevator.Direction.*;
import static com.fortum.codechallenge.elevators.backend.api.Elevator.IncorrectFloor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ElevatorImplTest {

    private ElevatorImpl elevator;

    @BeforeEach
    void setUp() {
        elevator = new ElevatorImpl(1, 10);
    }

    private ElevatorState state(int floor, Direction direction) {
        return new ElevatorState(1, floor, direction);
    }

    private void stopped(int floor, Direction beforeStop, Direction afterStop) {
        assertEquals(state(floor, beforeStop), elevator.advanceTime());
        assertEquals(state(floor, afterStop), elevator.advanceTime());
    }

    private void orderStop(Direction direction, int... stops) {
        Arrays.stream(stops).forEach(s -> assertEquals(direction, elevator.scheduleStop(s).direction));
    }

    private void movingUp(int from, int to) {
        for (int i = from; i <= to; i++)
            assertEquals(state(i, UP), elevator.advanceTime());
    }

    private void movingDown(int from, int to) {
        for (int i = from; i >= to; i--)
            assertEquals(state(i, DOWN), elevator.advanceTime());
    }

    private void costOfStopping(int floor, int cost) {
        assertEquals(cost, elevator.costOfStopping(floor));
    }

    @Test
    void getIdReturnsIdAssignedInConstructor() {
        assertEquals(1, new ElevatorImpl(1, 2).getId());
        assertEquals(23, new ElevatorImpl(23, 2).getId());
    }

    @Test
    void elevatorStartsIdleOnTheBaseFloor() {
        assertEquals(state(0, NONE)
                , new ElevatorImpl(1, 2).advanceTime());
    }

    @Test
    void elevatorCanOnlyBeScheduledToPositiveFloors() {
        new ElevatorImpl(1, 20).scheduleStop(10);
        assertThrows(IncorrectFloor.class
                , () -> new ElevatorImpl(1, 2).scheduleStop(-1));
    }

    @Test
    void elevatorCanOnlyBeScheduledBelowMaxFloor() {
        assertThrows(IncorrectFloor.class
                , () -> new ElevatorImpl(1, 10).scheduleStop(10));
    }

    @Test
    void elevatorStartsMovingWhenStopIsScheduledAndStopsWhenFloorIsReached() {
        orderStop(UP,3);
        movingUp(1, 2);
        stopped(3, UP, NONE);
    }

    @Test
    void whenSchedulingStopOnTheTopFloorTheDirectionIsSetToDown() {
        orderStop(UP,9);
        movingUp(1, 8);
        stopped(9, UP, NONE);
        orderStop(DOWN,7);
    }


    @Test
    void additionalStopsCanBeAddedToTheMovingElevator() {
        orderStop(UP,3);
        movingUp(1, 1);
        orderStop(UP,4);
        movingUp(2, 2);
        stopped(3, UP, UP);
        stopped(4, UP, NONE);
    }

    @Test
    void elevatorAwaitsAdditionalTimeUnitOnEachStop() {
        orderStop(UP,2, 3, 4);
        movingUp(1, 1);
        stopped(2, UP, UP);
        stopped(3, UP, UP);
        stopped(4, UP, NONE);
    }


    @Test
    void elevatorCanBeRedirectedOnlyAfterReachingFinalStopInCurrentDirection() {
        orderStop(UP,3);
        movingUp(1, 2);
        orderStop(UP,1);
        stopped(3, UP, DOWN);
        movingDown(2, 2);
        stopped(1, DOWN, NONE);
    }

    @Test
    void elevatorIgnoresOppositeDirectionUntilFinalFloorInCurrentDirectionIsReached() {
        orderStop(UP,4);
        movingUp(1, 2);
        orderStop(UP,1);
        movingUp(3, 3);
        orderStop(UP,5);
        stopped(4, UP, UP);
        stopped(5, UP, DOWN);
        movingDown(4, 2);
        stopped(1, DOWN, NONE);
    }

    @Test
    void elevatorCanReturnToBaseFloor() {
        orderStop(UP,1, 5, 9);
        stopped(1, UP, UP);
        movingUp(2, 4);
        stopped(5, UP, UP);
        orderStop(UP,2, 0);
        movingUp(6, 8);
        stopped(9, UP, DOWN);
        movingDown(8, 3);
        stopped(2, DOWN, DOWN);
        movingDown(1, 1);
        stopped(0, DOWN, NONE);
    }

    @Test
    void costOfStoppingNeighbouringFloorIsOne() {
        costOfStopping(1, 1);
        orderStop(UP,1);
        stopped(1, UP, NONE);
        costOfStopping(0, 1);
    }

    @Test
    void costOfStoppingOnTheElevatorPathToSingleFloorIsEqualToDistance() {
        orderStop(UP,7);
        costOfStopping(2, 2);
        costOfStopping(5, 5);
        movingUp(1, 3);
        costOfStopping(4, 1);
        costOfStopping(7, 4);
    }

    @Test
    void goingUpDestinationIsUpward() {
        orderStop(UP,7, 3, 4);
        costOfStopping(3, 3);
        costOfStopping(4, 5);
        costOfStopping(5, 7);
    }

    @Test
    void goingDownDestinationIsDownward() {
        orderStop(UP,9);
        movingUp(1, 8);
        stopped(9, UP, NONE);
        orderStop(DOWN,5, 3, 2, 1);
        costOfStopping(5, 4);
        costOfStopping(3, 7);
        costOfStopping(2, 9);
        costOfStopping(1, 11);
        costOfStopping(0, 13);
    }

    @Test
    void goingUpDestinationIsDownward() {
        orderStop(UP,6);
        movingUp(1, 4);
        orderStop(UP,3, 2, 1);
        costOfStopping(6, 2);
        costOfStopping(4, 5);
        costOfStopping(3, 6);
        costOfStopping(2, 8);
        costOfStopping(1, 10);
        costOfStopping(0, 12);
    }

    @Test
    void goingDownDestinationIsUpward() {
        orderStop(UP,9);
        movingUp(1, 8);
        stopped(9, UP, NONE);
        orderStop(DOWN,4);
        movingDown(8, 5);
        orderStop(DOWN,8, 7, 5);
        costOfStopping(4, 1);
        costOfStopping(5, 3);
        costOfStopping(7, 6);
        costOfStopping(8, 8);
        costOfStopping(9, 10);
    }


}
