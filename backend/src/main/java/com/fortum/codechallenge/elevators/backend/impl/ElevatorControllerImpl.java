package com.fortum.codechallenge.elevators.backend.impl;

import com.fortum.codechallenge.elevators.backend.api.Elevator;
import com.fortum.codechallenge.elevators.backend.api.ElevatorController;
import com.fortum.codechallenge.elevators.backend.api.ElevatorState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

import static com.fortum.codechallenge.elevators.backend.api.Elevator.Direction.NONE;
import static java.util.stream.Collectors.toList;

@Service
public class ElevatorControllerImpl implements ElevatorController {

    private static final int STEP = 2000;
    private final List<Elevator> elevators;
    private final Map<Integer, ElevatorState> states;

    @Autowired
    public ElevatorControllerImpl(@Value("${com.fortum.codechallenge.numberOfElevators}") int elevatorCount) {
        this(IntStream.range(0, elevatorCount).mapToObj(i -> new ElevatorImpl(i, 10)).collect(toList()));
    }

    public ElevatorControllerImpl(List<Elevator> elevators) {
        this.elevators = new ArrayList<>(elevators);
        this.states = new TreeMap<>();
        elevators.forEach(e -> states.put(e.getId(), new ElevatorState(e.getId(), 0, NONE)));
    }

    @Override
    public void callElevator(int floor) {
        elevators.stream()
                .min(Comparator.comparing(e -> e.costOfStopping(floor)))
                .ifPresent(perform(e -> e.scheduleStop(floor)));
    }

    @Override
    public void rideElevator(int floor, int elevatorId) {
        elevators.stream()
                .filter(e -> e.getId() == elevatorId)
                .findFirst()
                .ifPresent(perform(e -> e.scheduleStop(floor)));
    }

    @Override
    public List<ElevatorState> currentState() {
        return new ArrayList<>(states.values());
    }

    @Scheduled(fixedRate = STEP)
    private void reportCurrentTime() {
        elevators.forEach(perform(Elevator::advanceTime));
    }

    private synchronized Consumer<Elevator> perform(Function<Elevator, ElevatorState> f) {
        return e -> Optional.ofNullable(f.apply(e)).ifPresent(s -> states.put(s.id, s));
    }

}
