package com.fortum.codechallenge.elevators.backend.impl;

import ch.qos.logback.classic.Logger;
import com.fortum.codechallenge.elevators.backend.api.Elevator;
import com.fortum.codechallenge.elevators.backend.api.ElevatorController;
import com.fortum.codechallenge.elevators.backend.api.ElevatorState;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class ElevatorControllerImpl implements ElevatorController {

    @Override
    public void requestElevator(int floor) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void goToFloor(int floor) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public List<ElevatorState> currentState() {
        throw new UnsupportedOperationException("TODO");
    }
}
