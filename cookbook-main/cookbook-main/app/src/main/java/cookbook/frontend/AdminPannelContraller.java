package cookbook.frontend;


import cookbook.db.entities.UserEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import cookbook.db.services.UserService;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminPannelContraller implements Initializable {
  public TableView<UserEntity> userList;
  public TableColumn<UserEntity, String> displayNameColumn;


  public List<UserEntity> allusers;
  ObservableList<UserEntity> Users = FXCollections.observableArrayList();
  @FXML
  private TextField txtPassword;

  @FXML
  private TextField txtDisplayName;


  @FXML
  private TextField txtUserName;

  private void refreshData() {
    loadData();
    initiatecols();
  }

  @FXML
  void Addbutton(ActionEvent event) {

    UserService.add(txtDisplayName.getText(), txtUserName.getText(), txtPassword.getText(), false);

    refreshData();
    txtUserName.setText("");
    txtPassword.setText("");
    txtDisplayName.setText("");

  }

  @FXML
  void ModifyButton(ActionEvent event) {
    UserEntity user = userList.getSelectionModel().getSelectedItem();
    if (user == null)
      return;

    UserService.update(txtDisplayName.getText(), txtUserName.getText(), txtPassword.getText(), false, user.getId());
    refreshData();
    txtUserName.setText("");
    txtPassword.setText("");
    txtDisplayName.setText("");

  }

  @FXML
  void deleteButtun(ActionEvent event) {
    UserEntity user = userList.getSelectionModel().getSelectedItem();
    if (user == null)
      return;
    UserService.delete(user);
    refreshData();
    txtUserName.setText("");
    txtPassword.setText("");
    txtDisplayName.setText("");

  }

  // get the data from the UserServices
  private void loadData() {
    allusers = UserService.getUsers();
    Users = FXCollections.observableArrayList(allusers);
    userList.getItems().clear();
    userList.getItems().addAll(Users);
  }

  // adding data to the columns
  private void initiatecols() {

    displayNameColumn.setCellValueFactory(new PropertyValueFactory<>("displayName"));

  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    refreshData();

  }
  // display name , user ,password when we select a user
  public void displaySelected(MouseEvent event) {
    UserEntity user = userList.getSelectionModel().getSelectedItem();
    if (user == null)
      return;
    String name = user.getDisplayName();
    String password = user.getPassword();
    String userName = user.getUsername();

    txtDisplayName.setText(name);
    txtUserName.setText(userName);
    txtPassword.setText(password);


  }

}





