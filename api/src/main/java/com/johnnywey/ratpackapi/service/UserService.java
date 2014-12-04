package com.johnnywey.ratpackapi.service;

import com.johnnywey.flipside.failable.Fail;
import com.johnnywey.flipside.failable.Failable;
import com.johnnywey.flipside.failable.Failed;
import com.johnnywey.flipside.failable.Success;
import com.johnnywey.ratpackapi.domain.User;
import de.caluga.morphium.Morphium;
import de.caluga.morphium.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final Morphium morphium;

    public UserService(Morphium morphium) {
        this.morphium = morphium;
    }

    /**
     * Locate a user by their username.
     */
    public Failable<User> findUserByUsername(final String username) {
        return findByFieldName("username", username);
    }

    /**
     * Locate a user by its objectId.
     */
    public Failable<User> findUserByUserId(final String userId) {
        return findByFieldName("objectId", userId);
    }

    /**
     * Get all users.
     */
    public List<User> findAll() {
        return morphium.readAll(User.class);
    }

    /**
     * Locate a user by a given field name and value.
     */
    protected Failable<User> findByFieldName(final String fieldName, final String value) {
        Query<User> query = morphium.createQueryFor(User.class).f(fieldName).eq(value);
        List<User> results = query.asList();
        if (results.isEmpty()) {
            return new Failed<>(Fail.NOT_FOUND, "User not found.");
        } else {
            return new Success<>(results.get(0));
        }
    }
}
