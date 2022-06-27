package cookbook.util;

import java.nio.file.Files;
import java.nio.file.Path;

import com.moandjiezana.toml.Toml;

public class ConfigManager {
    public static String dbUser;
    public static String dbPass;

    public static void load() {
        try {
            // read the config file
            // the cfg.sample.toml file is a template
            // it should be copied, changed to "cfg.toml" and filled with the appropriate credentials
            String tomlFile = Files.readString(Path.of("../cfg.toml"));
            // parse the toml
            Toml toml = new Toml().read(tomlFile);
            // get the two properties for db credentials
            dbUser = toml.getString("db.user");
            dbPass = toml.getString("db.pass");
        } catch (Exception e) {
            throw new RuntimeException("Couldn't load the cfg.toml file");
        }
        
    }
}
