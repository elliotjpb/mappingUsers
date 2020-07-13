package org.mappingusers

import com.stehno.ersatz.ErsatzServer
import com.stehno.ersatz.cfg.ContentType
import com.stehno.ersatz.encdec.Encoders
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
        boolean correct2 = getUsersWithinDistanceService.harversineDistance(lonLat, lonLong, 51.6553959, 0.0572553, 50)
        boolean correct3 = getUsersWithinDistanceService.harversineDistance(lonLat, lonLong, 51.6710832, 0.8078532, 50)
        boolean correct4 = getUsersWithinDistanceService.harversineDistance(lonLat, lonLong, 51.5079, 0.0877, 50)
        boolean correct5 = getUsersWithinDistanceService.harversineDistance(lonLat, lonLong, 51.4934, 0.0098, 50)

        expect:
        assert correct1
        assert correct2
        assert correct3
        assert correct4
        assert correct5
    }

    def "test incorrect distances"() {

        given:
        boolean distance1 = getUsersWithinDistanceService.harversineDistance(lonLat, lonLong, 52.5200, 13.4050, 50)
        boolean distance2 = getUsersWithinDistanceService.harversineDistance(lonLat, lonLong, 40.7128, 74.0060, 50)
        boolean distance3 = getUsersWithinDistanceService.harversineDistance(lonLat, lonLong, 50.8198, 1.0880, 50)
        boolean distance4 = getUsersWithinDistanceService.harversineDistance(lonLat, lonLong, 55.9533, 3.1883, 50)
        boolean distance5 = getUsersWithinDistanceService.harversineDistance(lonLat, lonLong, 48.8566, 2.3522, 50)

        expect:
        assert !distance1
        assert !distance2
        assert !distance3
        assert !distance4
        assert !distance5

    }

    def "Return null for HttpClientResponseException"() {

        given:
        ErsatzServer ersatz = new ErsatzServer()
        ersatz.expectations {
            GET('/users') {
                called(1)
                responder {
                    code(404)
                }
            }
        }

        service.setupHttpClient(ersatz.httpUrl)

        when:
        List<User> allUsers = service.allUsers()

        then:
        !allUsers

        and:
        ersatz.verify()

        cleanup:
        ersatz.stop()
    }
}
