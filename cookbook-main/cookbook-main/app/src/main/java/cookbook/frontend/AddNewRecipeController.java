package cookbook.frontend;

import cookbook.db.entities.IngredientEntity;
import cookbook.db.entities.QuantifiedIngredientEntity;
import cookbook.db.entities.RecipeEntity;
import cookbook.db.entities.TagEntity;
import cookbook.db.services.RecipeService;
import cookbook.db.services.TagService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;

public class AddNewRecipeController implements Initializable {

    private List<TagEntity> tags = new ArrayList<>();
    private List<QuantifiedIngredientEntity> ingredients = new ArrayList<>();

    @FXML
    private TextField recipeName;

    @FXML
    private TextArea recipeShortDesc;

    @FXML
    private TextArea recipeLongDesc;

    @FXML
    private ComboBox<String> tagsDropdown;

    @FXML
    private TextField tagName;

    @FXML
    private Label tagsLabel;

    @FXML
    private Label ingredientsLabel;

    @FXML
    private TextField ingredientAmount;

    @FXML
    private ComboBox<String> ingredientUnit;

    @FXML
    private TextField ingredientName;

    @FXML
    private void addNewRecipeBtn(ActionEvent event) throws IOException {
        try {
            RecipeEntity recipe = new RecipeEntity(UUID.randomUUID().toString(), recipeName.getText(), recipeShortDesc.getText(), recipeLongDesc.getText(), false);
            for (QuantifiedIngredientEntity qie : ingredients) {
                recipe.addQuantifiedIngredient(qie);
            }
            for (TagEntity tag : tags) {
                recipe.addTag(tag);
            }
            RecipeService.add(recipe);
        } catch(NumberFormatException e) {
            return;
        }
        
    }

    @FXML
    private void onAddTag(ActionEvent event) throws IOException {
        String name = tagName.getText();
        if (name.length() > 0) {
            tags.add(new TagEntity(UUID.randomUUID().toString(), tagName.getText()));
        }
        tagName.setText("");
        updateTagsLabel();
        
    }

    @FXML
    private void onAddIngredient(ActionEvent event) throws IOException {
        try{
            ingredients.add(
                    new QuantifiedIngredientEntity(ingredientUnit.getValue(), Float.parseFloat(ingredientAmount.getText()),
                            new IngredientEntity(UUID.randomUUID().toString(), ingredientName.getText())));
            ingredientAmount.setText("");
            ingredientUnit.setValue(null);
            ingredientName.setText("");
            updateIngredientsLabel();
        } catch(NumberFormatException e) {
            return;
        }
    }

    @FXML
    private void onTagsDropdown(ActionEvent event) throws IOException {
        String selected = tagsDropdown.getSelectionModel().getSelectedItem();
        if (selected != null) {
            tags.add(new TagEntity(UUID.randomUUID().toString(), selected));
            tagsDropdown.setValue(null);
            tagName.setText("");
            //System.out.println(selected);
            updateTagsLabel();
        }
    }

    private void updateTagsDropdown() {
        tagsDropdown.getItems().add(null);
        tagsDropdown.getItems().addAll(TagService.getAll().stream().map(x -> x.getName()).collect(Collectors.toList()));
    }

    private void updateTagsLabel() {
        tagsLabel.setText("Tags: " + String.join(", ", tags.stream().map(x -> x.getName()).collect(Collectors.toList())));
    }

    private void updateIngredientsLabel() {
        String ingredientsString = "";
        for (QuantifiedIngredientEntity quantifiedIngredient : ingredients) {
            float ingredientAmount = quantifiedIngredient.getAmount();
            String ingredientUnit = quantifiedIngredient.getUnit() != null ? " " + quantifiedIngredient.getUnit() : "";
            String ingredientName = quantifiedIngredient.getIngredient().getName();
            ingredientsString += String.format("%s%s %s\n", ingredientAmount, ingredientUnit, ingredientName);
        }
        ingredientsLabel.setText(ingredientsString);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateTagsDropdown();
        ingredientUnit.setItems(FXCollections.observableArrayList(null,"g", "ml", "dollop", "clove", "pinch", "tsp", "tbsp"));
    }
}
