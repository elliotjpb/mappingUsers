package org.mappingusers

import com.stehno.ersatz.ErsatzServer
import com.stehno.ersatz.cfg.ContentType
import com.stehno.ersatz.encdec.Encoders
import grails.testing.mixin.integration.Integration
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

@Integration
class GetUsersInCityServiceSpec extends Specification implements ServiceUnitTest<GetUsersInCityService>{

    def "A list of User objects is created from a JSON Payload"() {

        String bodyJson = "{" +
                "\"id\": 135, " +
                "\"first_name\": \"Mechelle\", " +
                "\"last_name\": \"Boam\", " +
                "\"email\": \"mboam3q@thetimes.co.uk\", " +
                "\"ip_address\": \"113.71.242.187\", " +
                "\"latitude\": -6.5115909, " +
                "\"longitude\": 105.652983" +
                "}"

        given:
        ErsatzServer ersatz = new ErsatzServer()
        ersatz.expectations {
            GET('/city/London/users') {
                called(1)
                responder {
                    encoder(ContentType.APPLICATION_JSON, Map, Encoders.json) // <1>
                    code(200)
                    body([ bodyJson ], ContentType.APPLICATION_JSON)
                }
            }
        }

        service.setupHttpClient(ersatz.httpUrl)

        when:
        List<User> usersInCity = service.getUsersInCity('London')

        then:
        usersInCity

        usersInCity.get(0).id == 135
        usersInCity.get(0).first_name == 'Mechelle'
        usersInCity.get(0).last_name == 'Boam'
        usersInCity.get(0).email == 'mboam3q@thetimes.co.uk'
        usersInCity.get(0).ip_address == '113.71.242.187'
        usersInCity.get(0).latitude.toBigDecimal() ==  -6.5115909
        usersInCity.get(0).longitude.toBigDecimal() == 105.652983

        and:
        ersatz.verify()

        cleanup:
        ersatz.stop()
    }
}
