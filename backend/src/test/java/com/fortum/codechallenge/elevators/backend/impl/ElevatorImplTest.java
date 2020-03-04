package com.fortum.codechallenge.elevators.backend.impl;

import com.fortum.codechallenge.elevators.backend.api.ElevatorState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

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

    private ElevatorState state(int floor, Direction direction, Set<Integer> stops) {
        return new ElevatorState(1, floor, direction, stops);
    }

    private void stopped(int floor, Direction beforeStop, Direction afterStop, Set<Integer> stops) {
        assertEquals(state(floor, beforeStop, stops), elevator.advanceTime());
        assertEquals(state(floor, afterStop, stops.stream().filter(i -> i != floor).collect(Collectors.toSet())), elevator.advanceTime());
    }

    private void orderStop(Direction direction, int... stops) {
        Arrays.stream(stops).forEach(s -> assertEquals(direction, elevator.scheduleStop(s).direction));
    }

    private void movingUp(int from, int to, Set<Integer> stops) {
        for (int i = from; i <= to; i++)
            assertEquals(state(i, UP, stops), elevator.advanceTime());
    }

    private void movingDown(int from, int to, Set<Integer> stops) {
        for (int i = from; i >= to; i--)
            assertEquals(state(i, DOWN, stops), elevator.advanceTime());
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
        assertEquals(state(0, NONE, Set.of())
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
        orderStop(UP, 3);
        movingUp(1, 2, Set.of(3));
        stopped(3, UP, NONE, Set.of(3));
    }

    @Test
    void whenSchedulingStopOnTheTopFloorTheDirectionIsSetToDown() {
        orderStop(UP, 9);
        movingUp(1, 8, Set.of(9));
        stopped(9, UP, NONE, Set.of(9));
        orderStop(DOWN, 7);
    }


    @Test
    void additionalStopsCanBeAddedToTheMovingElevator() {
        orderStop(UP, 3);
        movingUp(1, 1, Set.of(3));
        orderStop(UP, 4);
        movingUp(2, 2, Set.of(3, 4));
        stopped(3, UP, UP, Set.of(3, 4));
        stopped(4, UP, NONE, Set.of(4));
    }

    @Test
    void elevatorAwaitsAdditionalTimeUnitOnEachStop() {
        orderStop(UP, 2, 3, 4);
        movingUp(1, 1, Set.of(2, 4, 3));
        stopped(2, UP, UP, Set.of(2, 4, 3));
        stopped(3, UP, UP, Set.of(4, 3));
        stopped(4, UP, NONE, Set.of(4));
    }


    @Test
    void elevatorCanBeRedirectedOnlyAfterReachingFinalStopInCurrentDirection() {
        orderStop(UP, 3);
        movingUp(1, 2, Set.of(3));
        orderStop(UP, 1);
        stopped(3, UP, DOWN, Set.of(3, 1));
        movingDown(2, 2, Set.of(1));
        stopped(1, DOWN, NONE, Set.of(1));
    }

    @Test
    void elevatorIgnoresOppositeDirectionUntilFinalFloorInCurrentDirectionIsReached() {
        orderStop(UP, 4);
        movingUp(1, 2, Set.of(4));
        orderStop(UP, 1);
        movingUp(3, 3, Set.of(1, 4));
        orderStop(UP, 5);
        stopped(4, UP, UP, Set.of(1, 4, 5));
        stopped(5, UP, DOWN, Set.of(1, 5));
        movingDown(4, 2, Set.of(1));
        stopped(1, DOWN, NONE, Set.of(1));
    }

    @Test
    void elevatorCanReturnToBaseFloor() {
        orderStop(UP, 1, 5, 9);
        stopped(1, UP, UP, Set.of(1, 5, 9));
        movingUp(2, 4, Set.of(5, 9));
        stopped(5, UP, UP, Set.of(5, 9));
        orderStop(UP, 2, 0);
        movingUp(6, 8, Set.of(0, 2, 9));
        stopped(9, UP, DOWN, Set.of(0, 2, 9));
        movingDown(8, 3, Set.of(0, 2));
        stopped(2, DOWN, DOWN, Set.of(0, 2));
        movingDown(1, 1, Set.of(0));
        stopped(0, DOWN, NONE, Set.of(0));
    }

    @Test
    void costOfStoppingNeighbouringFloorIsOne() {
        costOfStopping(1, 1);
        orderStop(UP, 1);
        stopped(1, UP, NONE, Set.of(1));
        costOfStopping(0, 1);
    }

    @Test
    void costOfStoppingOnTheElevatorPathToSingleFloorIsEqualToDistance() {
        orderStop(UP, 7);
        costOfStopping(2, 2);
        costOfStopping(5, 5);
        movingUp(1, 3, Set.of(7));
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
        movingUp(1, 8, Set.of(9));
        stopped(9, UP, NONE, Set.of(9));
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
        movingUp(1, 4, Set.of(6));
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
        movingUp(1, 8, Set.of(9));
        stopped(9, UP, NONE, Set.of(9));
        orderStop(DOWN,4);
        movingDown(8, 5, Set.of(4));
        orderStop(DOWN,8, 7, 5);
        costOfStopping(4, 1);
        costOfStopping(5, 3);
        costOfStopping(7, 6);
        costOfStopping(8, 8);
        costOfStopping(9, 10);
    }


}
