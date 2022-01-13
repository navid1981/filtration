package com.ustvgo;

import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


public class RestClient {
    public static String URL="https://ustvgo.tv/data.php";

    public static String URL2="https://ustvgo.tv/player.php?stream=BBCAmerica";

    public static String m38u="wmsAuthSign=";

    public String call(String code) throws RestClientException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("stream",code);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        ResponseEntity<String> response =restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);
        System.out.println(response.getBody());
        return response.getBody();
    }

    public String getToken() throws RestClientException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("referer","https://ustvgo.tv/");
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        ResponseEntity<String> response =restTemplate.exchange(URL2, HttpMethod.GET, entity, String.class);
        String output=response.getBody();
        int first=output.indexOf(m38u)+12;
        String result=output.substring(first,first+116);
        return result;
    }
}
