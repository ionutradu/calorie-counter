package ro.mpsit.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class SmartwatchService {

    private Map<String, Integer> waitingList = new ConcurrentHashMap<>();
    private Map<String, Integer> pairedWatches = new ConcurrentHashMap<>();

    private RestTemplate restTemplate = new RestTemplate();

    private static final String SMARTWATCH_START_URL = "http://localhost:%d/api/start";
    private static final String SMARTWATCH_STOP_URL = "http://localhost:%d/api/stop";
    private static final String SMARTWATCH_PAIR_URL = "http://localhost:%d/api/pair";

    public void ping(String name, Integer port) {
        waitingList.put(name, port);
    }

    public String pair(String name, String code) {
        if (!waitingList.containsKey(name)) {
            return "Device not recognized.";
        }

        String response = requestPairing(name, code);

        if (response == null) {
            return null;
        }

        pairedWatches.put(name, waitingList.get(name));
        waitingList.remove(name);

        return response;
    }

    public boolean isPaired(String name) {
        return pairedWatches.containsKey(name);
    }

    public void startExercise(String name) {
        String deviceUri = String.format(SMARTWATCH_START_URL, pairedWatches.get(name));

        URI uri = UriComponentsBuilder.fromHttpUrl(deviceUri)
                .build().encode().toUri();

        System.out.println("Requesting starting to " + uri.toString());

        try {
            restTemplate.getForEntity(uri, String.class);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            System.out.println("Couldn't start. " + e.getMessage());
        }
    }

    public void stopExercise(String name) {
        String deviceUri = String.format(SMARTWATCH_STOP_URL, pairedWatches.get(name));

        URI uri = UriComponentsBuilder.fromHttpUrl(deviceUri)
                .build().encode().toUri();

        System.out.println("Requesting stopping to " + uri.toString());

        try {
            restTemplate.getForEntity(uri, String.class);
        } catch (Exception e) {
            System.out.println("Couldn't stop. " + e.getMessage());
        }
    }

    private String requestPairing(String name, String code) {
        String deviceUri = String.format(SMARTWATCH_PAIR_URL, waitingList.get(name));

        URI uri = UriComponentsBuilder.fromHttpUrl(deviceUri)
                .queryParam("code", code)
                .build().encode().toUri();

        System.out.println("Requesting pairing to " + uri.toString());

        ResponseEntity<String> response = null;
        try {
            response = restTemplate.getForEntity(uri, String.class);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            return null;
        }

        return "ok";
    }

    public List<String> getPaired() {
        return pairedWatches.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
    }

    public List<String> getWaitingWatches() {
        return waitingList.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
    }

    public void unpair(String name) {
        pairedWatches.remove(name);
        waitingList.remove(name);
    }
}
