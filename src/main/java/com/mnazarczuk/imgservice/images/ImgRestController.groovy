package com.mnazarczuk.imgservice.images;

import groovy.json.JsonSlurper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;


@RestController()
public class ImgRestController {

    @GetMapping("/api/image")
    public String test123(@RequestParam  String code, @RequestParam  String city) throws IOException, InterruptedException {
        System.out.println(code);
        System.out.println(city);
        String query = "$city $code airport"
        def encoded = URLEncoder.encode(query, StandardCharsets.UTF_8)
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://bing-image-search1.p.rapidapi.com/images/search?q=" + encoded))
            .header("X-RapidAPI-Key", "e8a32ef5cbmshaa1803051b3af29p15ce7fjsn4f04004a4a56")
            .header("X-RapidAPI-Host", "bing-image-search1.p.rapidapi.com")
            .method("GET", HttpRequest.BodyPublishers.noBody())
            .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        def object = new JsonSlurper().parseText(response.body())
        def url = object.value[0].contentUrl
        System.out.println(url);
        return  url;
    }
}
