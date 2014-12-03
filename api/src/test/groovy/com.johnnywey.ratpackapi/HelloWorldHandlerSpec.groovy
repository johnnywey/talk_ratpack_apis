package com.johnnywey.ratpackapi

import ratpack.test.UnitTest
import ratpack.test.handling.HandlingResult
import spock.lang.Specification

class HelloWorldHandlerSpec extends Specification {

    void "test we actually say hello"() {
        when: 'seeing if we can say hello'
        HandlingResult result = UnitTest.handle(new HelloWorldHandler(), { fixture -> fixture.uri("/") })

        then: 'should say hello'
        200 == result.status.code
        'Hello World!' == result.rendered(String.class)
    }
}
