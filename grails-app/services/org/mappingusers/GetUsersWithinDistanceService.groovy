package org.mappingusers

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import grails.config.Config
import grails.core.support.GrailsConfigurationAware
import grails.gorm.transactions.Transactional
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.http.uri.UriBuilder

@Transactional
class GetUsersWithinDistanceService implements GrailsConfigurationAware {

    HttpClient client
    double centralLat
    double centralLong
    double distance

    @Override
    void setConfiguration(Config co) {
        setupHttpClient(co.getProperty('userdata.url', String))
        this.centralLat = Double.parseDouble(co.getProperty('userdata.city.latitude'))
        this.centralLong = Double.parseDouble(co.getProperty('userdata.city.longitude'))
        this.distance = Double.parseDouble(co.getProperty('userdata.distance'))
    }

    void setupHttpClient(String url) {
        this.client = HttpClient.create(url.toURL())
    }

    URI usersWithinDistanceUri() {
        UriBuilder uriBuilder = UriBuilder.of('/users')
        uriBuilder.build()
    }

    List<GUser> allUsers() {
        HttpRequest request = HttpRequest.GET(usersWithinDistanceUri())
        HttpResponse<String> resp = client.toBlocking().exchange(request, String)
        String json = resp.body()
        ObjectMapper objectMapper = new ObjectMapper()
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        List<GUser> allUsers = objectMapper.readValue(json, new TypeReference<List<GUser>>(){})
        getUsersWithinDistance(allUsers)
    }

    List<GUser> getUsersWithinDistance(List<GUser> allUsers) {

        List<GUser> withinDistance = new ArrayList<>()

        for (GUser g : allUsers) {
            if (harversineDistance(centralLat, centralLong, g.latitude, g.longitude, distance)) {
                withinDistance.add(g)
            }
        }

        return withinDistance
    }



    boolean harversineDistance(double centralLat, double centralLong, double pointLat, double pointLong, double distance) {

        final double CIRCUMFERENCE_SEGMENT = 6372.8
        final double KM_TO_MILES = 0.621371

        double latDifference = Math.toRadians(pointLat - centralLat)
        double longDifference = Math.toRadians(pointLong - centralLong)
        double centralLatInRad = Math.toRadians(centralLat)
        double pointLatInRad = Math.toRadians(pointLat)

        double a = Math.pow(Math.sin(latDifference / 2), 2) + Math.pow(Math.sin(longDifference / 2), 2) * Math.cos(centralLatInRad) * Math.cos(pointLatInRad)
        double c = 2 * Math.asin(Math.sqrt(a))

        return (CIRCUMFERENCE_SEGMENT * c * KM_TO_MILES) <= distance
    }
}
