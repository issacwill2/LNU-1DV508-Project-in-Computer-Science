module cookbook {
  requires javafx.controls;
  requires javafx.fxml;

  requires org.controlsfx.controls;
  requires com.dlsc.formsfx;

  requires javafx.graphics;
  requires java.sql;

  requires toml4j;

  exports cookbook.frontend;
  opens cookbook.frontend to javafx.fxml;
  opens cookbook.db.entities;
}