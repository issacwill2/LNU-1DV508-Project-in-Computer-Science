package cookbook.frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import cookbook.db.services.UserService;


import cookbook.GlobalState;
import cookbook.db.entities.UserEntity;

public class LoginController implements Initializable {

    @FXML
    private TextField user;

    @FXML
    private TextField passwordtext;

    @FXML // press logIn to Homepage
    private void moveToHomePage(ActionEvent event) throws IOException {
        String uname = user.getText();
        String pass = passwordtext.getText();

        UserEntity userEntity = UserService.get(uname, pass);

        GlobalState.user = userEntity;

        if (userEntity != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("firstpage.fxml"));
            Parent root = (Parent) loader.load();

            FirstPageController setController = loader.getController();
            setController.showInformation(user.getText());
            Scene homePageScene = new Scene(root);
            homePageScene.getStylesheets().add(
                    "https://fonts.googleapis.com/css?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap");

            homePageScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

            Stage appStage =
                    (Stage) ((Node) event.getSource()).getScene().getWindow();
            appStage.hide();
            appStage.setScene(homePageScene);
            appStage.show();
            appStage.setHeight(750);
            appStage.setWidth(1069);
            appStage.show();
            appStage.centerOnScreen();
            appStage.setTitle("Cookbook");
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setContentText("please check your login information");
            alert.show();
            user.setText("");
            passwordtext.setText("");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
