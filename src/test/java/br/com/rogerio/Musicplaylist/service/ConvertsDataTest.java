package br.com.rogerio.Musicplaylist.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import br.com.rogerio.Musicplaylist.entity.AccessToken;
import br.com.rogerio.Musicplaylist.entity.TrackSearchResult;

public class ConvertsDataTest {
  @Test
  public void testGetData_ReceiveAcessTokenJsonString_ShouldDeserializeItIntoAccessTokenObject() {
    // Set up test
    ConvertsData convertsData = new ConvertsData();
    String testAcessTokenResponse = "{\"access_token\":\"BQCbdg4Du_F1N5ieyesQvH9CSYH2UbMqocqQRD7-QZV_waBq5WfGIbHfNlg4gByOdTyqKPvtaTGpJKWFlaOJxfbFm12qGGMu8JezDEJcvd3IQtXpe3GQq0Jl4SohNZ4WCWReUc0zJy8\",\"token_type\":\"Bearer\",\"expires_in\":3600}";
    // Method under test
    AccessToken testAccessToken = convertsData.GetData(testAcessTokenResponse, AccessToken.class);
    // Create expected result
    String expectedAccessToken = "BQCbdg4Du_F1N5ieyesQvH9CSYH2UbMqocqQRD7-QZV_waBq5WfGIbHfNlg4gByOdTyqKPvtaTGpJKWFlaOJxfbFm12qGGMu8JezDEJcvd3IQtXpe3GQq0Jl4SohNZ4WCWReUc0zJy8";
    // Verify
    assertEquals(expectedAccessToken, testAccessToken.accessToken());
  }

