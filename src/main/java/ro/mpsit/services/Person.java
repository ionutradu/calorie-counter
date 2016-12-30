package ro.mpsit.services;

public class Person {

    private Integer weight;
    private Integer age;
    private Long startExerciseDate;
    private Boolean male;

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
}
