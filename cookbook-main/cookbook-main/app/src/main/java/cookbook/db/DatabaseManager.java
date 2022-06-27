package cookbook.db;

import java.nio.file.Path;
import java.sql.*;

import cookbook.util.ConfigManager;

public class DatabaseManager {
    static Connection conn;

    public static void connect(boolean shouldDropSchema, boolean shouldSeed) {
        try {
            // use db credentials from the config file
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost", ConfigManager.dbUser, ConfigManager.dbPass);

            createSchema(shouldDropSchema);

            if (shouldSeed) {
                seed();
            }

            try (PreparedStatement st = conn.prepareStatement("USE cookbook")) {
                st.execute();
            } catch (SQLException e) {
                System.out.println(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConn() {
        return conn;
    }

    private static void createSchema(boolean shouldDropSchema) {
        boolean exists = false;

        try (PreparedStatement st = conn.prepareStatement("SHOW DATABASES LIKE 'cookbook'")) {
            ResultSet result = st.executeQuery();
            exists = result.next();
            result.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        if (shouldDropSchema && exists) {
            try (PreparedStatement st = conn.prepareStatement("DROP DATABASE cookbook")) {
                st.execute();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
        if (shouldDropSchema || !exists) {
            try {
                String path = Path.of("").toAbsolutePath().toString()
                        + "\\src\\main\\java\\cookbook\\db\\createSchema.py";
                // pass the db credentials to the script
                new ProcessBuilder("python", path, ConfigManager.dbUser, ConfigManager.dbPass).inheritIO().start()
                        .waitFor();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private static void seed() {
        try {
            String path = Path.of("").toAbsolutePath().toString() + "\\src\\main\\java\\cookbook\\db\\seeder.py";
            // same as above
            new ProcessBuilder("python", path, ConfigManager.dbUser,
                    ConfigManager.dbPass).inheritIO().start().waitFor();
        } catch (Exception e) {
            System.out.println(e);
        }
    }


}
