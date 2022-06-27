package cookbook.db.services;

import cookbook.db.DatabaseManager;
import cookbook.db.entities.*;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.Calendar;


public class WeekListServices {

    // Find the last monday relative to current date.
    public static Calendar getWeekZero() {
        // new Calendar instance
        Calendar cal = Calendar.getInstance();

        // Get the delta between today's day and last monday
        int day_number = cal.get(Calendar.DAY_OF_WEEK);
        int delta = Calendar.MONDAY - day_number;

        // Add the numebr of days needed (negative days)
        // Take this day as Monday of this week
        cal.add(Calendar.DATE, delta);

        // Return calendar set to monday of current week
        return cal;
    }


    public static List<String> getNextWeeks(int numberOfWeeks){
        List<String> stringList = new ArrayList<>();
        final ZonedDateTime input = ZonedDateTime.now();

        for(int i = 1; i < numberOfWeeks; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            final ZonedDateTime startOfLastWeek = input.plusWeeks(i).with(DayOfWeek.MONDAY);
            final ZonedDateTime endOfLastWeek = startOfLastWeek.plusDays(6);
            stringBuilder.append(startOfLastWeek.format(DateTimeFormatter.ISO_LOCAL_DATE));
            stringBuilder.append(" - ").append(endOfLastWeek.format(DateTimeFormatter.ISO_LOCAL_DATE));
            stringList.add(stringBuilder.toString());
//            System.out.print(startOfLastWeek.format(DateTimeFormatter.ISO_LOCAL_DATE));
//            System.out.println(" - " + endOfLastWeek.format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
        return stringList;
    }


    // Returns a List of Lists of Scheduled Recipes given the week number
    public static List<List<ScheduledRecipeEntity>> getWeekList(int weekNumber) {

        List<List<ScheduledRecipeEntity>> week = new ArrayList<>();

        // Get a calendar pointing to this monday
        Calendar currentMonday = getWeekZero();
        System.out.println("currentMonday: " + currentMonday.getTime());
        // Get the monday of the week in question
        currentMonday.add(Calendar.DATE, weekNumber*7);
        Calendar newWeekMonday = currentMonday;

        // We can convert this to an sql date now
        java.util.Date weekMondayDate = newWeekMonday.getTime();
        java.sql.Date weekListMonday = new java.sql.Date(weekMondayDate.getTime());
        // We add it to the arraylist
        week.add(ScheduledRecipeEntityServices.getDateSchedule(weekListMonday));

        // Now we get a list of lists for each day in our week
        // Starting at monday
        java.util.Date currentDay;
        java.sql.Date currentDate;

        for (int day = 1; day < 7; day ++) {
            newWeekMonday.add(Calendar.DATE, 1);
            currentDay = newWeekMonday.getTime();
            currentDate = new java.sql.Date(currentDay.getTime());

            week.add(ScheduledRecipeEntityServices.getDateSchedule(currentDate));
        }
        return week;
    }


    // Insert a scheduled Recipe record
    public static void addScheduledRecipe(String recipeId, String recipeName, String userId, java.sql.Date date) {

        Connection conn = DatabaseManager.getConn();

        // Insert records into db
        String insertString = "INSERT INTO week_list VALUES(?, ?, ?);";

        try (PreparedStatement preparedStmnt = conn.prepareStatement(insertString)) {
            preparedStmnt.setDate(1, date);
            preparedStmnt.setString(2, recipeId);
            preparedStmnt.setString(3, userId);

            preparedStmnt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e);
            // If there's an error inserting records, return false.
        }
    }


    // Delete a scheduled recipe from the db
    public static void deleteScheduledRecipe(ScheduledRecipeEntity schedRec) {
        String recipeId = schedRec.getRecipeId();
        java.sql.Date date = schedRec.getDate();

        String deleteString = "DELETE FROM week_list WHERE date = (?) AND recipe_id = (?);";

        Connection conn = DatabaseManager.getConn();

        try (PreparedStatement preparedStmnt = conn.prepareStatement(deleteString)) {
            preparedStmnt.setDate(1, date);
            preparedStmnt.setString(2, recipeId);
            preparedStmnt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e);
            // If there's an error inserting records, return false.
        }
    }

    // Generate a shopping list for a given Week [INCLUDES INGREDIENT QUANTITY]
    public static List<QuantifiedIngredientEntity> generateShoppingList(List<List<ScheduledRecipeEntity>> week) {

        // Get list for Quanitifed ingredients ready
        List<QuantifiedIngredientEntity> weekIngredients = new ArrayList<>();

        // Flatten the WeekList
        List<ScheduledRecipeEntity> weekList = week.stream().flatMap(List::stream).collect(Collectors.toList());
        
        for( ScheduledRecipeEntity meal : weekList) {
            for (QuantifiedIngredientEntity ingredient : meal.getIngredients()) {
                if (weekIngredients.contains(ingredient)) {
                    // Find the index of the Ingredient in the ingredient tuple list
                    int index = weekIngredients.indexOf(ingredient);             
                    // Add the ingredient amount to existing list amount       
                    weekIngredients.get(index).addAmount(ingredient.getAmount());
                    continue;
                } else {
                    // Add ingredient to ingredients list
                    weekIngredients.add(ingredient);
                }
            }
        }
        // Retrun that week's ingredient
        return weekIngredients;
    }
}
