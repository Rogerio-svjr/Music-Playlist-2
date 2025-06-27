package br.com.rogerio.Musicplaylist.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AccessToken(
  @JsonAlias("access_token") String accessToken, 
  @JsonAlias("expires_in") Short expiresIn
) {}
