package cookbook.frontend;

import cookbook.GlobalState;
import cookbook.db.DatabaseManager;
import cookbook.db.entities.*;
import cookbook.db.services.RecipeService;
import cookbook.db.services.UserService;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import cookbook.db.services.CommentService;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AppController implements Initializable {

  
  public List<RecipeEntity> allRecipes;

  ObservableList<RecipeEntity> recipeEntities = FXCollections.observableArrayList();
  ObservableList<CommentEntity> recipeCommentEntities = FXCollections.observableArrayList();
  int portions = 1;

  @FXML
  public TableView<RecipeEntity> recipeLst;

  @FXML
  public TableColumn<RecipeEntity, String> recipeCol;
  
  @FXML
  private TextField addCmntText;

  @FXML
  private ListView<CommentEntity> comntlist;


  @FXML
  private ImageView favImageView;

  @FXML
  private CheckBox favoriteChck;


  @FXML
  private Text ingredText;


  @FXML
  private Text tagText;


  @FXML
  private Label recipeNameLabel;

  @FXML
  private TextField search;

  @FXML
  private Button addCommentBtn;

  @FXML
  private Text longdesText;

  @FXML
  private Button editCommentBtn;

  @FXML
  private Button deleteButtonBtn;

  @FXML
  private ScrollPane mainScrollPane;

  @FXML
  private Label portionsLabel;

  @FXML
  private Text shortDesText;


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    mainScrollPane.getStyleClass().clear();
    refreshData();
  }

  // refresh the data
  private void refreshData() {
    loadData("", false);
    initiatecols();
  }

  // set the column in the recipe table
  private void initiatecols() {
    recipeCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    hover();
  }

  // add the recipes to the recipe table
  private void loadData(String searchQuery, boolean starredOnly) {
    if (!RecipeService.getAll().isEmpty()) {
      allRecipes = RecipeService.getAll().stream().filter(recipe -> {
        if (starredOnly && !recipe.getStarred())
          return false;
  
        String tagsString = String.join(" ", recipe.getTags().stream().map(tag -> tag.getName())
                .collect(Collectors.toList()));
  
        String ingredientsString = String.join(" ", recipe.getQuantifiedIngredients().stream().map(qi -> qi.getIngredient().getName())
                .collect(Collectors.toList()));
  
        //system.out.println(tagsString);
        //system.out.println(ingredientsString);
  
        return String.format("""
                            %s %s %s %s %s
                        """, recipe.getName(), recipe.getShortDesc(), recipe.getLongDesc(), tagsString, ingredientsString).toLowerCase()
                .contains(searchQuery.toLowerCase());
      }).collect(Collectors.toList());
  
      recipeEntities = FXCollections.observableArrayList(allRecipes);
      recipeLst.getItems().clear();
      recipeLst.getItems().addAll(recipeEntities);
      recipeEntities.removeAll();
    } else {
      return;
    }
  }

  // show the short description (hover)
  public void hover() {
    recipeCol.setCellFactory(new Callback<>() {
      @Override
      public TableCell<RecipeEntity, String> call(TableColumn<RecipeEntity, String> param) {
        return new TableCell<>() {
          @Override
          protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(item);
            Optional<RecipeEntity> currentRecipe = allRecipes.stream().filter(r -> r.getName().equals(item))
                    .findAny();
            var t = new Tooltip(
                    currentRecipe.isPresent() ? currentRecipe.get().getShortDesc() : "No short desc");
            t.setStyle("-fx-font-style: italic; -fx-font-size: 14;");
            setTooltip(t);

          }
        };
      }
    });
  }

  private void updateIngredientsText() {
      StringBuilder ingredientsString = new StringBuilder();
      RecipeEntity recp = recipeLst.getSelectionModel().getSelectedItem();
      for (QuantifiedIngredientEntity quantifiedIngredient : recp.getQuantifiedIngredients()) {
          float ingredientAmount = quantifiedIngredient.getAmount() * portions;
          String ingredientUnit = quantifiedIngredient.getUnit() != null ? " " + quantifiedIngredient.getUnit() : "";
          String ingredientName = quantifiedIngredient.getIngredient().getName();
          ingredientsString.append(String.format("%s%s %s\n", ingredientAmount, ingredientUnit, ingredientName));
      }
      ingredText.setText(ingredientsString.toString());
      portionsLabel.setText(String.valueOf(portions));
  }

  // display the selected recipe
  @FXML
  private void displaySelected(MouseEvent event) {
    portions = 1;
    RecipeEntity recp = recipeLst.getSelectionModel().getSelectedItem();
    if (recp != null) {
      String name = recp.getName();
      String des = recp.getLongDesc();
      String shortDes = recp.getShortDesc();
      boolean fav = recp.getStarred();
      StringBuilder tagString = new StringBuilder();


      recipeNameLabel.setStyle("-fx-font-size: 21; -fx-font-weight: 700");
      recipeNameLabel.setText(name);
      longdesText.setText(des);
      shortDesText.setText(shortDes);

      // display the tags
      for (TagEntity tag : recp.getTags()) {
        String tagUser = tag.getName();
        tagString.append(String.format("%s", tagUser)).append(", ");

      }
      tagText.setText(tagString.toString());

      updateIngredientsText();

      // star and unstar image on the bottun favorite

      if (fav) {
        String path = "file:\\" + Path.of("").toAbsolutePath()
                + "\\src\\main\\resources\\cookbook\\images\\deletes.png";
        favImageView.setImage(new Image(path));
      } else {
        String path = "file:\\" + Path.of("").toAbsolutePath()
                + "\\src\\main\\resources\\cookbook\\images\\Untitled.png";
        //system.out.println(path);
        favImageView.setImage(new Image(path));
      }
      // Display the comments
      recipeCommentEntities.clear();
      comntlist.setCellFactory(commentList -> new CommentListViewCell());
      comntlist.setItems(recipeCommentEntities);
      recipeCommentEntities.addAll(recp.getComments());
      
        
      
      // reset the scrollable
      mainScrollPane.setVvalue(0);
    } else {
      return;
    }
    
  }

  // search button
  @FXML
  public void Search(ActionEvent event) {
    loadData(search.getText(), favoriteChck.isSelected());
    initiatecols();
  }

  // favorite check
  @FXML
  public void starred(ActionEvent event) {
    loadData(search.getText(), favoriteChck.isSelected());
    initiatecols();
  }

  // when we clicked on the comment listview
  @FXML
  public void onReviewClicked(MouseEvent event) {
    RecipeEntity recp = recipeLst.getSelectionModel().getSelectedItem();
    if (recp == null) {
      return ;
    }
    CommentEntity comment = comntlist.getSelectionModel().getSelectedItem();
    if (comment == null) {
      return ;
    } else if (GlobalState.user.getId().equals(comment.getUserId())) {

      // WE CAN EDIT OR DELETE
      addCommentBtn.setVisible(true);
      editCommentBtn.setVisible(true);
      deleteButtonBtn.setVisible(true);

      //system.out.println("WE CAN MODIFY");
      //addCmntText.setText(comment.getBody());
    } else {
      addCommentBtn.setVisible(true);
      editCommentBtn.setVisible(false);
      deleteButtonBtn.setVisible(false);

      addCmntText.setText("Add a comment ....");
      //system.out.println("WE CANNOT MODIFY");
    }

  }

  @FXML
  // Add favorite recipe
  public void addfavorite(ActionEvent event) throws SQLException {

    RecipeEntity recp = recipeLst.getSelectionModel().getSelectedItem();

    if (recp == null)
      return;

    if (RecipeService.updateStarredStatus(recp.getId()))
      refreshData();

  }

  @FXML
  // delete a comment
  public void deleteCmntBtn(ActionEvent event) {
    RecipeEntity recp = recipeLst.getSelectionModel().getSelectedItem();
    if (recp == null) {
      return;
    }
    CommentEntity comment = comntlist.getSelectionModel().getSelectedItem();
    if (comment == null) {
      return;
    }
    CommentService.deleteComment(comment.getId());
    recipeCommentEntities.remove(comment);
    recp.getComments().remove(comment);
  }

  @FXML
  // edit comments button
  public void editCommentAction(ActionEvent event) {
    RecipeEntity recp = recipeLst.getSelectionModel().getSelectedItem();
    if (recp == null) {
      return;
    }
    CommentEntity comment = comntlist.getSelectionModel().getSelectedItem();
    if (comment == null) {
      return;
    }
    // TO DO:
    //  EDIT EXISTING COMMENT
    addCmntText.setText(comment.getBody());
    CommentService.deleteComment(comment.getId());
    recipeCommentEntities.remove(comment);
    recp.getComments().remove(comment);
  }

  /// add comment button
  @FXML
  public void addCommentBtn(ActionEvent event) {

    RecipeEntity recp = recipeLst.getSelectionModel().getSelectedItem();
    if (recp == null) {
      return;
    } else if (addCmntText.getText() == null){
      return;
    }
    CommentEntity newComment = new CommentEntity(GlobalState.user.getId(), addCmntText.getText());
    recipeCommentEntities.add(newComment);
    CommentService.addComment(addCmntText.getText(), recp.getId(), GlobalState.user.getId());
    recp.addComment(newComment);
    addCmntText.clear();
  }

  @FXML
  void sendMessageBtnAction(ActionEvent event) throws IOException {
    RecipeEntity recp = recipeLst.getSelectionModel().getSelectedItem();

    if (recipeLst.getSelectionModel().getSelectedItem() != null) {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("sendMessage.fxml"));
      loader.load();
      Parent p = loader.getRoot();


      // pass the id
      SendMessageController setController = loader.getController();
      setController.passInformation(recp.getId());

      // pass the name
      SendMessageController setControllertwo = loader.getController();
      setControllertwo.passNameInformation(recp.getName());

      Stage stage = new Stage();
      stage.setScene(new Scene(p));
      stage.setTitle("Send a Message");


      stage.show();
    } else {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Error");
      alert.setContentText("Please select a recipe");
      alert.show();
    }
  }


  public void SearchAction(ActionEvent event) {
    loadData(search.getText(), favoriteChck.isSelected());
    initiatecols();
  }

  // datePicker
  @FXML
  private DatePicker myDatePicker;

  // weeklyList DatePicker
  @FXML
  void datePicker(ActionEvent event) {
    LocalDate localDate = myDatePicker.getValue();
    localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
  }

  // Add date to the weeklist table
  @FXML
  void addDateButton(ActionEvent event) {
    RecipeEntity recp = recipeLst.getSelectionModel().getSelectedItem();

    try {
      LocalDate localDate = myDatePicker.getValue();

      Timestamp timestamp = Timestamp.valueOf(localDate.atTime(LocalTime.MIDNIGHT));
      //System.out.println(timestamp); //2019-05-16 00:00:00.0

      RecipeService.adddate(recp.getId(),timestamp, GlobalState.user.getId());

    }  //this generic but you can control another types of exception
    catch (Exception e) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Error");
      alert.setContentText("Please select a recipe");
      alert.show();
    }
  }

  @FXML
  void onDecreasePortions(ActionEvent event) {
    if (portions > 1) {
        portions--;
        updateIngredientsText();
    }
  }
  
  @FXML
  void onIncreasePortions(ActionEvent event) {
    portions++;
    updateIngredientsText();
  }
}








