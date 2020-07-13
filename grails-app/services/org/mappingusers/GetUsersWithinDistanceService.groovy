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
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.uri.UriBuilder

@Transactional
class GetUsersWithinDistanceService implements GrailsConfigurationAware {

    HttpClient client
    double centralLat
    double centralLong
    double distance

    @Override
    void setConfiguration(Config co) {
        setupHttpClient(co.getProperty('bpdts-test-app.url', String))
        this.centralLat = Double.parseDouble(co.getProperty('bpdts-test-app.city.latitude'))
        this.centralLong = Double.parseDouble(co.getProperty('bpdts-test-app.city.longitude'))
        this.distance = Double.parseDouble(co.getProperty('bpdts-test-app.distance'))
    }

    void setupHttpClient(String url) {
        this.client = HttpClient.create(url.toURL())
    }

    URI usersWithinDistanceUri() {
        UriBuilder uriBuilder = UriBuilder.of('/users')
        uriBuilder.build()
    }

    /**
     * Performs get request /users
     * @return The list of users within distance of city
     */

    List<User> allUsers() {
        try {
            HttpRequest request = HttpRequest.GET(usersWithinDistanceUri())
            HttpResponse<String> resp = client.toBlocking().exchange(request, String)
            String json = resp.body()
            ObjectMapper objectMapper = new ObjectMapper()
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            List<User> allUsers = objectMapper.readValue(json, new TypeReference<List<User>>(){})
            getUsersWithinDistance(allUsers)

        } catch (HttpClientResponseException e){
            return null
        }

    }

    /**
     *
     * @param allUsers Takes in all users from request
     * @return sub-set of users with users currently within 50 miles of London
     */
    List<User> getUsersWithinDistance(List<User> allUsers) {

        List<User> withinDistance = new ArrayList<>()

        for (User g : allUsers) {
            if (harversineDistance(centralLat, centralLong, g.latitude, g.longitude, distance)) {
                withinDistance.add(g)
            }
        }
        return withinDistance
    }


    /**
     * Haversine formula - https://rosettacode.org/wiki/Haversine_formula
     * Calculates distances from two points on a sphere from their longitudes and latitudes.
     * @param centralLat - centre of circle {'bpdts-test-app.city.latitude'}
     * @param centralLong -centre of circle {'bpdts-test-app.city.longitude'}
     * @param pointLat - lat from data in object
     * @param pointLong - long from data in object
     * @param distance - {'bpdts-test-app.distance'}
     * @return boolean - if lat and long are within the distance
     */

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
