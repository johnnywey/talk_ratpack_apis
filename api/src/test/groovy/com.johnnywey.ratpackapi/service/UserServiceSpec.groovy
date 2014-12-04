package com.johnnywey.ratpackapi.service

import com.johnnywey.flipside.failable.Fail
import com.johnnywey.ratpackapi.domain.User
import de.caluga.morphium.Morphium
import de.caluga.morphium.query.MongoField
import de.caluga.morphium.query.Query
import spock.lang.Specification

import static org.junit.Assert.assertEquals

class UserServiceSpec extends Specification {

    void "test findByFieldName"() {
        given:
        final String TEST_USERNAME = 'TESTUSER'

        when: 'no users found'
        def mockDb = [createQueryFor: { Class klazz ->
            assertEquals User.class, klazz
            [f: { String field ->
                assertEquals "username", field
                [eq: { String val ->
                    assertEquals TEST_USERNAME, val
                    [asList: {
                        [] as List<User>
                    }] as Query<User>
                }] as MongoField
            }] as Query<User>
        }] as Morphium

        def unit = new UserService(mockDb)
        def result = unit.findByFieldName("username", TEST_USERNAME)

        then: 'should fail'
        !result.isSuccess()
        Fail.NOT_FOUND == result.reason

        when: 'user found'
        mockDb = [createQueryFor: { Class klazz ->
            assertEquals User.class, klazz
            [f: { String field ->
                assertEquals "username", field
                [eq: { String val ->
                    assertEquals TEST_USERNAME, val
                    [asList: {
                        [new User(username: TEST_USERNAME)] as List<User>
                    }] as Query<User>
                }] as MongoField
            }] as Query<User>
        }] as Morphium

        unit = new UserService(mockDb)
        result = unit.findByFieldName("username", TEST_USERNAME)

        then: 'should reflect found user'
        result.isSuccess()
        TEST_USERNAME == result.get().username
    }
}
