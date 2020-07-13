package org.mappingusers

import grails.testing.mixin.integration.Integration
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

@Integration
class GetUsersWithinDistanceServiceSpec extends Specification implements ServiceUnitTest<GetUsersWithinDistanceService>{

    double lonLat = 51.507361
    double lonLong = -0.127750

    GetUsersWithinDistanceService getUsersWithinDistanceService

    def "test correct distances"() {

        given:
        boolean correct1 = getUsersWithinDistanceService.harversineDistance(lonLat, lonLong, 51.5489435, 0.3860497, 50)
        //boolean correct2 = getUsersWithinDistanceService.harversineDistance(lonLat, lonLong, ,, 50)
        //boolean correct3 = getUsersWithinDistanceService.harversineDistance(lonLat, lonLong, ,, 50)

        expect:
        assert correct1
        //assert correct2
       // assert correct3
    }
}
