package ro.mpsit.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ro.mpsit.services.CalorieCounterService;
import ro.mpsit.services.SmartwatchService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api/smartwatch")
public class SmartwatchResource {

    @Autowired
    private SmartwatchService smartwatchService;

    @Autowired
    private CalorieCounterService calorieCounterService;

    @RequestMapping(value = "/ping", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> ping(@RequestParam String name, @RequestParam Integer port) {
        smartwatchService.ping(name, port);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @RequestMapping(value = "/pair", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> pair(@RequestParam String name, @RequestParam String code) {
        String response = smartwatchService.pair(name, code);

        if (response == null) {
            return ResponseEntity.badRequest().body("Couldn't pair devices.");
        }

        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @RequestMapping(value = "/unpair", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> unpair(@RequestParam String name) {
        smartwatchService.unpair(name);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @RequestMapping(value = "/start", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> start(@RequestParam String name,
                                        @RequestParam Integer age,
                                        @RequestParam Integer weight,
                                        @RequestParam Boolean isMale
                                        ) {
        if (!smartwatchService.isPaired(name)) {
            return ResponseEntity.badRequest().body("Device not registered.");
        }

        calorieCounterService.startCounter(name, age, weight, isMale);

        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @RequestMapping(method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getPairedWatches() {
        List<String> pairedWatches = smartwatchService.getPaired();
        return new ResponseEntity<>(pairedWatches, HttpStatus.OK);
    }
}
