package ro.mpsit.services;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CalorieCounterService {

    private Map<String, Person> currentActivities = new ConcurrentHashMap<>();

    private static final int HOUR_IN_MS = 60 * 60 * 1000;


    @PostConstruct
    public void init() throws InterruptedException {
        Person person = new Person();
        person.setAge(23);
        person.setWeight(60);
        person.setMale(true);
        person.setStartExerciseDate(System.currentTimeMillis());

        Thread.sleep(60000);

        System.out.println(getExerciseTimeInHours(person.getStartExerciseDate()));
    }

    public void startCounter(String name, Integer age, Integer weight, Boolean isMale) {
        Person person = new Person();
        person.setAge(age);
        person.setWeight(weight);
        person.setMale(isMale);
        person.setStartExerciseDate(System.currentTimeMillis());

        currentActivities.put(name, person);
    }

    public Double getCalories(String name, Integer heartRate) {
        if (!currentActivities.containsKey(name)) {
            return null;
        }

        Person person = currentActivities.get(name);

        if (person.getMale()) {
            return getMaleCalories(heartRate, person);
        }

        return getFemaleCalories(heartRate, person);
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
        return (System.currentTimeMillis() - startExerciseDate) / HOUR_IN_MS;
    }
}
