package cookbook.frontend;

import java.io.IOException;

import cookbook.db.entities.QuantifiedIngredientEntity;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;

public class ShoppingListViewCell extends ListCell<QuantifiedIngredientEntity> {
  @FXML
  private Label ingrString;

  @FXML
  private GridPane gridPane;

  private FXMLLoader loader;

  @Override
  protected void updateItem(QuantifiedIngredientEntity ingrEntity, boolean empty) {
    super.updateItem(ingrEntity, empty);

    if (empty || ingrEntity == null) {
      ;
    } else {
      if (loader == null) {
        loader = new FXMLLoader(getClass().getResource("shoppingListCell.fxml"));
        loader.setController(this);

        try {
          loader.load();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      //system.out.println("\n\n"+commentEntity.getBody()+"\n\n");
      ingrString.setText(String.valueOf(ingrEntity.toString()));
    }
    setText(null);
    setGraphic(gridPane);
  }
}