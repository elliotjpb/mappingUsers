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
class GetUsersInCityService implements GrailsConfigurationAware {

    String cityName
    HttpClient client

    @Override
    void setConfiguration(Config co) {
        setupHttpClient(co.getProperty('bpdts-test-app.url', String))
        cityName = co.getProperty('bpdts-test-app.cityName', String)
    }

    void setupHttpClient(String url) {
        this.client = HttpClient.create(url.toURL())
    }

    List<User> user() {
        getUsersInCity(cityName)
    }

    List<User> getUsersInCity(String cityName) {
        HttpRequest request = HttpRequest.GET(usersInCityUri(cityName))
        HttpResponse<String> resp = client.toBlocking().exchange(request, String)
        String json = resp.body()
        System.out.println(json)
        ObjectMapper objectMapper = new ObjectMapper()
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        List<User> usersInCityResult = objectMapper.readValue(json, new TypeReference<List<User>>(){})

        return usersInCityResult
    }

    URI usersInCityUri(String cityName) {
        UriBuilder uriBuilder = UriBuilder.of('/city/' + cityName + '/users')
        uriBuilder.build()
    }


}
