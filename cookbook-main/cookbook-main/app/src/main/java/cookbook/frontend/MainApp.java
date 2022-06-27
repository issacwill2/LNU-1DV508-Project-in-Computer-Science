package cookbook.frontend;

import javafx.animation.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import javafx.scene.Scene;

import javafx.scene.Group;

import javafx.animation.Timeline;
import javafx.animation.KeyFrame;

import java.io.IOException;
import java.nio.file.Path;


public class MainApp extends Application {



  public void start(Stage primaryStage) {
    System.setProperty("prism.lcdtext", "false");
    System.setProperty("prism.text", "t2k");

    Button button = new Button("Start the Program");
    button.setTranslateX(230);
    button.setTranslateY(500);
    button.setStyle("-fx-font-size:20");
    button.setOnAction(new EventHandler<>() {
      public void handle(ActionEvent event) {


        Parent homePage = null;

        try {
          homePage = (Parent) FXMLLoader.load(this.getClass().getResource("login.fxml"));
        } catch (IOException var5) {
          var5.printStackTrace();
        }

        Scene homePageScene = new Scene(homePage);
        homePageScene.getStylesheets().add(
            "https://fonts.googleapis.com/css?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap");

        homePageScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.hide();
        appStage.setScene(homePageScene);
        appStage.show();
        appStage.setHeight(400);
        appStage.setWidth(315);
        appStage.show();
        appStage.setResizable(false);
        appStage.centerOnScreen();
        appStage.setTitle("Sign In");
      }
    });

    Group root = new Group();
    Scene scene = new Scene(root, 640, 630 );

    scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap");

    scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

    String path = "file:\\" + Path.of("").toAbsolutePath() + "\\src\\main\\resources\\cookbook\\images\\start.png";

    ImageView background = new ImageView(new Image(path));
    String path2 = "file:\\" + Path.of("").toAbsolutePath() + "\\src\\main\\resources\\cookbook\\images\\1.png";

    ImageView picOne = new ImageView(new Image(path2));
    String path3 = "file:\\" + Path.of("").toAbsolutePath() + "\\src\\main\\resources\\cookbook\\images\\3.png";

    ImageView picTwo = new ImageView(new Image(path3));
    String path4 = "file:\\" + Path.of("").toAbsolutePath() + "\\src\\main\\resources\\cookbook\\images\\4.png";

    ImageView picThree = new ImageView(new Image(path4));
    String path5 = "file:\\" + Path.of("").toAbsolutePath() + "\\src\\main\\resources\\cookbook\\images\\5.png";

    ImageView picFour = new ImageView(new Image(path5));

    Text text = new Text();
    text.setStyle("-fx-font-size: 60; -fx-font-weight: 700");


    text.setText("Welcome");
    text.setLayoutX(185);
    text.setLayoutY(470);

    picOne.setFitWidth(100);
    picOne.setPreserveRatio(true);
    picOne.setSmooth(true);
    picOne.setLayoutX(300);
    picOne.setLayoutY(0);

    picTwo.setFitWidth(100);
    picTwo.setPreserveRatio(true);
    picTwo.setSmooth(true);
    picTwo.setLayoutX(300);
    picTwo.setLayoutY(100);

    picThree.setFitWidth(100);
    picThree.setPreserveRatio(true);
    picThree.setSmooth(true);
    picThree.setLayoutX(300);
    picThree.setLayoutY(200);


    picFour.setFitWidth(100);
    picFour.setPreserveRatio(true);
    picFour.setSmooth(true);
    picFour.setLayoutX(300);
    picFour.setLayoutY(300);

    FadeTransition ft = new FadeTransition(Duration.millis(3000), background);
    ft.setFromValue(1.0);
    ft.setToValue(0.1);
    ft.setCycleCount(Timeline.INDEFINITE);
    ft.setAutoReverse(true);
    ft.play();

    Timeline tml = new Timeline();
    tml.setCycleCount(Timeline.INDEFINITE);
    KeyFrame movePlane = new KeyFrame(Duration.millis(10000),
            new KeyValue(picOne.translateXProperty(), 1000),
            new KeyValue(picTwo.translateXProperty(), -1000),
            new KeyValue(picThree.translateXProperty(), 1000),
            new KeyValue(picFour.translateXProperty(), -1000));



    tml.getKeyFrames().add(movePlane);
    tml.play();
    primaryStage.setResizable(false);

    root.getChildren().addAll(background,picOne, picTwo,picThree,picFour, text,button);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
