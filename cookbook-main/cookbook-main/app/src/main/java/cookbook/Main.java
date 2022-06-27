package cookbook;
import java.util.Arrays;
import java.util.List;

import cookbook.db.DatabaseManager;
import cookbook.frontend.MainApp;
import cookbook.util.ConfigManager;
import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        List<String> argList = Arrays.asList(args);

        // load the toml file
        ConfigManager.load();

        // connect to db
        DatabaseManager.connect(argList.contains("drop"), argList.contains("seed"));
        
        // launch javafx
        Application.launch(MainApp.class, args);
    }
}
