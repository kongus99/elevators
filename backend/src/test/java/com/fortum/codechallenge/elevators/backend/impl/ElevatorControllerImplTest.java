package com.fortum.codechallenge.elevators.backend.impl;

import com.fortum.codechallenge.elevators.backend.api.Elevator;
import com.fortum.codechallenge.elevators.backend.api.ElevatorController;
import com.fortum.codechallenge.elevators.backend.api.ElevatorState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.Times;

import java.util.List;

import static com.fortum.codechallenge.elevators.backend.api.Elevator.Direction.NONE;
import static com.fortum.codechallenge.elevators.backend.api.Elevator.Direction.UP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ElevatorControllerImplTest {


    private ElevatorController controller;
    @Mock
    private Elevator elevator1;
    @Mock
    private Elevator elevator2;
    @Mock
    private Elevator elevator3;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(elevator1.getId()).thenReturn(11);
        when(elevator2.getId()).thenReturn(22);
        when(elevator3.getId()).thenReturn(33);
        controller = new ElevatorControllerImpl(List.of(elevator1, elevator2, elevator3));
    }

    private ElevatorState defaultState(int id) {
        return new ElevatorState(id, 0, NONE);
    }

    @Test
    void callingElevatorCallsTheOneWithLowestCost() {
        when(elevator1.costOfStopping(5)).thenReturn(100);
        when(elevator2.costOfStopping(5)).thenReturn(5);
        when(elevator3.costOfStopping(5)).thenReturn(10);
        controller.callElevator(5);
        verify(elevator2, new Times(1)).scheduleStop(5);
        verify(elevator1, new Times(0)).scheduleStop(5);
        verify(elevator3, new Times(0)).scheduleStop(5);
    }

    @Test
    void callingElevatorWhenTwoHaveSameCostCallsTheFirstOne() {
        when(elevator1.costOfStopping(5)).thenReturn(0);
        when(elevator2.costOfStopping(5)).thenReturn(0);
        when(elevator3.costOfStopping(5)).thenReturn(0);
        controller.callElevator(5);
        verify(elevator1, new Times(1)).scheduleStop(5);
        verify(elevator2, new Times(0)).scheduleStop(5);
        verify(elevator3, new Times(0)).scheduleStop(5);
    }


    @Test
    void ridingElevatorOnlyRidesWithTheRightId() {
        controller.rideElevator(7, 11);
        controller.rideElevator(5, 33);
        verify(elevator1, new Times(0)).scheduleStop(5);
        verify(elevator1, new Times(1)).scheduleStop(7);
        verify(elevator2, new Times(0)).scheduleStop(5);
        verify(elevator2, new Times(0)).scheduleStop(7);
        verify(elevator3, new Times(1)).scheduleStop(5);
        verify(elevator3, new Times(0)).scheduleStop(7);
    }

    @Test
    void elevatorStatesContainAllTheAdjustments() {
        when(elevator1.costOfStopping(7)).thenReturn(0);
        when(elevator2.costOfStopping(7)).thenReturn(5);
        when(elevator3.costOfStopping(7)).thenReturn(10);
        var state1 = new ElevatorState(11, 0, UP);
        var state3 = new ElevatorState(33, 0, UP);
        when(elevator1.scheduleStop(7)).thenReturn(state1);
        when(elevator3.scheduleStop(5)).thenReturn(state3);
        controller.callElevator(7);
        controller.rideElevator(5, 33);
        assertEquals(List.of(state1, defaultState(22), state3), controller.currentState());
    }


}
