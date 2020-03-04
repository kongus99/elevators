# Elevator

This repository contains the basic elevator simutation with floor and elevator controls.

## Frontend

The frontend is placed in the frontend directory of the project. It is written using in React using ReasonML. There are still some remains of the TS files, but they are not active. The production version still needs setting up, and the dev server runs on localhost:8000. There are no tests, but most of the react stuff can be tested using Jest or its equivalent.

### Build And Run (as is)

Installing dependencies:

    yarn install
    
Compiling:

    yarn build    

Running app server:

    yarn server

## Backend

The backend is placed in the backend directory. The default port on which it runs is 8080. It needs to run on the same host as frontend in order for the frontend to work.

### Build And Run (as is)

As the project is, the Spring app can be started as seen below.

build and run the code with Maven

    mvn package
    mvn spring-boot:run

or start the target JAR file 

    mvn package
    java -jar target/elevators-backend-0.0.1-SNAPSHOT.jar

## Specification

The soulution is based on the premise of "as realistic as possible" elevator simulator. The elevators run up and down. The elevator does not change direction until it reaches it final stop in its current direction. Multiple stops per elevator can be added, even when elevator is running. The elevator runs a single time unit between the floors and adds extra time unit for each stop. It cannot stop on the floor it is currently passing, if that stop was entered during that passage. If called from outside, the least costly elevator is called. If there are multiple elevators with the same cost, the one with the lowest id is used. If there is an elevator on the calling floor, no elevator is called.
### Solution
In order to accomodate these requirements the ElevatorController was adjusted. It now supports 3 methods: call, ride and state.
First one is used from outside of the elevator to call it to specific floor. Second is used from the inside of the elevator to ride it to specified floor. Last one casts the elevator down to its state and is used to feed the information to the frontend. The solution is still incomplete, since I assumed that the floors controls can affect the elevator. In reality, calling the elevator from outside sets up an interrupt on the specified floor and stops the elevator if it passes that floor. Since it would take too long to make it this way, I simplified this a bit. This can result in non-optimal elevator calls since the cost function is static and the elevator system is dynamic, with users constantly adjusting the cost function.
### Tests
I have written Elevator and ElevatorController using TDD, but since it was taking too much of my time I skipped the rest of the backend tests. In the frontend there are currently no tests, but most of the modules there can be tested using Jest and/or Selenium.


