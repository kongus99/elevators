package com.fortum.codechallenge.elevators.backend.impl;

import com.fortum.codechallenge.elevators.backend.api.Elevator;
import com.fortum.codechallenge.elevators.backend.api.ElevatorState;

import java.util.SortedSet;
import java.util.TreeSet;

import static com.fortum.codechallenge.elevators.backend.api.Elevator.Direction.*;
import static java.lang.Math.abs;

public class ElevatorImpl implements Elevator {
    private final int id;
    private final int maxFloor;
    private int currentFloor;
    private Direction currentDirection;
    private SortedSet<Integer> destination;

    public ElevatorImpl(int id, int maxFloor) {
        this.id = id;
        this.maxFloor = maxFloor;
        currentDirection = NONE;
        currentFloor = 0;
        destination = new TreeSet<>();
    }

    private Direction nextDirection() {
        if (destination.isEmpty()) return NONE;
        switch (currentDirection) {
            case NONE:
            case UP:
                return destination.last() > currentFloor ? UP : DOWN;
            case DOWN:
            default:
                return destination.first() < currentFloor ? DOWN : UP;
        }
    }

    private int nextFloor() {
        switch (currentDirection) {
            case DOWN:
                return currentFloor - 1;
            case UP:
                return currentFloor + 1;
            case NONE:
            default:
                return currentFloor;
        }
    }

    private int distanceCost(int destinationFloor) {
        switch (currentDirection) {
            case UP:
                if (destinationFloor > currentFloor) return abs(destinationFloor - currentFloor);
                else return abs(destination.last() - currentFloor) + abs(destination.last() - destinationFloor);
            case DOWN:
                if (destinationFloor < currentFloor) return abs(destinationFloor - currentFloor);
                else return abs(destination.first() - currentFloor) + abs(destination.first() - destinationFloor);
            case NONE:
            default:
                return abs(destinationFloor - currentFloor);
        }
    }

    private SortedSet<Integer> stopsNumber(int destinationFloor) {
        switch (currentDirection) {
            case UP:
                if (destinationFloor > currentFloor) return destination.subSet(currentFloor + 1, destinationFloor);
                else return destination.subSet(destinationFloor + 1, destination.last() + 1);
            case DOWN:
                if (destinationFloor < currentFloor) return destination.subSet(destinationFloor + 1, currentFloor);
                else return destination.subSet(destination.first(), destinationFloor);
            case NONE:
            default:
                return new TreeSet<>();
        }
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public ElevatorState scheduleStop(int onFloor) {
        if (onFloor < 0 || onFloor >= maxFloor)
            throw new IncorrectFloor(onFloor);
        destination.add(onFloor);
        if(currentDirection == NONE)
            currentDirection = nextDirection();
        return new ElevatorState(id, currentFloor, currentDirection, destination);
    }

    @Override
    public int costOfStopping(int destinationFloor) {
        return distanceCost(destinationFloor) + stopsNumber(destinationFloor).size();
    }

    @Override
    public ElevatorState advanceTime() {
        var stop = destination.remove(currentFloor);
        currentDirection = nextDirection();
        if (!stop)
            currentFloor = nextFloor();
        return new ElevatorState(id, currentFloor, currentDirection, destination);
    }
}
