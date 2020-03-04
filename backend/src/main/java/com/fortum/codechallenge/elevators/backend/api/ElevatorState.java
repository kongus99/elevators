package com.fortum.codechallenge.elevators.backend.api;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.fortum.codechallenge.elevators.backend.api.Elevator.Direction;

public class ElevatorState {
    public final int id;
    public final int floor;
    public final Direction direction;
    public final List<Integer> stops;

    public ElevatorState(int id, int floor, Direction direction, Collection<Integer> stops) {
        this.id = id;
        this.floor = floor;
        this.direction = direction;
        this.stops = stops.stream().sorted().collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElevatorState that = (ElevatorState) o;
        return id == that.id &&
                floor == that.floor &&
                direction == that.direction &&
                stops.equals(that.stops);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, floor, direction, stops);
    }

    @Override
    public String toString() {
        return "ElevatorState{" +
                "id=" + id +
                ", floor=" + floor +
                ", direction=" + direction +
                ", stops=" + stops +
                '}';
    }
}
