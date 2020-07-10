package org.mappingusers

import grails.gorm.transactions.Transactional

import grails.config.Config
import grails.core.support.GrailsConfigurationAware
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.uri.UriBuilder
import org.grails.web.json.JSONObject
import org.springframework.web.client.RestTemplate
import org.userdata.User

@CompileStatic
class UserdataService implements GrailsConfigurationAware {

    String cityName
    BlockingHttpClient client

    @Override
    void setConfiguration(Config co) {
        setupHttpClient(co.getProperty('userdata.url', String))
        //cityName = co.getProperty('userdata.cityName', String)
        System.out.println("URL: " + co.getProperty('userdata.url', String))
       // System.out.println("CityName: " + cityName)
    }

    void setupHttpClient(String url) {
        this.client = HttpClient.create(url.toURL()).toBlocking()
    }

    User user() {
        try {
            HttpRequest request = HttpRequest.GET(userDataUri())
            return client.retrieve(request, User)
        } catch (HttpClientResponseException e) {
            System.out.println("Unable to retrieve: " + e)
            return null
        }
    }

    URI userDataUri() {
        UriBuilder uriBuilder = UriBuilder.of('/user/1')
        uriBuilder.build()
    }
}
