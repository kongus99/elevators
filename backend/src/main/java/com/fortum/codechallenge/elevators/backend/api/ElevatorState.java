package com.fortum.codechallenge.elevators.backend.api;

import java.util.Objects;

import static com.fortum.codechallenge.elevators.backend.api.Elevator.*;

public class ElevatorState {
    public final int id;
    public final int floor;
    public final Direction direction;

    public ElevatorState(int id, int floor, Direction direction) {
        this.id = id;
        this.floor = floor;
        this.direction = direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElevatorState that = (ElevatorState) o;
        return id == that.id &&
                floor == that.floor &&
                direction == that.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, floor, direction);
    }

    @Override
    public String toString() {
        return "ElevatorState{" +
                "id=" + id +
                ", floor=" + floor +
                ", direction=" + direction +
                '}';
    }
}
