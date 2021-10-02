package com.nutritious.camera;

import android.util.Log;

import java.io.*;

public class OCR {
    static String text = "";
    int[] totalValues = new int[7];

    public double findDailyIntake(double weight, double height, char sex, int age) {
        double BMR;
        if (sex == 'M' || sex == 'm') {
            BMR = (height * 2.54 * 6.25) + (weight / 2.2 * 9.99) - (age * 4.92) + 5;
        } else {
            BMR = (height * 2.54 * 6.25) + (weight / 2.2 * 9.99) - (age * 4.92) - 161;
        }
        return BMR * 1.55;
    }

    public int findCalories(String label) {
        int location = label.indexOf("Calories") + 9;
        int space = label.indexOf(" ", location);
        if (space < 0)
            space = label.indexOf(System.getProperty("line.separator"), location);

        String calories = label.substring(location, space);
        if (calories.equalsIgnoreCase("o"))
            calories = "0";
        int c = Integer.parseInt(calories);
        return c;
    }

    public String calorieResponse(char meal, double weight, double height, char sex, int age, String label) {
        String response = "";
        double dailyIntake = findDailyIntake(weight, height, sex, age);
        if (meal == 'B' || meal == 'b') {
            if (findCalories(label) > (.42 * dailyIntake)) {
                response = "This meal/snack is HIGH in calories, by " + (int) (findCalories(label) - dailyIntake * .42)
                        + " calories.";
            } else
                response = "You are " + (int) (dailyIntake * .42 - findCalories(label))
                        + " calories underneath the meal limit.";
        } else if (meal == 'L' || meal == 'l') {
            if (findCalories(label) > (.34 * dailyIntake)) {
                response = "This meal/snack is HIGH in calories, by " + (int) (findCalories(label) - dailyIntake * .34)
                        + " calories.";

            } else
                response = "You are " + ((int) dailyIntake * .34 - findCalories(label))
                        + " calories underneath the meal limit.";
        } else if (meal == 'D' || meal == 'd') {
            if (findCalories(label) > (.42 * dailyIntake)) {
                response = "This meal/snack is HIGH in calories, by " + (int) (findCalories(label) - dailyIntake * .42)
                        + " calories.";
            } else
                response = "You are " + (int) (dailyIntake * .42 - findCalories(label))
                        + " calories underneath the meal limit.";

        } else if (meal == 'S' || meal == 's') {
            if (findCalories(label) > .18 * dailyIntake) {
                response = "This meal/snack is HIGH in calories, by " + (int) (findCalories(label) - dailyIntake * .18)
                        + " calories.";
            } else
                response = "You are " + (int) (dailyIntake * .18 - findCalories(label))
                        + " calories underneath the meal limit.";

        }

        totalValues[0] += findCalories(label);
        return response;
    }

    public String totalFatResponse(char meal, double weight, double height, char sex, int age, String label) {
        String response = "";
        double dailyIntake = findDailyIntake(weight, height, sex, age);
        if (meal == 'B' || meal == 'b') {
            if (findTotalFat(label) > (.42 * dailyIntake * findCalories(label) * 0.30 / 9)) {
                response = "This meal/snack is HIGH in total fat.";
            } else
                response = "You meal/snack has a healthy amount of fat.";
        } else if (meal == 'L' || meal == 'l') {
            if (findTotalFat(label) > (.34 * dailyIntake * findCalories(label) * 0.30 / 9)) {
                response = "This meal/snack is HIGH in total fat.";
            } else
                response = "You meal/snack has a healthy amount of fat.";
        } else if (meal == 'D' || meal == 'd') {
            if (findTotalFat(label) > (.42 * dailyIntake * findCalories(label) * 0.30 / 9)) {
                response = "This meal/snack is HIGH in total fat.";
            } else
                response = "You meal/snack has a healthy amount of fat.";

        } else if (meal == 'S' || meal == 's') {
            if (findTotalFat(label) > (.18 * dailyIntake * findCalories(label) * 0.30 / 9)) {
                response = "This meal/snack is HIGH in total fat.";
            } else
                response = "You meal/snack has a healthy amount of fat.";
        }

        totalValues[1] += findTotalFat(label);
        return response;
    }

