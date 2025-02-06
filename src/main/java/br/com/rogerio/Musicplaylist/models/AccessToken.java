package br.com.rogerio.Musicplaylist.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AccessToken(@JsonAlias("access_token") String accessToken) {

}
