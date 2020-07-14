package org.mappingusers

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.CompileStatic

@CompileStatic
class User {
    Long id
    String first_name
    String last_name
    String email
    String ip_address
    double latitude
    double longitude
    String city
}
