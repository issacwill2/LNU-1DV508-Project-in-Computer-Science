package cookbook.frontend;

import cookbook.GlobalState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FirstPageController implements Initializable {

  @FXML
  private Label UserName;

  @FXML
  private AnchorPane root;

  @FXML
  private Parent fxml;

  @FXML
  private Button adminPanel;


  @FXML
  private Button addbutton;



  @FXML
  private Button helpbutton;

  @FXML
  private Button homebtn;

  @FXML
  private Button messagebutton;



  @FXML
  private Button signoutButton;

  @FXML
  private Button weeklistbutton;

  @FXML
  void homebtn(ActionEvent event) {  // shows the homepage
    try {
      fxml = FXMLLoader.load(getClass().getResource("app.fxml"));
      root.getChildren().removeAll();
      root.getChildren().setAll(fxml);
      String Nostyle=String.format("-fx-background-color: #FFFFFF;");

      String style=String.format("-fx-background-color: #ECF0FD;");
      weeklistbutton.setStyle(Nostyle);
      helpbutton.setStyle(Nostyle);
      homebtn.setStyle(style);
      addbutton.setStyle(Nostyle);
      adminPanel.setStyle(Nostyle);
      signoutButton.setStyle(Nostyle);
      messagebutton.setStyle(Nostyle);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  void addNewRecipe(ActionEvent event) {
    try {
      fxml = FXMLLoader.load(getClass().getResource("addNewRecipe.fxml"));
      root.getChildren().removeAll();
      root.getChildren().setAll(fxml);
      String Nostyle=String.format("-fx-background-color: #FFFFFF;");


      String style=String.format("-fx-background-color: #ECF0FD;");
      weeklistbutton.setStyle(Nostyle);
      helpbutton.setStyle(Nostyle);
      homebtn.setStyle(Nostyle);
      addbutton.setStyle(style);
      adminPanel.setStyle(Nostyle);
      signoutButton.setStyle(Nostyle);
      messagebutton.setStyle(Nostyle);


    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  @FXML
  void helpbtn(ActionEvent event) {
    try {
      fxml = FXMLLoader.load(getClass().getResource("help.fxml"));
      root.getChildren().removeAll();
      root.getChildren().setAll(fxml);
      String Nostyle=String.format("-fx-background-color: #FFFFFF;");


      String style=String.format("-fx-background-color: #ECF0FD;");

      weeklistbutton.setStyle(Nostyle);
      helpbutton.setStyle(style);
      homebtn.setStyle(Nostyle);
      addbutton.setStyle(Nostyle);
      adminPanel.setStyle(Nostyle);
      signoutButton.setStyle(Nostyle);
      messagebutton.setStyle(Nostyle);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  void calendarButton(ActionEvent event) {
    try {
      fxml = FXMLLoader.load(getClass().getResource("calendar.fxml"));
      root.getChildren().removeAll();
      root.getChildren().setAll(fxml);
      String Nostyle=String.format("-fx-background-color: #FFFFFF;");


      String style=String.format("-fx-background-color: #ECF0FD;");

      weeklistbutton.setStyle(style);
      helpbutton.setStyle(Nostyle);
      homebtn.setStyle(Nostyle);
      addbutton.setStyle(Nostyle);
      adminPanel.setStyle(Nostyle);
      signoutButton.setStyle(Nostyle);
      messagebutton.setStyle(Nostyle);


    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  void signoutbtn(ActionEvent event) throws IOException {
    Parent homePage = null;
    try {
      homePage = FXMLLoader.load(getClass().getResource("login.fxml"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    Scene homePageScene = new Scene(homePage);
    Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    appStage.hide();
    appStage.setScene(homePageScene);
    appStage.show();
    appStage.setHeight(400);
    appStage.setWidth(315);
    appStage.show();
    appStage.setResizable(true);
    appStage.centerOnScreen();
    appStage.setTitle("Sign In");



  }

  public void showInformation(String name) {

  }


  @FXML
  void adminPannelAction(ActionEvent event) {
    try {
      fxml = FXMLLoader.load(getClass().getResource("adminPannel.fxml"));
      root.getChildren().removeAll();
      root.getChildren().setAll(fxml);
      String Nostyle=String.format("-fx-background-color: #FFFFFF;");

      String style=String.format("-fx-background-color: #ECF0FD;");

      weeklistbutton.setStyle(Nostyle);
      helpbutton.setStyle(Nostyle);
      homebtn.setStyle(Nostyle);
      addbutton.setStyle(Nostyle);
      adminPanel.setStyle(style);
      signoutButton.setStyle(Nostyle);
      messagebutton.setStyle(Nostyle);



    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  @Override
  public void initialize(URL location, ResourceBundle resources) {

    String name = GlobalState.user.getDisplayName();

    // create two substrings from name
    // first substring contains first letter of name
    // second substring contains remaining letters
    String firstLetter = name.substring(0, 1);
    String remainingLetters = name.substring(1, name.length());

    // change the first letter to uppercase
    firstLetter = firstLetter.toUpperCase();
    name = firstLetter + remainingLetters;

    //UserName.setText( GlobalState.user.getDisplayName());
    UserName.setText( "Hello, " + name);

    if (GlobalState.user != null && GlobalState.user.getIsAdmin()) {
      adminPanel.setDisable(false);

    } else
      adminPanel.setDisable(true);


  }
  @FXML
  void checkInbox(ActionEvent event) {
    try {
      fxml= FXMLLoader.load(getClass().getResource("inbox.fxml"));
      root.getChildren().removeAll();
      root.getChildren().setAll(fxml);
      String Nostyle=String.format("-fx-background-color: #FFFFFF;");
      String style=String.format("-fx-background-color: #ECF0FD;");

      weeklistbutton.setStyle(Nostyle);
      helpbutton.setStyle(Nostyle);
      homebtn.setStyle(Nostyle);
      addbutton.setStyle(Nostyle);
      adminPanel.setStyle(Nostyle);
      signoutButton.setStyle(Nostyle);
      messagebutton.setStyle(style);

    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
