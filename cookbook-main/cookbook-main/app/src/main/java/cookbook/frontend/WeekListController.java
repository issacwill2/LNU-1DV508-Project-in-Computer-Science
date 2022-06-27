package cookbook.frontend;

import cookbook.db.entities.QuantifiedIngredientEntity;
import cookbook.db.entities.ScheduledRecipeEntity;
import cookbook.db.services.ScheduledRecipeEntityServices;
import cookbook.db.services.WeekListServices;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class WeekListController implements Initializable {
  @FXML
  private Label Weeklabel;

  @FXML
  private ListView<String> fridayListView;
  @FXML
  private ListView<String> mondayListView;
  @FXML
  private ListView<String> saturdayListView;
  @FXML
  private VBox weekSelection;
  @FXML
  private ListView<String> sundayListView;
  @FXML
  private ListView<String> thursdayListView;
  @FXML
  private ListView<String> tuesdayListView;
  @FXML
  private ListView<String> WednesdayListView;
  @FXML
  private Button openShopp;
  @FXML
  private ComboBox<String> weeksComboxBox;

  private ObservableList<QuantifiedIngredientEntity> shoppingList = FXCollections.observableArrayList();

  private LocalDate startingDateGlobal;

  public static List<LocalDate> getDatesBetweenUsingJava8(
          LocalDate startDate, LocalDate endDate) {

    long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;
    return IntStream.iterate(0, i -> i + 1)
            .limit(numOfDaysBetween)
            .mapToObj(i -> startDate.plusDays(i))
            .collect(Collectors.toList());
  }

  public void clearall() {
    mondayListView.getItems().clear();
    tuesdayListView.getItems().clear();
    WednesdayListView.getItems().clear();
    thursdayListView.getItems().clear();
    fridayListView.getItems().clear();
    saturdayListView.getItems().clear();
    sundayListView.getItems().clear();
    shoppingList.clear();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    ObservableList<String> weekList = FXCollections.observableArrayList(WeekListServices.getNextWeeks(11));
    //ListView<String> weeks = new ListView<>(weekList);
    weeksComboxBox.setItems(weekList);



    weeksComboxBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
      public void changed(ObservableValue<? extends String> ov,
                          final String oldvalue, final String newvalue) {
        weekChanged(newvalue);
        Weeklabel.setText(newvalue);
      }
    });
  }


  public void weekChanged(String newValue) {
    clearall();
    startingDateGlobal = LocalDate.parse(newValue.split(" - ")[0]);
    LocalDate endingDate = LocalDate.parse(newValue.split(" - ")[1]);
    List<LocalDate> datesInBetween = getDatesBetweenUsingJava8(startingDateGlobal, endingDate);

    for (LocalDate date : datesInBetween) {
      String z = String.valueOf(date);
      LocalDate dt = LocalDate.parse(z.toString());
      String day = String.valueOf(dt.getDayOfWeek());
      ListView<String> currentListView;

      if (day.equals("MONDAY")) {
        currentListView = mondayListView;
      } else if (day.equals("TUESDAY")) {
        currentListView = tuesdayListView;
      } else if (day.equals("WEDNESDAY")) {
        currentListView = WednesdayListView;
      } else if (day.equals("THURSDAY")) {
        currentListView = thursdayListView;
      } else if (day.equals("FRIDAY")) {
        currentListView = fridayListView;
      } else if (day.equals("SATURDAY")) {
        currentListView = saturdayListView;
      } else {
        currentListView = sundayListView;
      }


      List<ScheduledRecipeEntity> dateSchedule = ScheduledRecipeEntityServices.getDateSchedule(Date.valueOf(date));
      if (dateSchedule == null) {
        ;
      } else { 
        for (ScheduledRecipeEntity entity : dateSchedule) {
          currentListView.getItems().add(entity.getRecipeName());
          for (QuantifiedIngredientEntity qe : entity.getIngredients()) {
            if (shoppingList.contains(qe)) {
              int index = shoppingList.indexOf(qe);
              shoppingList.get(index).addAmount(qe.getAmount());
            } else {
              shoppingList.add(qe);
            }
          }
        }
      }
    }
  }


  @FXML
  void openShoppingList(ActionEvent event) throws IOException {
    if (!shoppingList.isEmpty() && startingDateGlobal!=null) {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("shoppingList.fxml"));
      loader.load();
      Parent p = loader.getRoot();

      // Pass the shopping list to next controller
      ShoppingListController shoppingController = loader.getController();
      shoppingController.getShoppingList(shoppingList, startingDateGlobal);

      Stage stage = new Stage();
      stage.setScene(new Scene(p));
      stage.setTitle("Shopping List");

      stage.show();
    } else {

      if (weeksComboxBox.getSelectionModel().getSelectedItem() == null) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("NOTE");
        alert.setContentText("Please select a Week");
        alert.show();
      } else {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("NOTE");
        alert.setContentText("No shopping list for this Week.");
        alert.show();
      }
    }
  }
}






