package br.com.rogerio.Musicplaylist.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ErrorTrackRequest (
  int status,
  String message
) {}
