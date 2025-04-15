package com.quan.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.regex.Pattern;


@Service
@Slf4j
public class OSMGeocodingService {

    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search?format=json&q=";

    public double[] getLatLngFromAddress(String address) {
        String normalized = Normalizer.normalize(address, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String processedAddress= pattern.matcher(normalized).replaceAll("");
        // Thay thế riêng chữ "Đ" thành "D" và "đ" thành "d"
        processedAddress = processedAddress.replace("Đ", "D").replace("đ", "d");

//        processedAddress = processedAddress.replaceAll("[^a-zA-Z0-9\\s]", " "); // Xóa các ký tự đặc biệt
        processedAddress = processedAddress.replaceAll("\\s+", " ").trim(); // Xóa khoảng trắng thừa



        log.warn("address: %s, processedAddress: %s"+ address+" "+ processedAddress);


        String encodedAddress = URLEncoder.encode(processedAddress, StandardCharsets.UTF_8);

        String url = NOMINATIM_URL + encodedAddress;

        log.info("URL: " + url);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0");
        headers.set("Accept-Charset", "UTF-8");


        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        String response = responseEntity.getBody();
        System.out.println("Response từ API: " + response);


        JSONArray jsonArray = new JSONArray(response);
        if (jsonArray.length() == 0) {
            return new double[]{0, 0}; // Không tìm thấy địa chỉ
        }

        JSONObject location = jsonArray.getJSONObject(0);
        double lat = location.getDouble("lat");
        double lon = location.getDouble("lon");
        log.warn(response.toString());

        return new double[]{lat, lon};
    }
}




