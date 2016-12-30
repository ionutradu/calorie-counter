package ro.mpsit.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class SmartwatchService {

    private Map<String, Integer> waitingList = new ConcurrentHashMap<>();
    private Map<String, Integer> pairedWatches = new ConcurrentHashMap<>();

    public void ping(String name) {
        waitingList.put(name, 0);
    }

    public String pair(String name, Integer port) {
        if (!waitingList.containsKey(name)) {
            return "Device not recognized.";
        }

        waitingList.remove(name);
        pairedWatches.put(name, port);

        return "ok";
    }

    public List<String> getPaired() {
        return pairedWatches.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
    }

    public void unpair(String name) {
        pairedWatches.remove(name);
    }
}
