package com.johnnywey.ratpackapi;

import com.johnnywey.ratpackapi.domain.User;
import com.johnnywey.ratpackapi.domain.UserRole;
import de.caluga.morphium.Morphium;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bootstrap {
    private final static Logger LOGGER = LoggerFactory.getLogger(Bootstrap.class);

    public static void loadSampleData(final Morphium morphium) {
        // If we have users, abort
        if (morphium.createQueryFor(User.class).countAll() > 0) {
            LOGGER.info("Skipping creation of test data");
            return;
        }

        LOGGER.info("Creating test data ...");

        User admin = new User();
        admin.setUsername("admin");
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setEmailAddress("testAdmin@test.com");
        admin.getRoles().add(UserRole.ADMINISTRATOR);
        admin.setPassword(BCrypt.hashpw("secret", BCrypt.gensalt()));

        morphium.store(admin);

        User user = new User();
        user.setUsername("user");
        user.setFirstName("Regular");
        user.setLastName("User");
        user.setEmailAddress("testUser@test.com");
        user.getRoles().add(UserRole.USER);
        user.setPassword(BCrypt.hashpw("secret", BCrypt.gensalt()));

        morphium.store(user);
    }
}
