package cookbook.frontend;

import java.io.IOException;

import cookbook.db.entities.CommentEntity;
import cookbook.db.services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;

public class CommentListViewCell extends ListCell<CommentEntity> {
  @FXML
  private Label comment_user;

  @FXML
  private Label comment_text;

  @FXML
  private GridPane gridPane;

  private FXMLLoader loader;

  @Override
  protected void updateItem(CommentEntity commentEntity, boolean empty) {
    super.updateItem(commentEntity, empty);

    if (empty || commentEntity == null || commentEntity.getBody() == null) {
      ;
    } else {
      if (loader == null) {
        loader = new FXMLLoader(getClass().getResource("listcell.fxml"));
        loader.setController(this);

        try {
          loader.load();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      //system.out.println("\n\n"+commentEntity.getBody()+"\n\n");
      comment_text.setText(commentEntity.getBody());
      comment_user.setText(UserService.getByUserId(commentEntity.getUserId()).getUsername());
    }
    setText(null);
    setGraphic(gridPane);
  }
}