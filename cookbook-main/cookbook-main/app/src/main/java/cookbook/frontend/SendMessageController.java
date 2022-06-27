package cookbook.frontend;

import cookbook.GlobalState;
import cookbook.db.entities.UserEntity;
import cookbook.db.services.MessageService;
import cookbook.db.services.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;


public class SendMessageController implements Initializable {
  public TableView<UserEntity> userList;
  public TableColumn<UserEntity, String> displayNameColumn;
  public TableColumn<UserEntity, String> userColumn;


  @FXML
  private Label recipeId;

  @FXML
  private Label recipeName;

  @FXML
  private Label date;

  @FXML
  private TextArea sendMessageText;


  @FXML
  private TextField txtUserName;

  public List<UserEntity> allusers;
  ObservableList<UserEntity> Users = FXCollections.observableArrayList();


  private void refreshData() {
    loadData();

    initiatecols();
  }


  // adding data to the columns
  private void initiatecols() {

    displayNameColumn.setCellValueFactory(new PropertyValueFactory<>("displayName"));
    userColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

  }


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    refreshData();

  }

  // display name , user
  public void displaySelected(MouseEvent event) {
    UserEntity user = userList.getSelectionModel().getSelectedItem();
    if (user == null)
      return;
    String userName = user.getDisplayName();
    txtUserName.setText(userName);
    date.setText("");


  }

  // load the data for the combobox and the user table
  private void loadData() {

    allusers = UserService.getUsers();
    Users = FXCollections.observableArrayList(allusers);
    userList.getItems().clear();
    userList.getItems().addAll(Users);

  }


  // send message button
  @FXML
  void sendMessageAction(ActionEvent event) throws ParseException {

    UserEntity user = userList.getSelectionModel().getSelectedItem();
    try {
      String time = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss").format(LocalDateTime.now());

      Timestamp timestamp = Timestamp.valueOf(time);
      date.setText("Message sent successfully at :  "   + timestamp);


      sendMessageText.setWrapText(true);
      MessageService.sendMessage(GlobalState.user.getId(), user.getId(),recipeId.getText(), sendMessageText.getText(),timestamp );

    } catch (Exception e) { //this generic but you can control another types of exception
      System.out.println("error");
    }
    txtUserName.clear();
    sendMessageText.clear();



  }




  // receive information about the recipe id
  public void passInformation(String id) {
    recipeId.setText(id);

  }

  public void passNameInformation(String name) {
    recipeName.setText(name);

  }
}

