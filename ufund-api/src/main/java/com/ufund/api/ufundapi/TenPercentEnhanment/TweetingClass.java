package com.ufund.api.ufundapi.TenPercentEnhanment;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.ufund.api.ufundapi.model.Need;

public class TweetingClass {
    private HttpClient client;
    private String postURL;
    private Twitter twitter;
    private String previousTitle;

    private String title;

    public TweetingClass(){
        this.client = HttpClient.newHttpClient();
        this.postURL = "https://api.twitter.com/2/tweets";
        this.previousTitle = "";
        this.twitter = Twitter.newBuilder()
                .prettyDebugEnabled(true)
                .oAuthConsumer("fmUDdUQjEiDb93bk9cMQAwNYV","ZfzTDkeuE5KuizupYiPy6Qv4X2vUw6W1SXnRS5UCgaGW6VJy1n")
                .oAuthAccessToken("1672949931013709825-fBmwJL5m9ovIOoQn73EfmXUoPcyfjD","O46APlAceZA6homKQZONUiknX8f9nzhqnwNhKIFXOlY5E")
                .build();
        
    }
    public HttpRequest makeRequest(Need need) throws IOException, InterruptedException, TwitterException {

        
        /*ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.body());
        JsonNode next = jsonNode.get("data");
        JsonNode last = next.get(0);
        title = last.get("title").asText();
        JsonNode artist = last.get("artist");
        String artistName = artist.get("name").asText();
        JsonNode album = last.get("album");
        String cover = album.get("cover_big").asText();
        URL url = new URL(cover);

        Long mediaID = uploadFile(url);
        var requestNonce = UUID.randomUUID().toString().replace("-", "");
        var time = System.currentTimeMillis() / 1000L;*/

        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "OAuth oauth_consumer_key=\"fmUDdUQjEiDb93bk9cMQAwNYV\",oauth_token=\"1672949931013709825-fBmwJL5m9ovIOoQn73EfmXUoPcyfjD\",oauth_signature_method=\"HMAC-SHA1\",oauth_timestamp=\"1699972867\",oauth_nonce=\"Jdt52Syvu6O\",oauth_version=\"1.0\",oauth_signature=\"Rc83Q5pvbVis2Oqxn34FTAB2f4s%3D\"")
                .uri(URI.create(this.postURL))
                .header("Content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"text\":\"We need help funding Need: " + need.getName() + " || Cost: " + need.getCost() + " || Quantity: " + need.getQuantity() + "\"}"))
                .build();


        previousTitle = title;
        return request;
    }
    public HttpClient getClient(){
        return this.client;
    }
}