    public String saturatedFatResponse(char meal, double weight, double height, char sex, int age, String label) {
        
        String response = "";
        double dailyIntake = findDailyIntake(weight, height, sex, age);
        if (meal == 'B' || meal == 'b') {
            if (findTotalFat(label) > (.42 * dailyIntake * findCalories(label) * 0.08 / 9)) {
                response = "This meal/snack is HIGH in saturated fat.";
            } else
                response = "You meal/snack has a healthy amount of saturated fat.";
        } else if (meal == 'L' || meal == 'l') {
            if (findTotalFat(label) > (.34 * dailyIntake * findCalories(label) * 0.08 / 9)) {
                response = "This meal/snack is HIGH in saturated fat.";
            } else
                response = "You meal/snack has a healthy amount of saturated fat.";
        } else if (meal == 'D' || meal == 'd') {
            if (findTotalFat(label) > (.42 * dailyIntake * findCalories(label) * 0.08 / 9)) {
                response = "This meal/snack is HIGH in saturated fat.";
            } else
                response = "You meal/snack has a healthy amount of saturated fat.";

        } else if (meal == 'S' || meal == 's') {
            if (findTotalFat(label) > (.18 * dailyIntake * findCalories(label) * 0.08 / 9)) {
                response = "This meal/snack is HIGH in saturated fat.";
            } else
                response = "You meal/snack has a healthy amount of saturated fat.";
        }

        totalValues[2] += findSaturatedFat(label);
        return response;
    }

    public String transFatResponse(char meal, double weight, double height, char sex, int age, String label) {
        
        String response = "";
        if (findTransFat(label) > 0)
            response = "Any trans fat is unhealthy for you! This meal/snack has too much.";
        else
            response = "This food has the right amount of trans fat: 0 grams!";
        totalValues[3] += findTransFat(label);
        return response;
    }

    public String cholesterolResponse(char meal, double weight, double height, char sex, int age, String label) {

        String response = "";
        double dailyIntake = findDailyIntake(weight, height, sex, age);
        if (meal == 'B' || meal == 'b') {
            if (findCholesterol(label) > 200 / 3) {
                response = "This meal/snack is HIGH in cholesterol.";
            } else
                response = "You meal/snack has a healthy amount cholesterol.";
        } else if (meal == 'L' || meal == 'l') {
            if (findCholesterol(label) > 200 / 3) {
                response = "This meal/snack is HIGH in cholesterol.";
            } else
                response = "You meal/snack has a healthy amount cholesterol.";
        } else if (meal == 'D' || meal == 'd') {
            if (findCholesterol(label) > 200 / 3) {
                response = "This meal/snack is HIGH in cholesterol.";
            } else
                response = "You meal/snack has a healthy amount cholesterol.";

        } else if (meal == 'S' || meal == 's') {
            if (findCholesterol(label) > 200 * 0.18) {
                response = "This meal/snack is HIGH in cholesterol.";
            } else
                response = "You meal/snack has a healthy amount cholesterol.";
        }

        totalValues[4] += findCholesterol(label);
        return response;
    }

    public String addedSugarResponse(char meal, double weight, double height, char sex, int age, String label) {
        String response = "";
        double dailyIntake = findDailyIntake(weight, height, sex, age);
        if (meal == 'B' || meal == 'b') {
            if (findAddedSugars(label) > (.42 * dailyIntake * findCalories(label) * 0.1 / 9)) {
                response = "This meal/snack is HIGH in added sugars.";
            } else
                response = "You meal/snack does not have too much added sugar.";
        } else if (meal == 'L' || meal == 'l') {
            if (findAddedSugars(label) > (.33 * dailyIntake * findCalories(label) * 0.1 / 9)) {
                response = "This meal/snack is HIGH in added sugars.";
            } else
                response = "You meal/snack does not have too much added sugar.";
        } else if (meal == 'D' || meal == 'd') {
            if (findAddedSugars(label) > (.42 * dailyIntake * findCalories(label) * 0.1 / 9)) {
                response = "This meal/snack is HIGH in added sugars.";
            } else
                response = "You meal/snack does not have too much added sugar.";

        } else if (meal == 'S' || meal == 's') {
            if (findAddedSugars(label) > (.18 * dailyIntake * findCalories(label) * 0.1 / 9)) {
                response = "This meal/snack is HIGH in added sugars.";
            } else
                response = "You meal/snack does not have too much added sugar.";
        }

        totalValues[5] += findAddedSugars(label);
        return response;
    }

