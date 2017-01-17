package ro.mpsit.services;

import org.springframework.stereotype.Service;
import ro.mpsit.dto.ExerciseInformation;
import ro.mpsit.dto.ExerciseResults;
import ro.mpsit.dto.Person;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class CalorieCounterService {

    private Map<String, Person> currentActivities = new ConcurrentHashMap<>();

    private static final int HOUR_IN_MS = 60 * 60 * 1000;


    public void startExercise(String name, Integer age, Integer weight, Boolean isMale, Integer exerciseDurationInMinutes) {
        Person person = new Person();
        person.setAge(age);
        person.setWeight(weight);
        person.setMale(isMale);
        person.setStartExerciseDate(System.currentTimeMillis());
        person.setCalories(0.0);
        person.setExerciseDurationInMs(exerciseDurationInMinutes * 60 * 1000L);

        currentActivities.put(name, person);
    }

    public ExerciseResults stopExercise(String name) {
        if (!currentActivities.containsKey(name)) {
            return null;
        }

        Person person = currentActivities.remove(name);
        ExerciseResults exerciseResults = new ExerciseResults();

        exerciseResults.setCalories(String.valueOf(person.getCalories()));

        long exerciseTimeInMs = System.currentTimeMillis() - person.getStartExerciseDate();

        exerciseResults.setCaloriesPerMinute(String.valueOf(person.getCalories() / (exerciseTimeInMs / 60000.0)));

        String formattedExerciseTime = "";

        if (TimeUnit.MILLISECONDS.toMinutes(exerciseTimeInMs) == 0) {
            formattedExerciseTime += String.format("%2d seconds", TimeUnit.MILLISECONDS.toSeconds(exerciseTimeInMs));
        } else {
            formattedExerciseTime += String.format("%2d minutes, %2d seconds",
                    TimeUnit.MILLISECONDS.toMinutes(exerciseTimeInMs),
                    TimeUnit.MILLISECONDS.toSeconds(exerciseTimeInMs) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(exerciseTimeInMs)));
        }

        exerciseResults.setTime(formattedExerciseTime);
        exerciseResults.setAverageHeartRate(String.valueOf(person.getAverageHeartRate()));

        return exerciseResults;
    }

    public Double updateCalories(String name, Double heartRate) {
        if (!currentActivities.containsKey(name)) {
            return null;
        }

        Person person = currentActivities.get(name);

        Double calories = null;

        if (person.getMale()) {
            calories = getMaleCalories(heartRate, person);
        } else {
            calories = getFemaleCalories(heartRate, person);
        }

        person.setCalories(calories);
        person.setHeartRate(heartRate);

        currentActivities.put(name, person);

        return calories;
    }

    public ExerciseInformation getInformation(String name) {
        if (!currentActivities.containsKey(name)) {
            return null;
        }

        Person person = currentActivities.get(name);

        ExerciseInformation exerciseInformation = new ExerciseInformation();
        exerciseInformation.setCalories(String.valueOf(person.getCalories()));
        exerciseInformation.setExercisePercentage(String.valueOf(getPercentage(person)));
        exerciseInformation.setHeartRate(String.valueOf(person.getHeartRate()));

        if (100d - getPercentage(person) < -0.4d) {
            return null;
        }

        return exerciseInformation;
    }

    private Double getPercentage(Person person) {
        double exerciseTimeInMs = (System.currentTimeMillis() - person.getStartExerciseDate()) * 1.0;
        double percentage = exerciseTimeInMs / person.getExerciseDurationInMs();
        return percentage * 10000 / 100;
    }

    private Double getMaleCalories(Double heartRate, Person person) {
        return ((-55.0969 + (0.6309 * heartRate) + (0.1988 * person.getWeight()) + (0.2017 * person.getAge())) / 4.184) * 60 *
                getExerciseTimeInHours(person.getStartExerciseDate());
    }

    private Double getFemaleCalories(Double heartRate, Person person) {
        return ((-20.4022 + (0.4472 * heartRate) + (0.1263 * person.getWeight()) + (0.074 * person.getAge())) / 4.184) * 60 *
                getExerciseTimeInHours(person.getStartExerciseDate());
    }

    private double getExerciseTimeInHours(Long startExerciseDate) {
        return (System.currentTimeMillis() - startExerciseDate) * 1.0 / HOUR_IN_MS * 1.0;
    }
}
