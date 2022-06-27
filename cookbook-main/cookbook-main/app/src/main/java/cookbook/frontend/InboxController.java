package cookbook.frontend;

import cookbook.GlobalState;
import cookbook.db.entities.UserEntity;
import cookbook.db.services.MessageService;
import cookbook.db.services.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class InboxController implements Initializable {


  @FXML
  private TableView<UserEntity> userList;

  @FXML
  private TableColumn<UserEntity, String> userColumn;


  @FXML
  private Label inboxbox;


  @FXML
  private ListView<String> inboxlist;


  public List<UserEntity> allusers;
  ObservableList<UserEntity> Users = FXCollections.observableArrayList();


  // load all the users
  private void loadData() {

    allusers = UserService.getUsers();
    Users = FXCollections.observableArrayList(allusers);
    userList.getItems().clear();
    userList.getItems().addAll(Users);

  }

  // adding data to the columns
  private void initiatecols() {

    userColumn.setCellValueFactory(new PropertyValueFactory<>("displayName"));

  }

  // display all the message and the recipe when selecting the user if there is a message
  @FXML
  void DisplaySelected(MouseEvent event) {
    inboxlist.getItems().clear();
    Label lbl = new Label("No messages from this user");
    lbl.setStyle("-fx-font-weight: bold; -fx-font-size:20");
    inboxlist.setPlaceholder(lbl);

    inboxbox.setText("");
    UserEntity user = userList.getSelectionModel().getSelectedItem();
    if (user == null)
      return;
    String userId = user.getId();
    var message = MessageService.getMessagesFromOneUserToOther(userId, GlobalState.user.getId());
    for (var item : message) {
      String msg = item.getBody();
      String recipe = item.getRecipeId();
      var RecipeName = MessageService.getRecipeName(recipe);
      String allMessages = (String.format("%s\n%s\n\n", RecipeName.toUpperCase(), msg));
      inboxlist.getItems().addAll(allMessages);
      inboxlist.setStyle(" -fx-font-size: 16;");
    }

  }


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    loadData();

    initiatecols();
  }


  // display the message in the label
  @FXML
  void DisplayTheMSG(MouseEvent event) {


    String msg = inboxlist.getSelectionModel().getSelectedItem();
    inboxbox.setWrapText(true);
    inboxbox.setText(msg);

  }
}



