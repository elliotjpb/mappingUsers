package org.userdata;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class User implements Comparable<User>{

    @JsonProperty("id")
    private final Long id;

    @JsonProperty("first_name")
    private final String firstName;

    @JsonProperty("last_name")
    private final String lastName;

    @JsonProperty("email")
    private final String email;

    @JsonProperty("ip_address")
    private final String ipAddress;

    @JsonProperty("latitude")
    private final double latitude;

    @JsonProperty("longitude")
    private final double longitude;

    @JsonProperty("city")
    private final String city;

    @JsonCreator
    public User(@JsonProperty("id") Long id,
                @JsonProperty("first_name") String firstName,
                @JsonProperty("last_name") String lastName,
                @JsonProperty("email") String email,
                @JsonProperty("ip_address") String ipAddress,
                @JsonProperty("latitude") double latitude,
                @JsonProperty("longitude") double longitude,
                @JsonProperty("city") String city) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.ipAddress = ipAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
    }

    @Override
    public int compareTo(User o) {
        return this.id.compareTo(o.id);
    }
}
