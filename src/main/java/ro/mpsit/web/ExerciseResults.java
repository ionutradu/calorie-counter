package ro.mpsit.web;

public class ExerciseResults {

    private String calories;
    private String caloriesPerMinute;
    private String time;
    private String averageHeartRate;

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getCaloriesPerMinute() {
        return caloriesPerMinute;
    }

    public void setCaloriesPerMinute(String caloriesPerMinute) {
        this.caloriesPerMinute = caloriesPerMinute;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAverageHeartRate() {
        return averageHeartRate;
    }

    public void setAverageHeartRate(String averageHeartRate) {
        this.averageHeartRate = averageHeartRate;
    }
}
