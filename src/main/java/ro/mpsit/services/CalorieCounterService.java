package ro.mpsit.services;

import org.springframework.stereotype.Service;
import ro.mpsit.web.ExerciseInformation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CalorieCounterService {

    private Map<String, Person> currentActivities = new ConcurrentHashMap<>();

    private static final int HOUR_IN_MS = 60 * 60 * 1000;


//    @PostConstruct
    public void init() throws InterruptedException {
        Person person = new Person();
        person.setAge(23);
        person.setWeight(60);
        person.setMale(true);
        person.setStartExerciseDate(System.currentTimeMillis());

        currentActivities.put("test", person);

        int heartRate = 80;
        while (true) {
            System.out.println(updateCalories("test", heartRate));
            Thread.sleep(5000);
        }

    }

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

    public void stopExercise(String name) {
        currentActivities.remove(name);
    }

    public Double updateCalories(String name, Integer heartRate) {
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

        currentActivities.put(name, person);

        return calories;
    }

    public ExerciseInformation getInformation(String name) {
        if (!currentActivities.containsKey(name)) {
            return null;
        }

        Person person = currentActivities.get(name);

        ExerciseInformation exerciseInformation = new ExerciseInformation();
        exerciseInformation.setCalories(String.valueOf(person.getCalories() + 2.0));
        exerciseInformation.setExercisePercentage(getPercentage(person));
        return exerciseInformation;
    }

    private String getPercentage(Person person) {
        double exerciseTimeInMs = (System.currentTimeMillis() - person.getStartExerciseDate()) * 1.0;
        double percentage = exerciseTimeInMs / person.getExerciseDurationInMs();
        return String.valueOf((percentage * 10000 / 100));
    }

    private Double getMaleCalories(Integer heartRate, Person person) {
        return ((-55.0969 + (0.6309 * heartRate) + (0.1988 * person.getWeight()) + (0.2017 * person.getAge())) / 4.184) * 60 *
                getExerciseTimeInHours(person.getStartExerciseDate());
    }

    private Double getFemaleCalories(Integer heartRate, Person person) {
        return ((-20.4022 + (0.4472 * heartRate) + (0.1263 * person.getWeight()) + (0.074 * person.getAge())) / 4.184) * 60 *
                getExerciseTimeInHours(person.getStartExerciseDate());
    }

    private double getExerciseTimeInHours(Long startExerciseDate) {
        return (System.currentTimeMillis() - startExerciseDate) * 1.0 / HOUR_IN_MS * 1.0;
    }
}
