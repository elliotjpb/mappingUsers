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

    def "A list of User objects is created from a JSON Payload 2"() {

        String bodyJson =
                "    {\n" +
                        "        \"ip_address\": \"113.71.242.187\",\n" +
                        "        \"last_name\": \"Boam\",\n" +
                        "        \"id\": 135,\n" +
                        "        \"longitude\": 105.652983,\n" +
                        "        \"email\": \"mboam3q@thetimes.co.uk\",\n" +
                        "        \"latitude\": -6.5115909,\n" +
                        "        \"city\": null,\n" +
                        "        \"first_name\": \"Mechelle\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"ip_address\": \"143.190.50.240\",\n" +
                        "        \"last_name\": \"Stowgill\",\n" +
                        "        \"id\": 396,\n" +
                        "        \"longitude\": 111.3479498,\n" +
                        "        \"email\": \"tstowgillaz@webeden.co.uk\",\n" +
                        "        \"latitude\": -6.7098551,\n" +
                        "        \"city\": null,\n" +
                        "        \"first_name\": \"Terry\"\n" +
                        "    }"

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

        usersInCity.get(1).id == 396
        usersInCity.get(1).first_name == 'Terry'
        usersInCity.get(1).last_name == 'Stowgill'
        usersInCity.get(1).email == 'tstowgillaz@webeden.co.uk'
        usersInCity.get(1).ip_address == '143.190.50.240'
        usersInCity.get(1).latitude.toBigDecimal() ==  -6.7098551
        usersInCity.get(1).longitude.toBigDecimal() == 111.3479498

        and:
        ersatz.verify()

        cleanup:
        ersatz.stop()
    }
}
