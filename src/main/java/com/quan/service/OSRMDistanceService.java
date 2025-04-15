package com.quan.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

@Service
@Slf4j
public class OSRMDistanceService {

    private static final String OSRM_URL = "http://router.project-osrm.org/route/v1/driving/";

    public double[] getDistanceAndTime(double lat1, double lon1, double lat2, double lon2) {
        String url = OSRM_URL + lon1 + "," + lat1 + ";" + lon2 + "," + lat2 + "?overview=false";
        log.warn("URL: ", url);
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        JSONObject json = new JSONObject(response);
        JSONObject route = json.getJSONArray("routes").getJSONObject(0);

        double distance = route.getDouble("distance") / 1000; // Đổi sang km
        double duration = route.getDouble("duration") / 60; // Đổi sang phút

        log.warn(String.format("Distance: %.2f Minutes: %.2f", distance, duration));

        return new double[]{distance, duration};
    }
}
