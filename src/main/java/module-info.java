module org.example.pcbuilderapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.slf4j;

    opens org.example.pcbuilderapplication to javafx.fxml;
    opens org.example.pcbuilderapplication.controllers to javafx.fxml;

    exports org.example.pcbuilderapplication;
    exports org.example.pcbuilderapplication.controllers;
}