package ro.mpsit.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ro.mpsit.services.CalorieCounterService;
import ro.mpsit.services.Person;
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

    @CrossOrigin
    @RequestMapping(value = "/ping", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> ping(@RequestParam String name, @RequestParam Integer port) {
        smartwatchService.ping(name, port);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/pair", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> pair(@RequestParam String name, @RequestParam String code) {
        String response = smartwatchService.pair(name, code);

        if (response == null) {
            return ResponseEntity.badRequest().body("Pairing code not valid.");
        }

        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/unpair", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> unpair(@RequestParam String name) {
        smartwatchService.unpair(name);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/start", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> start(@RequestParam String name,
                                        @RequestParam Integer age,
                                        @RequestParam Integer weight,
                                        @RequestParam Boolean isMale,
                                        @RequestParam Integer exerciseDuration
                                        ) {
        if (!smartwatchService.isPaired(name)) {
            return ResponseEntity.badRequest().body("Device not paired.");
        }

        calorieCounterService.startExercise(name, age, weight, isMale, exerciseDuration);
        smartwatchService.startExercise(name);

        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/stop", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ExerciseResults> stop(@RequestParam String name) {
        if (!smartwatchService.isPaired(name)) {
            return ResponseEntity.badRequest().body(null);
        }

        ExerciseResults exerciseResults = calorieCounterService.stopExercise(name);

        if (exerciseResults == null) {
            return ResponseEntity.badRequest().body(null);
        }

        smartwatchService.stopExercise(name);

        return new ResponseEntity<>(exerciseResults, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/updateCalories", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateCalories(@RequestParam String name, @RequestParam Double heartRate) {
        if (!smartwatchService.isPaired(name)) {
            return ResponseEntity.badRequest().body("Device not registered.");
        }

        Double calories = calorieCounterService.updateCalories(name, heartRate);

        return new ResponseEntity<>(String.valueOf(calories), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/calories", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ExerciseInformation> calories(@RequestParam String name) {
        ExerciseInformation exerciseInformation = new ExerciseInformation();

        if (!smartwatchService.isPaired(name)) {
            return ResponseEntity.badRequest().body(exerciseInformation);
        }

        exerciseInformation = calorieCounterService.getInformation(name);

        if (exerciseInformation == null) {
            return ResponseEntity.badRequest().body(null);
        }

        System.out.println(name + ": " + exerciseInformation.getCalories() + " " + exerciseInformation.getExercisePercentage() + " " + exerciseInformation.getHeartRate());

        return new ResponseEntity<>(exerciseInformation, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getPairedWatches() {
        List<String> pairedWatches = smartwatchService.getPaired();
        return new ResponseEntity<>(pairedWatches, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/waiting", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getUnpairedWatches() {
        List<String> pairedWatches = smartwatchService.getWaitingWatches();
        return new ResponseEntity<>(pairedWatches, HttpStatus.OK);
    }
}