    public String protein(char meal, double weight, double height, char sex, int age, String label) {
        
        String response = "";
        double dailyIntake = findDailyIntake(weight, height, sex, age);
        if (meal == 'B' || meal == 'b') {
            if (findProtein(label) > (.2 * dailyIntake * findCalories(label) * 0.2 / 4)) {
                response = "This meal/snack has a healthy amount of protein.";
            } else
                response = "You meal/snack does not have enough protein.";
        } else if (meal == 'L' || meal == 'l') {
            if (findProtein(label) > (.2 * dailyIntake * findCalories(label) * 0.2 / 4)) {
                response = "This meal/snack has a healthy amount of protein.";
            } else
                response = "You meal/snack does not have enough protein.";
        } else if (meal == 'D' || meal == 'd') {
            if (findProtein(label) > (.25 * dailyIntake * findCalories(label) * 0.2 / 4)) {
                response = "This meal/snack has a healthy amount of protein.";
            } else
                response = "You meal/snack does not have enough protein.";
        } else if (meal == 'S' || meal == 's') {
            if (findProtein(label) > (.15 * dailyIntake * findCalories(label) * 0.2 / 4)) {
                response = "This meal/snack has a healthy amount of protein.";
            } else
                response = "You meal/snack does not have enough protein.";
        }

        totalValues[6] += findProtein(label);
        return response;
    }

    public int findTotalFat(String label) {

        int location = label.indexOf("Fat") + 4;
        int space = label.indexOf("g", location);
        String calories = label.substring(location, space);
        if (calories.equalsIgnoreCase("o"))
            calories = "0";
        int c = Integer.parseInt(calories);
        return c;


    }

    public int findSaturatedFat(String label) {
        
        int location = label.indexOf("Saturated") + 14;
        int space = label.indexOf("g", location);
        String calories = label.substring(location, space);
        if (calories.equalsIgnoreCase("o"))
            calories = "0";
        int c = Integer.parseInt(calories);
        return c;
    }

    public int findTransFat(String label) {
        
        int location = label.indexOf("Trans") + 10;
        int space = label.indexOf("g", location);

        String calories = label.substring(location, space-1);
        if (calories.equalsIgnoreCase("o"))
            calories = "0";
        int c = Integer.parseInt(calories);
        return c;
    }

    public int findCholesterol(String label) {
        Log.i("Abhi", label.indexOf("Cholesterol") + " " + label.substring(label.indexOf("Cholesterol"), label.indexOf("Cholesterol") + 12));
        int location = label.indexOf("Cholesterol") + 12;
        int space = label.indexOf("m", location);
        String calories = label.substring(location, space);
        if (calories.equalsIgnoreCase("o"))
            calories = "0";
        int c = Integer.parseInt(calories);
        return c;
    }

    public int findAddedSugars(String label) {
        
        int location = label.indexOf("Includes") + 9;
        int space = label.indexOf("g", location);

        String calories = label.substring(location, space-1);
        if (calories.equalsIgnoreCase("o"))
            calories = "0";
        int c = Integer.parseInt(calories);
        return c;
    }

    public int findProtein(String label) {
        
        int location = label.indexOf("Protein") + 8;
        int space = label.indexOf("g", location);

        Log.i("Oof", label.substring(location, space-1));
        String calories = label.substring(location, space-1);
        if (calories.equalsIgnoreCase("o"))
            calories = "0";
        int c = Integer.parseInt(calories);
        return c;
    }

}
