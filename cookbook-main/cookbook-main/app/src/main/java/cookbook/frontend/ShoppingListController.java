package cookbook.frontend;

import cookbook.GlobalState;
import cookbook.db.entities.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;

public class ShoppingListController implements Initializable {

    @FXML
    private ListView<QuantifiedIngredientEntity> ingView;
    @FXML
    private Button delBtn;
    @FXML
    private Button modBtn;
    @FXML
    private Button amtUp;
    @FXML
    private Button amtDown;
    @FXML
    private Label currentIng;
    @FXML
    private Label amt_text;

    private LocalDate startingDateGlobal;

    private ObservableList<QuantifiedIngredientEntity> ingredients = FXCollections.observableArrayList();
    private ObservableList<QuantifiedIngredientEntity> e = FXCollections.observableArrayList();


    private void clear() {
        ingView.getItems().clear();
        ingredients.clear();
    }

    public String stringRep() {
        StringBuilder str = new StringBuilder();
        for (QuantifiedIngredientEntity qe : ingView.getItems()) {
          str.append(qe.toData() + "\n");
        }
        String outstring = str.toString();
        return outstring;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clear();
        ingView.setCellFactory(ingr -> new ShoppingListViewCell());
        ingView.setItems(ingredients);

        ingView.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<QuantifiedIngredientEntity>() {
                @Override
                public void changed(ObservableValue<? extends QuantifiedIngredientEntity> ob, QuantifiedIngredientEntity oldQe, QuantifiedIngredientEntity newQe) {
                    selectQe(newQe);
                }
            });
    }

    public void getShoppingList(ObservableList<QuantifiedIngredientEntity> shoppingList, LocalDate ld) {
        startingDateGlobal = ld;
        List<QuantifiedIngredientEntity> shp = read();
        if ( shp == null) {
            ingredients.addAll(shoppingList);
        } else {
            ingredients.addAll(shp);
        }
        
    }

    public void selectQe(QuantifiedIngredientEntity qe) {
        if (qe != null){
            amt_text.setText(String.valueOf(qe.getAmount()));
            currentIng.setText(qe.getName());
        } else {
            return;
        }
    }

    @FXML
    public void onModyBtn(ActionEvent event) {
        QuantifiedIngredientEntity qe = ingView.getSelectionModel().getSelectedItem();
        if (qe == null) {
            return;
        } else {
            qe.setAmount(Float.valueOf(amt_text.getText()));
            ingView.setItems(e);
            ingView.setItems(ingredients);
            save();
        }
    }

    @FXML
    public void onDelBtn (ActionEvent event) {
        QuantifiedIngredientEntity qe = ingView.getSelectionModel().getSelectedItem();
        if (qe == null) {
            return;
        } else {
            ingredients.remove(qe);
            ingView.setItems(e);
            ingView.setItems(ingredients);
            save();
        }
    }

    @FXML
    public void onUp(ActionEvent event) {
        QuantifiedIngredientEntity qe = ingView.getSelectionModel().getSelectedItem();
        if (qe == null) {
            return;
        } else {
            Float currentAmt = Float.valueOf(amt_text.getText());
            String newAmt = String.valueOf(currentAmt+1);
            amt_text.setText(newAmt);
        }
    }

    @FXML
    public void onDown(ActionEvent event) {
        QuantifiedIngredientEntity qe = ingView.getSelectionModel().getSelectedItem();
        if (qe == null) {
            return;
        } else {
            Float currentAmt = Float.valueOf(amt_text.getText());
            if (currentAmt > 0) {
                String newAmt = String.valueOf(currentAmt-1);
                amt_text.setText(newAmt);
            } else {
                amt_text.setText("0");
            }
        }
    }

    
    public void save () {
        // Try to read Shopping list data.
        String pathdate = startingDateGlobal.toString();
        String userId = GlobalState.user.getId();
        String basePath = "src\\main\\resources\\cookbook\\shopping";
        String folderPath = basePath + "\\" + userId;
        String fullPath = folderPath + "\\" + pathdate + ".data";

        try{
            //If the folder doesnt exists, create it
            if (!new File(folderPath).exists()) {
                new File(folderPath).mkdir();

                // If the folder didnt exist, the file also is new
                File file = new File(fullPath);
                file.createNewFile();
            
            } else {
                //If folder existed, but file didn't
                if (!new File(fullPath).exists()) {
                    // If the folder didnt exist, the file also is new
                    File file = new File(fullPath);
                    file.createNewFile();
                
                }
                //if the folder and file existed already
                OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(fullPath), StandardCharsets.UTF_8);
                BufferedWriter bwriter =  new BufferedWriter(out);
                bwriter.write(stringRep());
                bwriter.close();
            
                //System.out.println("\nChanges Saved.");
            }
        } catch (IOException e) {
            //System.out.println("No file found, creating.");
            return;
        }
    }

    public  List<QuantifiedIngredientEntity> read() {
        // Try to read Shopping list data.
        String pathdate = startingDateGlobal.toString();
        String userId = GlobalState.user.getId();
        String basePath = "src\\main\\resources\\cookbook\\shopping";
        String fullPath = basePath + "\\" + userId + "\\" + pathdate + ".data";

        //Shopping list from file
        List<QuantifiedIngredientEntity> shp = new ArrayList<>();

        //System.out.println("Here!!");

        try {
            //Check if the file exists
            if (new File(fullPath).exists()) {
                File file = new File(fullPath);
                Scanner scn = new Scanner(file, "utf-8");
                // In a populated file, the first line should always be a USER.

                // This is is the file is empty
                if ((scn.hasNextLine()) == false) {
                    // Have to read blank file
                    // Set shopping list to blank
                    //System.out.println("File is empty");
                    scn.close();
                    return null;

                // This is if the file exists and has data
                } else {
                    //Have to read file
                    String line = scn.nextLine();
                    String[] values = line.split(":");

                    // If first line is not USER then brek try and return
                    if (values[0].equals("INGREDIENT") == false) {
                        // Thisshould raise an alert for corrupted data
                        //System.out.println("First entry is not Ingredient data.\nPlease check data file format.");
                        scn.close();
                        return null;
                    }

                    // Add first ingredient to list
                    QuantifiedIngredientEntity currentIngredient = new QuantifiedIngredientEntity(values[2], Float.valueOf(values[1]), values[3]);
                    shp.add(currentIngredient);

                    // File exists and is not corrupted
                    while(scn.hasNext()) {
                        String ingredientLine = scn.nextLine();
                        values = ingredientLine.split(":");

                        if (values[0].equals("INGREDIENT")) {
                            currentIngredient = new QuantifiedIngredientEntity(values[2], Float.valueOf(values[1]), values[3]);
                            shp.add(currentIngredient);
                        }
                    }
                    scn.close();
                    
                    return shp;
                }
            } else {
                return null;
            }
        } catch (FileNotFoundException e) {
            //System.out.println("No file found, creating.");
            return null;
        }
    }
}