package com.johnnywey.ratpackapi.service

import com.johnnywey.flipside.failable.Fail
import com.johnnywey.ratpackapi.domain.Session
import com.johnnywey.ratpackapi.domain.User
import de.caluga.morphium.Morphium
import de.caluga.morphium.query.MongoField
import de.caluga.morphium.query.Query
import spock.lang.Specification

import static com.johnnywey.flipside.Failables.Succeeded
import static org.junit.Assert.assertEquals

class AuthenticationServiceSpec extends Specification {
    void "test findUserForSession"() {
        given:
        final String TEST_SESSION_ID = 'TEST_SESSION'
        final String TEST_USER_ID = 'TEST_USER'
        def mockUserService = Mock(UserService)

        when: 'no sessions found'
        def mockDb = [createQueryFor: { Class klazz ->
            assertEquals Session.class, klazz
            [f: { String field ->
                assertEquals "sessionId", field
                [eq: { String val ->
                    assertEquals TEST_SESSION_ID, val
                    [asList: {
                        [] as List<Session>
                    }] as Query<Session>
                }] as MongoField
            }] as Query<Session>
        }] as Morphium

        def unit = new AuthenticationService(mockUserService, mockDb)
        def result = unit.findUserForSession(TEST_SESSION_ID)

        then: 'should fail'
        !result.isSuccess()
        Fail.NOT_FOUND == result.reason

        when: 'session found'
        mockDb = [createQueryFor: { Class klazz ->
            assertEquals Session.class, klazz
            [f: { String field ->
                assertEquals "sessionId", field
                [eq: { String val ->
                    assertEquals TEST_SESSION_ID, val
                    [asList: {
                        [new Session(userId: TEST_USER_ID, sessionId: TEST_SESSION_ID)] as List<Session>
                    }] as Query<Session>
                }] as MongoField
            }] as Query<Session>
        }] as Morphium

        unit = new AuthenticationService(mockUserService, mockDb)
        result = unit.findUserForSession(TEST_SESSION_ID)

        then: 'should find user'
        result.isSuccess()
        TEST_USER_ID == result.get().username
        1 * mockUserService.findUserByUserId(_ as String) >> { args ->
            Succeeded(new User(username: args[0] as String))
        }
    }
}
