package com.johnnywey.ratpackapi.service;

import com.johnnywey.flipside.failable.Fail;
import com.johnnywey.flipside.failable.Failable;
import com.johnnywey.flipside.failable.Failed;
import com.johnnywey.ratpackapi.domain.Session;
import com.johnnywey.ratpackapi.domain.User;
import de.caluga.morphium.Morphium;
import de.caluga.morphium.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

public class AuthenticationService {
    private final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

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

    /**
     * For a given user, create a session.
     */
    public String createSessionForUser(final User user) {
        log.info("Creating session for [" + user.getUsername() + "]");
        String sessionId = UUID.randomUUID().toString();
        Session session = new Session();
        session.setSessionId(sessionId);
        session.setUserId(user.getId());
        morphium.store(session);

        return sessionId;
    }

    /**
     * For a given sessionId, destroy it.
     */
    public void destroySessionBySessionId(final String sessionId) {
        log.info("Destroying session [" + sessionId + "]");
        morphium.createQueryFor(Session.class).f("sessionId").eq(sessionId).delete();
    }
}
