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
        setupHttpClient(co.getProperty('userdata.url', String))
        cityName = co.getProperty('userdata.cityName', String)
    }

    void setupHttpClient(String url) {
        this.client = HttpClient.create(url.toURL())
    }

    List<GUser> user() {
        getUsersInCity(cityName)
    }

    List<GUser> getUsersInCity(String cityName) {
        HttpRequest request = HttpRequest.GET(usersInCityUri(cityName))
        HttpResponse<String> resp = client.toBlocking().exchange(request, String)
        String json = resp.body()
        ObjectMapper objectMapper = new ObjectMapper()
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        List<GUser> gSearchResult = objectMapper.readValue(json, new TypeReference<List<GUser>>(){})

        return gSearchResult
    }

    URI usersInCityUri(String cityName) {
        UriBuilder uriBuilder = UriBuilder.of('/city/' + cityName + '/users')
        uriBuilder.build()
    }


}