  @Test
  public void testGatData_ReceiveTrackSearchJsonString_ShouldDeserializeItIntoTrackSearchResultObject() {
    // Set test up
    ConvertsData convertsData = new ConvertsData();
    String trackSearchResult = "{\"tracks\":{\"href\":\"https://api.spotify.com/v1/search?offset=0&limit=1&query=in-the-end&type=track\",\"limit\":1,\"next\":\"https://api.spotify.com/v1/search?offset=1&limit=1&query=in-the-end&type=track\",\"offset\":0,\"previous\":null,\"total\":1000,\"items\":[{\"album\":{\"album_type\":\"album\",\"artists\":[{\"external_urls\":{\"spotify\":\"https://open.spotify.com/artist/6XyY86QOPPrYVGvF9ch6wz\"},\"href\":\"https://api.spotify.com/v1/artists/6XyY86QOPPrYVGvF9ch6wz\",\"id\":\"6XyY86QOPPrYVGvF9ch6wz\",\"name\":\"Linkin Park\",\"type\":\"artist\",\"uri\":\"spotify:artist:6XyY86QOPPrYVGvF9ch6wz\"}],\"available_markets\":[\"AR\",\"AU\",\"AT\",\"BE\",\"BO\",\"BR\",\"BG\",\"CL\",\"CO\",\"CR\",\"CY\",\"CZ\",\"DK\",\"DO\",\"DE\",\"EC\",\"EE\",\"SV\",\"FI\",\"FR\",\"GR\",\"GT\",\"HN\",\"HK\",\"HU\",\"IS\",\"IE\",\"IT\",\"LV\",\"LT\",\"LU\",\"MY\",\"MT\",\"MX\",\"NL\",\"NZ\",\"NI\",\"NO\",\"PA\",\"PY\",\"PE\",\"PH\",\"PL\",\"PT\",\"SG\",\"SK\",\"ES\",\"SE\",\"CH\",\"TW\",\"TR\",\"UY\",\"US\",\"GB\",\"AD\",\"LI\",\"MC\",\"ID\",\"JP\",\"TH\",\"VN\",\"RO\",\"IL\",\"ZA\",\"SA\",\"AE\",\"BH\",\"QA\",\"OM\",\"KW\",\"EG\",\"MA\",\"DZ\",\"TN\",\"LB\",\"JO\",\"PS\",\"IN\",\"BY\",\"KZ\",\"MD\",\"UA\",\"AL\",\"BA\",\"HR\",\"ME\",\"MK\",\"RS\",\"SI\",\"KR\",\"BD\",\"PK\",\"LK\",\"GH\",\"KE\",\"NG\",\"TZ\",\"UG\",\"AG\",\"AM\",\"BS\",\"BB\",\"BZ\",\"BT\",\"BW\",\"BF\",\"CV\",\"CW\",\"DM\",\"FJ\",\"GM\",\"GE\",\"GD\",\"GW\",\"GY\",\"HT\",\"JM\",\"KI\",\"LS\",\"LR\",\"MW\",\"MV\",\"ML\",\"MH\",\"FM\",\"NA\",\"NR\",\"NE\",\"PW\",\"PG\",\"WS\",\"SM\",\"ST\",\"SN\",\"SC\",\"SL\",\"SB\",\"KN\",\"LC\",\"VC\",\"SR\",\"TL\",\"TO\",\"TT\",\"TV\",\"VU\",\"AZ\",\"BN\",\"BI\",\"KH\",\"CM\",\"TD\",\"KM\",\"GQ\",\"SZ\",\"GA\",\"GN\",\"KG\",\"LA\",\"MO\",\"MR\",\"MN\",\"NP\",\"RW\",\"TG\",\"UZ\",\"ZW\",\"BJ\",\"MG\",\"MU\",\"MZ\",\"AO\",\"CI\",\"DJ\",\"ZM\",\"CD\",\"CG\",\"IQ\",\"LY\",\"TJ\",\"VE\",\"ET\",\"XK\"],\"external_urls\":{\"spotify\":\"https://open.spotify.com/album/6hPkbAV3ZXpGZBGUvL6jVM\"},\"href\":\"https://api.spotify.com/v1/albums/6hPkbAV3ZXpGZBGUvL6jVM\",\"id\":\"6hPkbAV3ZXpGZBGUvL6jVM\",\"images\":[{\"height\":640,\"width\":640,\"url\":\"https://i.scdn.co/image/ab67616d0000b273e2f039481babe23658fc719a\"},{\"height\":300,\"width\":300,\"url\":\"https://i.scdn.co/image/ab67616d00001e02e2f039481babe23658fc719a\"},{\"height\":64,\"width\":64,\"url\":\"https://i.scdn.co/image/ab67616d00004851e2f039481babe23658fc719a\"}],\"is_playable\":true,\"name\":\"Hybrid Theory (Bonus Edition)\",\"release_date\":\"2000-10-24\",\"release_date_precision\":\"day\",\"total_tracks\":15,\"type\":\"album\",\"uri\":\"spotify:album:6hPkbAV3ZXpGZBGUvL6jVM\"},\"artists\":[{\"external_urls\":{\"spotify\":\"https://open.spotify.com/artist/6XyY86QOPPrYVGvF9ch6wz\"},\"href\":\"https://api.spotify.com/v1/artists/6XyY86QOPPrYVGvF9ch6wz\",\"id\":\"6XyY86QOPPrYVGvF9ch6wz\",\"name\":\"Linkin Park\",\"type\":\"artist\",\"uri\":\"spotify:artist:6XyY86QOPPrYVGvF9ch6wz\"}],\"available_markets\":[\"AR\",\"AU\",\"AT\",\"BE\",\"BO\",\"BR\",\"BG\",\"CL\",\"CO\",\"CR\",\"CY\",\"CZ\",\"DK\",\"DO\",\"DE\",\"EC\",\"EE\",\"SV\",\"FI\",\"FR\",\"GR\",\"GT\",\"HN\",\"HK\",\"HU\",\"IS\",\"IE\",\"IT\",\"LV\",\"LT\",\"LU\",\"MY\",\"MT\",\"MX\",\"NL\",\"NZ\",\"NI\",\"NO\",\"PA\",\"PY\",\"PE\",\"PH\",\"PL\",\"PT\",\"SG\",\"SK\",\"ES\",\"SE\",\"CH\",\"TW\",\"TR\",\"UY\",\"US\",\"GB\",\"AD\",\"LI\",\"MC\",\"ID\",\"JP\",\"TH\",\"VN\",\"RO\",\"IL\",\"ZA\",\"SA\",\"AE\",\"BH\",\"QA\",\"OM\",\"KW\",\"EG\",\"MA\",\"DZ\",\"TN\",\"LB\",\"JO\",\"PS\",\"IN\",\"BY\",\"KZ\",\"MD\",\"UA\",\"AL\",\"BA\",\"HR\",\"ME\",\"MK\",\"RS\",\"SI\",\"KR\",\"BD\",\"PK\",\"LK\",\"GH\",\"KE\",\"NG\",\"TZ\",\"UG\",\"AG\",\"AM\",\"BS\",\"BB\",\"BZ\",\"BT\",\"BW\",\"BF\",\"CV\",\"CW\",\"DM\",\"FJ\",\"GM\",\"GE\",\"GD\",\"GW\",\"GY\",\"HT\",\"JM\",\"KI\",\"LS\",\"LR\",\"MW\",\"MV\",\"ML\",\"MH\",\"FM\",\"NA\",\"NR\",\"NE\",\"PW\",\"PG\",\"WS\",\"SM\",\"ST\",\"SN\",\"SC\",\"SL\",\"SB\",\"KN\",\"LC\",\"VC\",\"SR\",\"TL\",\"TO\",\"TT\",\"TV\",\"VU\",\"AZ\",\"BN\",\"BI\",\"KH\",\"CM\",\"TD\",\"KM\",\"GQ\",\"SZ\",\"GA\",\"GN\",\"KG\",\"LA\",\"MO\",\"MR\",\"MN\",\"NP\",\"RW\",\"TG\",\"UZ\",\"ZW\",\"BJ\",\"MG\",\"MU\",\"MZ\",\"AO\",\"CI\",\"DJ\",\"ZM\",\"CD\",\"CG\",\"IQ\",\"LY\",\"TJ\",\"VE\",\"ET\",\"XK\"],\"disc_number\":1,\"duration_ms\":216880,\"explicit\":false,\"external_ids\":{\"isrc\":\"USWB10002407\"},\"external_urls\":{\"spotify\":\"https://open.spotify.com/track/60a0Rd6pjrkxjPbaKzXjfq\"},\"href\":\"https://api.spotify.com/v1/tracks/60a0Rd6pjrkxjPbaKzXjfq\",\"id\":\"60a0Rd6pjrkxjPbaKzXjfq\",\"is_local\":false,\"is_playable\":true,\"name\":\"In the End\",\"popularity\":92,\"preview_url\":null,\"track_number\":8,\"type\":\"track\",\"uri\":\"spotify:track:60a0Rd6pjrkxjPbaKzXjfq\"}]}}";
    // Method under test
    TrackSearchResult testTrackSearchResult = convertsData.GetData(trackSearchResult, TrackSearchResult.class);
    // Create expected result
    String expectedTrackName = "In the End";
    String expectedArtistName = "Linkin Park";
    String expectedAlbumName = "Hybrid Theory (Bonus Edition)";
    // Verify
    assertEquals(expectedTrackName, testTrackSearchResult.getPlaylist()
      .getMusics().get(0).getName());
    assertEquals(expectedArtistName, testTrackSearchResult.getPlaylist()
      .getMusics().get(0).getArtists().get(0).getName());
    assertEquals(expectedAlbumName, testTrackSearchResult.getPlaylist()
      .getMusics().get(0).getAlbum().getName());
  }
}
