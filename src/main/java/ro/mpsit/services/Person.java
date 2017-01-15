package ro.mpsit.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Person {

    private Integer weight;
    private Integer age;
    private Long startExerciseDate;
    private Boolean male;
    private Double calories;
    private Long exerciseDurationInMs;
    private Double heartRate;

    private List<Double> heartRates = new ArrayList<>();

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Long getStartExerciseDate() {
        return startExerciseDate;
    }

    public void setStartExerciseDate(Long startExerciseDate) {
        this.startExerciseDate = startExerciseDate;
    }

    public void setMale(Boolean male) {
        this.male = male;
    }

    public Boolean getMale() {
        return male;
    }

    public Double getCalories() {
        if (calories < 0) {
            return 0d;
        }

        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public Long getExerciseDurationInMs() {
        return exerciseDurationInMs;
    }

    public void setExerciseDurationInMs(Long exerciseDurationInMs) {
        this.exerciseDurationInMs = exerciseDurationInMs;
    }

    public void setHeartRate(Double heartRate) {
        this.heartRate = heartRate;
        this.heartRates.add(heartRate);
    }

    public Double getAverageHeartRate() {
        return heartRates.stream().collect(Collectors.averagingDouble(n -> n));
    }

    public Double getHeartRate() {
        return heartRate;
    }
}
