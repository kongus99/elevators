package com.fortum.codechallenge.elevators.backend.resources;

import com.fortum.codechallenge.elevators.backend.api.ElevatorController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest Resource.
 */
@RestController
@RequestMapping("/rest/v1")
public final class ElevatorControllerEndPoints {
    @Autowired
    private ElevatorController elevatorController;

    /**
     * Ping service to test if we are alive.
     *
     * @return String pong
     */
    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public String ping() {
        return "pong";
    }
}
