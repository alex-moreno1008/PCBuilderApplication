module org.example.pcbuilderapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.slf4j;
  requires java.net.http;
    requires java.desktop;

    opens org.example.pcbuilderapplication to javafx.fxml;
    opens org.example.pcbuilderapplication.controllers to javafx.fxml;
    opens org.example.pcbuilderapplication.models to org.junit.jupiter.api;  // add this

    exports org.example.pcbuilderapplication;
    exports org.example.pcbuilderapplication.controllers;
    exports org.example.pcbuilderapplication.models;  // add this
}