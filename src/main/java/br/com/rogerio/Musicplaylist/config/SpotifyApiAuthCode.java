package br.com.rogerio.Musicplaylist.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class SpotifyApiAuthCode {

  private static final String CONFIG_PATH = "src/main/resources/config.properties";

  private static final String CODE_KEY = "SPOTIFY_AUTHORIZATION_CODE";

  public static String getAuthorizationCode() {
    
    Properties props = new Properties();
    // Find the specified file and add it to the variable
    try (FileInputStream file = new FileInputStream(CONFIG_PATH)) {
      // Loads the file in the properties variable
      props.load(file);
    } catch (IOException e) {
      e.printStackTrace(); 
    }
    // Returns the property assigned to the specified key
    return props.getProperty(CODE_KEY);
  }

}
