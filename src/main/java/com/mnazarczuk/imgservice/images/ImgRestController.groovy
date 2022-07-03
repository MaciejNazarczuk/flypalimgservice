package com.mnazarczuk.imgservice.images;

import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import org.springframework.http.MediaType
import javax.imageio.ImageIO


@RestController()
public class ImgRestController {

    @Autowired
    ImgRepo imgRepo

    @GetMapping(
            value = "/api/image",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    @ResponseBody
    byte[] downloadFile(@RequestParam String code, @RequestParam String city) throws IOException, InterruptedException {
        System.out.println();
        System.out.println("incoming request" + code + " " + city);


        def byCode = imgRepo.findFirstByCode(code)
        if (byCode.empty) {
            println(code + " not found in DB")

            String query = "$city $code airport"
            def encoded = URLEncoder.encode(query, StandardCharsets.UTF_8)
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://bing-image-search1.p.rapidapi.com/images/search?q=" + encoded))
                    .header("X-RapidAPI-Key", "")
                    .header("X-RapidAPI-Host", "bing-image-search1.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());


            def object = new JsonSlurper().parseText(response.body())


            while (true) {

                def photoIndex = new Random().nextInt(7)
                println object.value
                def url = object.value[photoIndex].contentUrl
                System.out.println(url);

                HttpRequest request2 = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .method("GET", HttpRequest.BodyPublishers.noBody())
                        .build();
                HttpResponse<byte[]> imgResponse = HttpClient.newHttpClient().send(request2, HttpResponse.BodyHandlers.ofByteArray());

                if( ImageIO.read(new ByteArrayInputStream(imgResponse.body())) != null){
                    Image imgToSave = new Image(imgResponse.body(), code)
                    imgRepo.save(imgToSave)

                    return imgToSave.getData()
                }
            }

        } else {
            println(code + " successfully found in DB")
            return byCode.get().getData()
        }


//        return imgResponse.body()
    }

}
