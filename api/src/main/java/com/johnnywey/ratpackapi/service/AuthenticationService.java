package com.johnnywey.ratpackapi.service;

import com.johnnywey.flipside.failable.Fail;
import com.johnnywey.flipside.failable.Failable;
import com.johnnywey.flipside.failable.Failed;
import com.johnnywey.ratpackapi.domain.Session;
import com.johnnywey.ratpackapi.domain.User;
import de.caluga.morphium.Morphium;
import de.caluga.morphium.query.Query;

import java.util.List;

public class AuthenticationService {

    private final UserService userService;
    private final Morphium morphium;

    public AuthenticationService(UserService userService, Morphium morphium) {
        this.userService = userService;
        this.morphium = morphium;
    }

    /**
     * Given a session id, find the user (if one exists).
     */
    public Failable<User> findUserForSession(final String sessionId) {
        Query<Session> query = morphium.createQueryFor(Session.class).f("sessionId").eq(sessionId);
        List<Session> results = query.asList();

        if (results.isEmpty()) {
            return new Failed<>(Fail.NOT_FOUND, "Session not found.");
        }

        return userService.findUserByUserId(results.get(0).getUserId());
    }
}
